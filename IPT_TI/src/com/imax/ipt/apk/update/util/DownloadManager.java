package com.imax.ipt.apk.update.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.imax.ipt.R;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressDialog;
    private String TAG = "DownloadManager";
    private boolean installAfterDownload = true;
    private boolean downloaded = false;
    private Context mContext;

    /**
     * Constructor for the Download Manager. DO NOT USE ON YOUR OWN. All calls are through UpdateChecker
     *
     * @param context Activity context
     * @since API 1
     */
    public DownloadManager(Context context) {
        this(context, true);
    }

    /**
     * Constructor for the download manager. DO NOT USE ON YOUR OWN. All calls are handled through UpdateChecker.
     *
     * @param context              Activity context
     * @param installAfterDownload true if you want to install immediately after download, false otherwise
     * @since API 2
     */
    public DownloadManager(Context context, boolean installAfterDownload) {
        mContext = context;
        this.installAfterDownload = installAfterDownload;
//        Toast toast = Toast.makeText(mContext, R.string.dialog_message_software_update_force, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();

        }

                /**
                 * Checks to see if we have an active internet connection
                 *
                 * @return true if online, false otherwise
                 * @since API 1
                 */

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Downloads the update file in a background task
     *
     * @since API 1
     */
    @Override
    protected String doInBackground(String... sUrl) {
        if (isOnline()) {
            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/appupdate.apk");

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Log.e(TAG, "There was an IOException when downloading the update file", e);
            }
        }
        return null;
    }

    /**
     * Updates our progress bar with the download information
     *
     * @since API 1
     */
    @Override
    protected void onProgressUpdate(Integer... changed) {
        progressDialog.setProgress(changed[0]);
    }

    /**
     * Sets up the progress dialog to notify user of download progress
     *
     * @since API 1
     */
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(mContext.getString(R.string.apk_update_downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Dismissed progress dialog, calls install() if installAfterDownload is true
     *
     * @since API 1
     */
    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        downloaded = true;
        if (installAfterDownload) {
            install();
        }
    }

    /**
     * Launches an Intent to install the apk update.
     *
     * @since API 2
     */
    static final int REQUEST_INSTALL = 1;

    public void install() {
        if (downloaded) {
            String filepath = Environment.getExternalStorageDirectory().getPath() + "/appupdate.apk";
            Uri fileLoc = Uri.fromFile(new File(filepath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // adding this flag allow an "Open" button after re-installation is done
            intent.setDataAndType(fileLoc, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }
}
