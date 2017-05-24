package com.imax.ipt.apk.update.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UpdateChecker {
    private static String TAG = "UpdateChecker";
    private boolean updateAvailable = false;
    private boolean forceUpdate = false;
    private Context mContext;
    private static boolean haveValidContext = false;
    private static boolean useToasts = false;
    private DownloadManager downloadManager;

    /**
     * Constructor that only takes the Activity context.
     * <p>
     * This constructor sets the toast notification functionality to false. Example call:
     * UpdateChecker uupdateChecker = new UpdateChecker(this);
     *
     * @param context An instance of your Activity's context
     * @since API 1
     */
    public UpdateChecker(Context context) {
        this(context, false);
    }

    /**
     * Constructor for UpdateChecker
     * <p>
     * Example call:
     * UpdateChecker uupdateChecker = new UpdateChecker(this, false);
     *
     * @param context An instance of your Activity's context
     * @param toasts  True if you want toast notifications, false by default
     * @since API 2
     */
    public UpdateChecker(Context context, boolean toasts) {
        mContext = context;
        if (mContext != null) {
            haveValidContext = true;
            useToasts = toasts;
        }
    }

    /**
     * Checks for app update by version code.
     * <p>
     * Example call:
     * updateChecker.checkForUpdateByVersionCode("http://www.example.com/version.txt");
     *
     * @param url URL at which the text file containing your latest version code is located.
     * @since API 1
     */
    // this function will return the remote version code
    public int checkForUpdateByVersionCode(String url) {
        if (isOnline()) {
            if (haveValidContext) {
                int versionCode = getVersionCode();
                int readCode = 0;
                if (versionCode >= 0) {
                    try {

//                        readCode = Integer.parseInt(readFile(url));
                        List<VersionInfor> versionInfors = parseXml(url);
                        if(versionInfors.size() ==0){
                        Log.e(TAG, "cll Invalid versioninfors"); //Something wrong with the file content
                            return 0;
                        }
                        readCode = versionInfors.get(0).getVerCode();
                        // Check if update is available.
                        if (readCode > versionCode) {
                            updateAvailable = true; //We have an update available
                            forceUpdate = versionInfors.get(0).isForceUpdate();
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid number online"); //Something wrong with the file content
                    }

                } else {
                    Log.e(TAG, "Invalid version code in app"); //Invalid version code
                }

                return readCode;
            } else {
                Log.e(TAG, "Context is null"); //Context was null
            }
        } else {
            if (useToasts) {
                makeToastFromString("App update check failed. No internet connection available").show();
            }
        }

        return 0;
    }

    /**
     * Get's the version code of your app by the context passed in the constructor
     *
     * @return The version code if successful, -1 if not
     * @since API 1
     */
    public int getVersionCode() {
        int code;
        try {
            code = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
            return code; // Found the code!
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Version Code not available"); // There was a problem with the code retrieval.
        } catch (NullPointerException e) {
            Log.e(TAG, "Context is null");
        }

        return -1; // There was a problem.
    }

    /**
     * Downloads and installs the update apk from the URL
     *
     * @param apkUrl URL at which the update is located
     * @since API 1
     */
    public void downloadAndInstall(String apkUrl) {
        if (isOnline()) {

            downloadManager = new DownloadManager(mContext, true);
            downloadManager.execute(apkUrl);
        } else {
            if (useToasts) {
                makeToastFromString("App update failed. No internet connection available").show();
            }
        }
    }

    /**
     * Must be called only after download().
     *
     * @throws NullPointerException Thrown when download() hasn't been called.
     * @since API 2
     */
    public void install() {
        downloadManager.install();
    }

    /**
     * Downloads the update apk, but does not install it
     *
     * @param apkUrl URL at which the update is located.
     * @since API 2
     */
    public void download(String apkUrl) {
        if (isOnline()) {
            downloadManager = new DownloadManager(mContext, false);
            downloadManager.execute(apkUrl);
        } else {
            if (useToasts) {
                makeToastFromString("App update failed. No internet connection available").show();
            }
        }
    }

    /**
     * Should be called after checkForUpdateByVersionCode()
     *
     * @return Returns true if an update is available, false if not.
     * @since API 1
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    /**
     * Checks to see if an Internet connection is available
     *
     * @return True if connected or connecting, false otherwise
     * @since API 2
     */
    public  boolean isOnline() {
        if (haveValidContext) {
            try {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                return cm.getActiveNetworkInfo().isConnectedOrConnecting();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Launches your app's page on Google Play if it exists.
     *
     * @since API 2
     */
    public void launchMarketDetails() {
        if (haveValidContext) {
            if (hasGooglePlayInstalled()) {
                String marketPage = "market://details?id=" + mContext.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(marketPage));
                mContext.startActivity(intent);
            } else {
                if (useToasts) {
                    makeToastFromString("Google Play isn't installed on your device.").show();
                }
            }
        }
    }

    /**
     * Checks to use if the user's device has Google Play installed
     *
     * @return true if Google Play is installed, otherwise false
     * @since API 2
     */
    public boolean hasGooglePlayInstalled() {
        Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dummy"));
        PackageManager manager = mContext.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(market, 0);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).activityInfo.packageName.startsWith("com.android.vending") == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Makes a toast message with a short duration from the given text.
     *
     * @param text The text to be displayed by the toast
     * @return The toast object.
     * @since API 2
     */
    @SuppressLint("ShowToast")
    public Toast makeToastFromString(String text) {
        Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * Reads a file at the given URL
     *
     * @param url The URL at which the file is located
     * @return Returns the content of the file if successful
     * @since API 1
     */
    public String readFile(String url) {
        String result;
        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            result = bufferedReader.readLine();
//            parseXml(url);
            inputStream.close();
            return result;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Invalid URL");
        } catch (IOException e) {
            Log.e(TAG, "There was an IO exception");
        }

        Log.e(TAG, "There was an error reading the file");
        return "Problem reading the file";
    }

    public List<VersionInfor> parseXml(String url) {
//        String result ="";
        List<VersionInfor> versionInfors = new ArrayList<VersionInfor>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputStream inputStream = null;
        URLConnection conn = null;
//            inputStream = new URL(url).openStream();
        try {
            conn = new URL(url).openConnection();
            conn.setConnectTimeout(5000);// 2-2 error, 3-2 error,3-3 ok
            conn.setReadTimeout(5000);
            inputStream = conn.getInputStream();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(inputStream);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("versionInfor");//�������нڵ�

            for (int i = 0; i < items.getLength(); i++) {
                VersionInfor versionInfor = new VersionInfor();
                //�õ���һ���ڵ�
                Element versionInforNode = (Element) items.item(i);
                //��ȡ�ڵ������ֵ
                versionInfor.setId(new Integer(versionInforNode.getAttribute("id")));
                //��ȡ�ڵ��µ������ӽڵ�(��ǩ֮��Ŀհ׽ڵ��name/ageԪ��)
                NodeList childsNodes = versionInforNode.getChildNodes();
                for (int j = 0; j < childsNodes.getLength(); j++) {
                    Node node = (Node) childsNodes.item(j); //�ж��Ƿ�ΪԪ������
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        final Element childNode = (Element) node;
                        switch (childNode.getNodeName()) {
                            case "apkName":
                                versionInfor.setApkName(childNode.getFirstChild().getNodeValue());
                                break;
                            case "appName":
                                break;
                            case "verCode":

                                versionInfor.setVerCode(new Short(childNode.getFirstChild().getNodeValue()));
                                break;
                            case "verName":
                                versionInfor.setVerName(childNode.getFirstChild().getNodeValue());
                                break;
                            case "forceUpdate":
                                versionInfor.setForceUpdate(new Boolean(childNode.getFirstChild().getNodeValue()));
                                break;
                            case "description":
                                versionInfor.setDescription(childNode.getFirstChild().getNodeValue());
                                break;
                            default:
                                //do nothing
                        }

                    }

                }
                versionInfors.add(versionInfor);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid URL");

        } catch (IOException e) {
            Log.e(TAG, "There was an error reading the file");
            e.printStackTrace();
//            e.getLocalizedMessage();
            e.getCause().printStackTrace();
        }finally {

            return versionInfors;

        }
    }
//    public void parseXml(String url){
//        String result ="";
//        InputStream inputStream;
//        try {
//            inputStream = new URL(url).openStream();
////            inputStream = new URL(url).hashCode();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
//            while (bufferedReader.readLine() != null){
//            result += bufferedReader.readLine();
//
//            }
//            Log.e(TAG, " cll read file from URL: " + result);
//
//                //����������
//                SAXParserFactory spf = SAXParserFactory.newInstance();
//                SAXParser saxParser = spf.newSAXParser();
//
//                //���ý�������������ԣ�true��ʾ���������ռ�����
////                saxParser.setProperty("http://xml.org/sax/features/namespaces",true);
////            String[] tag = {"apkName","verCode","forceUpdate","verName","appName","discription",};
////            String[] tag = {"name","age"};
//
//            XMLContentHandler handler = new XMLContentHandler();
//            saxParser.parse(inputStream, handler);
//
//            inputStream.close();
//            Log.d(TAG,"cll parse xml verName: " + handler.getVersionInfors());
//
////            return result;
//        } catch (MalformedURLException e) {
//            Log.e(TAG, "Invalid URL");
//        } catch (IOException e) {
//            Log.e(TAG, "There was an IO exception");
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXNotSupportedException e) {
//            e.printStackTrace();
//        } catch (SAXNotRecognizedException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//
////        Log.e(TAG, "There was an error reading the file");
////        return "Problem reading the file";
//
//    }

}

//04-22 16:03:14.577  23744-23761/com.imax.ipt I/System.out�s (HTTPLog)-Static: (This is just Trace Log)java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Class java.lang.Object.getClass()' on a null object reference
