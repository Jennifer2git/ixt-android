package com.imax.ipt.ui.activity.media;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

public class PrimaHelp {

    public static final String PRIMA_LEFT = "left---\0";//6C 65 66 74 2D 2D 2D 00
    public static final String PRIMA_RIGHT = "right--\0";//72 69 67 68 74 2D 2D 00
    public static final String PRIMA_UP = "up-----\0";//75 70 2D 2D 2D 2D 2D 00

    public static final String PRIMA_DOWN = "down---\0";//64 6F 77 6E 2D 2D 2D 00

    public static final String PRIMA_OK = "enter--\0";//65 6E 74 65 72 2D 2D 00

    public static String sendTCP(String ip, int port, String cmd) {
        InetSocketAddress isa;
        Socket socket = null;
        BufferedWriter writer = null;
        InputStream in = null;
        String temp = "";
        try {
            isa = new InetSocketAddress(ip, port);//
            socket = new Socket();
            socket.connect(isa, 5000);
            socket.setSoTimeout(10000);

            socket.getOutputStream().write(cmd.getBytes());


//			writer = new BufferedWriter(new OutputStreamWriter(
//					socket.getOutputStream()));
            in = socket.getInputStream();
//
//			d(TAG, "发送TCP请求1");
//			if (socket.isConnected() && !socket.isOutputShutdown()) {
//
//				writer.write(cmd);
//				writer.flush();
//
//			}
            if (socket.isConnected() && !socket.isInputShutdown()) {

                StringBuffer sb = new StringBuffer();
                byte[] b = new byte[4096];
                int n = -1;
                while ((n = in.read(b)) != -1) {
                    sb.append(new String(b, 0, n));
                }

                temp = sb.toString().trim();

            }

            writer.close();
            in.close();
            socket.close();
            return temp;
        } catch (IOException e) {
            // 网络通信异常：显示网络通信失败，在右上角
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }


    static InetSocketAddress isa;
    static Socket socket = null;
    static BufferedWriter writer = null;
    static OutputStream out = null;


    public static boolean connect(String ip, int port) {

        if (socket != null && socket.isConnected() && !socket.isOutputShutdown()
                && out != null) {
            return true;
        }

        try {
            isa = new InetSocketAddress(ip, port);//
            socket = new Socket();
            socket.connect(isa, 5000);
            socket.setSoTimeout(10000);

            out = socket.getOutputStream();

            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("PrimaHelp", "connect exception");
            return false;
        }


    }

    public static boolean send(String cmd) {
        if (out != null && socket != null && socket.isConnected() && !socket.isOutputShutdown()) {
            try {
                out.write(cmd.getBytes());
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean disconnect() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
                out = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return true;
    }
}
