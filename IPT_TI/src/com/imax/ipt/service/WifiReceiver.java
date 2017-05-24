package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

    public static final String TAG = "WifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
//          Intent iptServerConnectionLostIntent = new Intent(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
//          LocalBroadcastManager.getInstance(context).sendBroadcast(iptServerConnectionLostIntent);         

            Intent iptServiceIntent = new Intent(context, ConnecteService.class).setAction(ConnecteService.ACTION_CONNECTION_NOT_CONNECTED_WIFI);
            context.startService(iptServiceIntent);
        } else {
            checkConnectedWifiSSID(context);

//         // check if connected WIFI network is IMAX IPT network
//         //    if it is, try to connect to IPT server
//         //    otherwise, check to see if IMAX WIFI is configured, then attempt connecting to it
//         
//         WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//         WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//         
//         if (wifiInfo.getSSID() != null)
//            Log.w(TAG, "Connected to SSID: " + wifiInfo.getSSID());
//         else
//            Log.w(TAG, "Not connected to any active WIFI access point");
//         
//         if (wifiInfo != null && wifiInfo.getSSID().equals(IPTService.NETWORK_SSID))
//         {
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_CONNECTION_CONNECTED_WIFI);
//            context.startService(iptServiceIntent);    
//         }
//         else
//         {
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_CONNECTION_NOT_CONNECTED_WIFI);
//            context.startService(iptServiceIntent);  
//         }
        }

        if (netInfo != null) {
            Log.d(TAG, "WIFI ->" + netInfo.toString());
        }
        Log.d(TAG, "WIFI -> NO_CONNECTIVITY: " + intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY));

//      if(netInfo==null)
//      {
//         Log.d(TAG, "Client is disconnect , Wireless is not setup ");
////         WifiActivity.fire(context);
////         WelcomeActivity.fire(context);
//         
//         // TODO: send an intent to open Welcome activity
//         Intent iptServerConnectionLostIntent = new Intent(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
//         LocalBroadcastManager.getInstance(context).sendBroadcast(iptServerConnectionLostIntent);
//         
//         Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_BATTERY_LOW);
//         context.startService(iptServiceIntent);
//         
//         return;
//      }
//      if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
//      {
//         IPTService.fire(context);
//         Log.d(TAG, "Client Connected");
//      }
//      else
//      {
//         IPTService.stop(context);
//         Log.d(TAG, "Client is disconnect");
//      }
    }

    public static void checkConnectedWifiSSID(Context context) {
        // check if connected WIFI network is IMAX IPT network
        //    if it is, try to connect to IPT server
        //    otherwise, check to see if IMAX WIFI is configured, then attempt connecting to it

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getSSID() != null) {
            Log.w(TAG, "Connected to SSID: " + wifiInfo.getSSID());
        } else {
            Log.w(TAG, "Not connected to any active WIFI access point");
        }

        if (wifiInfo != null && wifiInfo.getSSID().contains(ConnecteService.NETWORK_SSID)) {
//        if (wifiInfo != null && wifiInfo.getSSID().equals(ConnecteService.NETWORK_SSID)) {
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_CONNECTION_CONNECTED_WIFI);
            Intent iptServiceIntent = new Intent(context, ConnecteService.class).
                    setAction(ConnecteService.ACTION_CONNECTION_CONNECTED_WIFI);
            context.startService(iptServiceIntent);
        } else {
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_CONNECTION_NOT_CONNECTED_WIFI);
//            Intent iptServiceIntent = new Intent(context, ConnecteService.class)
//                    .setAction(ConnecteService.ACTION_CONNECTION_NOT_CONNECTED_WIFI);
            Intent iptServiceIntent = new Intent(context, ConnecteService.class)
                    .setAction(ConnecteService.ACTION_CONNECTION_NOT_CONNECTED_IPT_SERVER);
            context.startService(iptServiceIntent);
        }
    }
};