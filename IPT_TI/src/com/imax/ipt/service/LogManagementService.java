package com.imax.ipt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.imax.ipt.IPT;
import com.imax.ipt.conector.Client;
import com.imax.ipt.controller.eventbus.handler.push.RequestClientLogEvent;
import com.imax.iptevent.EventBus;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogManagementService extends Service {
    private final static String TAG = "LogManagementService";

    private final static String LOG_FOLDER_NAME = "/logcat";
    private final static String FTP_SERVER_IP_OR_HOSTNAME = Client.host;
    private final static String FTP_USERNAME = "imaxIptFtp";
    private final static String FTP_PASSWORD = "imax_1970";
    private final static String FTP_FOLDER = "/ClientLogs";

    private EventBus mEventBus;

    private String state = Environment.getExternalStorageState();
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    private File folder;

    public LogManagementService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        }

        if (mExternalStorageAvailable) {
            Log.d(TAG, "Storage available");

            // create the logcat folder
            folder = new File(getExternalFilesDir(null).getAbsolutePath() + LOG_FOLDER_NAME);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (!success) {
                Log.wtf(TAG,
                        "failed to create folder: " + folder.getAbsolutePath());
            }

            File file = new File(folder, "Ipt.log");
            String cmd = "logcat -v threadtime -r 8192 -n 21 -f "
                    + file.getAbsolutePath();
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                Log.e(TAG, "logcat IOException", e);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mEventBus.unregister(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onEvent(final RequestClientLogEvent requestClientLogEvent) {
        Log.d(TAG, "Client log will be packaged");

        // send the encoded-zip-packaged client log to server
        if (folder != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmssZ");
            String fileName = String.format("IptLog_%s_%s.zip", Build.SERIAL, sdf.format(new Date()));

            // send log package through JSON
//         String encoded64String = getZipPackageEncodedString(folder, fileName);
//         Log.d(TAG, "encodedString: " + encoded64String);
//                  
//         this.mEventBus.post(new SendCompressedLogHandler(fileName, encoded64String).getRequest());

            getZipPackageEncodedString(folder, fileName);


            // FTP the log package to the server
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER_IP_OR_HOSTNAME, 21);
                Log.i(TAG, "Connected to FTP: " + FTP_SERVER_IP_OR_HOSTNAME + " (" + ftpClient.getReplyString() + ")");

                // After connection attempt, you should check the reply code to verify
                // success.
                int reply = ftpClient.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    Log.e(TAG, "FTP server refused connection.");
                }

                ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
                ftpClient.changeWorkingDirectory(FTP_FOLDER);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                BufferedInputStream buffIn = null;
                buffIn = new BufferedInputStream(new FileInputStream(new File(getExternalFilesDir(null),
                        fileName)));
                ftpClient.enterLocalPassiveMode();

                ftpClient.storeFile(fileName, buffIn);
                buffIn.close();
                ftpClient.logout();
                ftpClient.disconnect();

            } catch (SocketException e) {
                Log.e(TAG, "FTP SocketException", e);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "FTP FileNotFoundException", e);
            } catch (IOException e) {
                Log.e(TAG, "FTP IOException", e);
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        Log.e(TAG, "FTP disconnect IOException", e);
                    }
                }
            }

            // remove all zip files sent
            removeZipFiles(getExternalFilesDir(null));
        }
    }

    private void getZipPackageEncodedString(File folder, String fileName) {
        File zipFile = new File(getExternalFilesDir(null),
                fileName);
        try {
            zipDirectory(folder, zipFile);
        } catch (IOException e) {
            Log.e(TAG, "zipDirectory IOException", e);
        }


//      // encode the result zip file into string for sending
//      // TODO: may need to truncate the file into smaller segments
//      byte[] bytes = new byte[(int) zipFile.length()];
//      FileInputStream fileInputStream = null;
//      try {
//         fileInputStream = new FileInputStream(zipFile);
//         fileInputStream.read(bytes, 0, bytes.length);                                 
//      } catch (FileNotFoundException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      } catch (IOException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
//      finally {
//         try
//         {
//            if (fileInputStream != null)
//               fileInputStream.close();
//         } catch (IOException e)
//         {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//         }
//      }
//      
//      
//      String encodedString = Base64.encodeToString(bytes, 0, bytes.length, Base64.DEFAULT);
//      Log.w(TAG, "encodedString = " + encodedString);
//      
//      return encodedString;
    }

    public static final void zipDirectory(File directory, File zip)
            throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        zip(directory, directory, zos);
        zos.close();
    }

    private static final void zip(File directory, File base, ZipOutputStream zos)
            throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }

    private void removeZipFiles(File directory) {
        String zipExtension = "zip";

        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(zipExtension)) {
                    files[i].delete();
                }
            }
        }
    }
}
