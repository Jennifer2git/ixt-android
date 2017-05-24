package com.imax.ipt.conector;

public interface ClientCallback {

    /**
     * @param message
     */
    void messageReceived(String message);

    /**
     * @param cause
     */
    void exceptionCaught(Throwable cause);

    /**
     *
     */
    void isConnected();
}
