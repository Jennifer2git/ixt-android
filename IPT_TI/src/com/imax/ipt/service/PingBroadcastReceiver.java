package com.imax.ipt.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class PingBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = "PingBroadcastReceiver";

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    private static WifiLock wifiLock;
    private static WakeLock wakeLock;

    private Context context;

//   private EventBus mEventBus;

    public static void lock() {
        try {
            wakeLock.acquire();
            wifiLock.acquire();
        } catch (Exception e) {
            Log.e(TAG, "Error getting Lock: " + e.getMessage());
        }
    }

    public static void unlock() {
        if (wakeLock.isHeld())
            wakeLock.release();
        if (wifiLock.isHeld())
            wifiLock.release();
    }

    public PingBroadcastReceiver() {
    } // called by the AlarmManager

    // this constructor is invoked by WlanLockService (see next code snippet)
    public PingBroadcastReceiver(Context context) {

//     mEventBus = IPT.getInstance().getEventBus();
//     mEventBus.register(this);

        // initialise the locks
        wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "PingWifiLock");
        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PingWakeLock");

        this.context = context;
    }

    public void start(int timeoutInSeconds) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // use this class as the receiver
        Intent intent = new Intent(context, PingBroadcastReceiver.class);
        // create a PendingIntent that can be passed to the AlarmManager
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // create a repeating alarm, that goes of every x seconds
        // AlarmManager.ELAPSED_REALTIME_WAKEUP = wakes up the cpu only
//        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timeoutInSeconds*1000, pendingIntent);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeoutInSeconds * 1000, pendingIntent);
    }

    // stop the repeating alarm
    public void stop() {
//     mEventBus.unregister(this);

        // alarmMgr may be null if the program is loaded while the screen is OFF
        if (alarmMgr != null)
            alarmMgr.cancel(pendingIntent);
    }


    private Object syncPingObject = new Object();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Alarm received");

        // Call Ping in IPTService
//        IPTService.actionPing(context);
        ConnecteService.actionPing(context);


//      WifiManager connManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//      if(connManager.isWifiEnabled()) {
        lock(); // lock wakeLock and wifiLock, then scan.
        // unlock() is then called at the end of the onReceive function of WifiStateReciever

//          mEventBus = IPT.getInstance().getEventBus();
//          mEventBus.register(this);          

//          new Thread(new Runnable() {
//            
//            @Override
//            public void run()
//            {
//
//               
//                   // send the Ping message to the Server
//                   mEventBus.post(new PingHandler().getRequest());
//                
////                   // wait for the proper response
////                   synchronized (syncPingObject)
////                  {
////                      try
////                     {
////                         Log.d(TAG, "syncPingObject Wait...");
////                        syncPingObject.wait();
////                     } catch (InterruptedException e)
////                     {
////                        // TODO Auto-generated catch block
////                        e.printStackTrace();
////                     }
////                  }
//                   Log.d(TAG, "syncPingObject Wait DONE");
//                   
////                   try
////                  {
////                      Log.d(TAG, "onReceive Wait...");
////                     wait();
////                  } catch (InterruptedException e)
////                  {
////                     // TODO Auto-generated catch block
////                     e.printStackTrace();
////                  }
////                   Log.d(TAG, "onReceive Wait DONE");
//                   
//                   unlock();                   
////                   mEventBus.unregister(this);  
//            }
//         }).start();


//      mEventBus = IPT.getInstance().getEventBus();
//      mEventBus.register(this);
//      
//          // send the Ping message to the Server
//          mEventBus.post(new PingHandler().getRequest());
//       
//          // wait for the proper response
//          synchronized (syncPingObject)
//         {
//             try
//            {
//                Log.d(TAG, "syncPingObject Wait...");
//               syncPingObject.wait();
//            } catch (InterruptedException e)
//            {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//            }
//         }
//          Log.d(TAG, "syncPingObject Wait DONE");
//          
////          try
////         {
////             Log.d(TAG, "onReceive Wait...");
////            wait();
////         } catch (InterruptedException e)
////         {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////         }
////          Log.d(TAG, "onReceive Wait DONE");
//          
//          unlock();                   
//          mEventBus.unregister(this);                   

//      }
    }

//   public void onEvent(PingHandler pingHandler)
//   {
//      Log.d(TAG, "onEvent PingHandler received");
////      notifyAll();
//      synchronized (syncPingObject)
//      {
//         syncPingObject.notifyAll();
//      }
//      Log.d(TAG, "onEvent NotifyAl");
//   }
}
