//package com.imax.ipt.service;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.util.Log;
//import android.util.SparseArray;
//import com.google.gson.Gson;
//import com.imax.ipt.IPT;
//import com.imax.ipt.conector.Client;
//import com.imax.ipt.conector.ClientCallback;
//import com.imax.ipt.conector.api.Request;
//import com.imax.ipt.conector.api.Response;
//import com.imax.ipt.controller.eventbus.handler.input.GetActiveDeviceTypesHandler;
//import com.imax.ipt.controller.eventbus.handler.push.PushHandler;
//import com.imax.ipt.controller.eventbus.handler.push.PushNotificationEnum;
//import com.imax.ipt.model.SystemStatus;
//import com.imax.ipt.model.TabletStatus;
//import com.imax.iptevent.EventBus;
//
///**
// * Created by yanli on 2016/12/2.
// */
//public class WelcomeBindService extends Service implements ClientCallback {
//    private static final String TAG ="WelcomeBindService" ;
//    public static final String BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER = "com.imax.ipt.service.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mEventBus = IPT.getInstance().getEventBus();
//        mEventBus.register(this);
//        gson = new Gson();
//        Log.d(TAG, "cll onCreate service" );
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        //todo start to connect
//        Log.d(TAG, "cll onBind service" );
//        this.connect();
//
//        return myBinder;
//    }
//
//
//    @Override
//    public void messageReceived(String message) {
//        Log.d(TAG, "messageReceived msg=" + message);
//        Response response = gson.fromJson(message, Response.class);
//        Request request = null;
//        {
//            Log.d(TAG, "Push Notification ::" + message);
//            if (response.getMethod() != null) {
//                Class<? extends PushHandler> clazzPushHandler = null;
//
//                PushNotificationEnum pushNotificationEnum = null;
//                try {
//                    pushNotificationEnum = PushNotificationEnum.valueOf(response.getMethod());
//                } catch (IllegalArgumentException e) {
//                    // push notification is not registered
//                    Log.w(TAG, "Push notification is not recognized: " + response.getMethod());
//                    return;
//                }
//
////                if ((clazzPushHandler = pushHandlers.get(pushNotificationEnum)) != null) {
////                    PushHandler handler;
////                    try {
////                        handler = clazzPushHandler.newInstance();
////                        handler.execute(mEventBus, message);
////                    } catch (InstantiationException e) {
////                        Log.e(TAG, e.getMessage(), e);
////                    } catch (IllegalAccessException e) {
////                        Log.e(TAG, e.getMessage(), e);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(Throwable cause) {
//        Log.e(TAG, "cll exceptionCaught=" + cause.getMessage(), cause);
//        tabletStatus.setIptServerNotConnected(true);
////        refreshStatusNotification();
////        Intent iptServerConnectionLostIntent = new Intent(BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
////        LocalBroadcastManager.getInstance(this).sendBroadcast(iptServerConnectionLostIntent);
//
//
//
//
//    }
//
//    @Override
//    public void isConnected() {
//        this.mEventBus.post(new GetActiveDeviceTypesHandler().getRequest());
//
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        Log.d(TAG, "cll onUnbind service" );
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onDestroy() {
//        //todo
//        Log.d(TAG, "cll onDestroy service" );
//        mEventBus.unregister(this);
//
//        super.onDestroy();
//    }
//    public class MyBinder extends Binder {
//        public WelcomeBindService getService(){
//            return WelcomeBindService.this;
//        }
//    }
//
//    private MyBinder myBinder = new MyBinder();
//
//    private Client mClient;
//    private EventBus mEventBus;
//    private SparseArray<Request> sparseArray = new SparseArray<Request>();
//    private Gson gson;
//    private BroadcastReceiver mScreenReceiver;
//    private BatteryReceiver batteryReceiver;
//    private PingBroadcastReceiver pingBroadcastReceiver;
//    private HomePremiereReceiver mHomePremiereReceiver;
//    private SystemStatus systemStatus = new SystemStatus();
//    private static TabletStatus tabletStatus = new TabletStatus();
//
//    private void connect() {
//        /*add to distory bad connection */
//        if (mClient != null) {
//            mClient.cancel();
//            mClient = null;
//        }
//        mClient = new Client(this);
//        mClient.connect();
//    }
//
//}
