package com.imax.ipt.conector;

import android.util.Log;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.GlobalController;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Rodrigo Lopez
 */
public class Client extends Thread {
    private static final String TAG = Client.class.getSimpleName();
    private Socket socket;
    public static final String host = Constants.CONTROL_PC_HOST;
    private int port = 11740;

    //   private char delimiter = '\0';
    private char delimiter = '\n';
    private String charset = "UTF-8";

    private ClientCallback clientCallback;
    private BufferedOutputStream bis;
    private BufferedReader in;

    /**
     * @param callback
     */
    public Client(ClientCallback callback) {
        this.clientCallback = callback;

    }

    public void connect() {
        Log.d(TAG, "Client - connect");
        start();
    }


    public synchronized void sendRequest(String request) {
        Log.d(TAG, "post-request: msg=" + request);
        if (bis == null) {
            // throwing an exception to the service will potentially cause more than 1 Client created
            //clientCallback.exceptionCaught(new IOException("socket is broken"));
            Log.w(TAG, "socket is broken");
            return;
        }
        byte[] buffer;
        try {
            buffer = (request + delimiter).getBytes(charset);
            Log.d(TAG, "request=" + (request + delimiter));
            bis.write(buffer);
            bis.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            Log.d(TAG, "Client - sendRequest - UnsupportedEncodingExcetpion");
        } catch (IOException e) {
            e.printStackTrace();

            Log.d(TAG, "Client - sendRequest - IOExcetpion");
        }

    }

    /**
     *
     */
    @Override
    public void run() {
        Log.d(TAG, "Client - socket Started");
        Boolean serverConnected = false;

        try {

            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 1000);
            socket.setKeepAlive(true); // todo need optimize the exception handle.

//            socket.setSoTimeout(25000);//set read stream timeout 25s


            if (!socket.isConnected()) {
                throw new IOException("There is not a valid connection with backend");
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));

            bis = new BufferedOutputStream(socket.getOutputStream());
            clientCallback.isConnected();

            serverConnected = true;
            GlobalController.getInstance().serverConnected();

            while (true) {
                StringBuilder sb = new StringBuilder();
                int value = 0;
                // reads to the end of the stream
                while ((value = in.read()) != -1) {
                    char c = (char) value;

                    if (c == delimiter) {
                        break;
                    }
                    sb.append(c);
                }

                Log.d(TAG, "response=" + sb.toString());
                clientCallback.messageReceived(sb.toString());
            }
        } catch (IOException e) {
            Log.d(TAG, "Client - run - IOException");
            Log.e(TAG, e.toString());
            e.printStackTrace();
            if (serverConnected) {
                GlobalController.getInstance().serverDisconnected();
            }

            // can cause re-entrance, not good
            //TODO NEED TO OPTIMIZE

            clientCallback.exceptionCaught(e);
        } catch (Exception e) {
            Log.d(TAG, "Client - run - Exception");
            Log.e(TAG, e.toString(), e);
            e.printStackTrace();
            if (serverConnected) {
                GlobalController.getInstance().serverDisconnected();
            }
            // can cause re-entrance, not good
            clientCallback.exceptionCaught(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (bis != null) {
                    bis.close();
                }
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            Log.d(TAG, "----------------------- END OF Client.run() -------------------");
        }
    }

    /**
     *
     */
    public void cancel() {
        Log.d(TAG, "cll Client - cancel called" + socket);
        try {
            if (socket != null) {
                if (in != null) {
                    in.close();
                }
                if (bis != null) {
                    bis.close();
                }
                socket.close();
            }
            Log.d(TAG, "Client - cancel end");
        } catch (IOException e) {
            Log.d(TAG, "Client - cancel - IOException");
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
