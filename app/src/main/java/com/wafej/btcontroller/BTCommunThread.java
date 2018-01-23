package com.wafej.btcontroller;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BTCommunThread extends Thread {

    private Handler mServiceHandler;
    private BluetoothSocket mSocket;
    private ObjectInputStream mInStream;
    private ObjectOutputStream mOutStream;
    public volatile boolean mIsRunning = true;

    public BTCommunThread(Handler handler, BluetoothSocket socket) {
        this.mServiceHandler = handler;
        this.mSocket = socket;
        try {
            this.mOutStream = new ObjectOutputStream(socket.getOutputStream());
            this.mInStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //filed msg
            mServiceHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!mIsRunning) {
                break;
            }
            try {
                Object obj = mInStream.readObject();
                //success msg
                Message msg = mServiceHandler.obtainMessage();
                msg.what = BTUtils.MESSAGE_READ_OBJECT;
                msg.obj = obj;
                msg.sendToTarget();
            } catch (Exception ex) {
                mServiceHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();
                ex.printStackTrace();
                return;
            }
        }

        if (mInStream != null) {
            try {
                mInStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutStream != null) {
            try {
                mOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeObject(Object obj) {
        try {
            mOutStream.flush();
            mOutStream.writeObject(obj);
            mOutStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
