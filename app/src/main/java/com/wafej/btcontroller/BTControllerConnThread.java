package com.wafej.btcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by wafej on 2018/1/23.
 */

public class BTControllerConnThread extends Thread {
    private Handler mControllerHandler = null;
    private BluetoothSocket mSocket = null;
    private BluetoothDevice mControllerDev = null;

    public BTControllerConnThread(Handler handler,BluetoothDevice device) {
        this.mControllerHandler = handler;
        this.mControllerDev = device;
    }

    @Override
    public void run() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            Log.i("ssss","connect to" + mControllerDev.getAddress());
            //UUID id = mControllerDev.getUuids()[0].getUuid();
            mSocket = mControllerDev.createInsecureRfcommSocketToServiceRecord(BTUtils.PRIVATE_UUID);
            if (null == mSocket) {
                Log.i("ssss","socket null");
            }
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            mSocket.connect();
            Log.i("ssss","connect toooo" + mControllerDev.getAddress());
        } catch (Exception ex) {
            Log.e("ssss","ex: " + ex.toString());

            try {
                mSocket =(BluetoothSocket) mControllerDev.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mControllerDev,1);
                mSocket.connect();
                Log.i("ssss","Connectedddddddd");
            }catch (Exception e2) {
                Log.e("ssss", "fffffffffffffffff!" + e2.toString());
                try {
                    mSocket.close();
                } catch (IOException e) {
                    Log.i("ssss","e: " + ex.toString());
                    e.printStackTrace();
                }
                //connect failed
                mControllerHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();
                return;
            }

            /*try {
                mSocket.close();
            } catch (IOException e) {
                Log.i("ssss","e: " + ex.toString());
                e.printStackTrace();
            }
            //connect failed
            mControllerHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();*/
            //return;
        }

        Log.i("ssss","success");
        //connect success
        Message msg = mControllerHandler.obtainMessage();
        msg.what = BTUtils.MESSAGE_CONNECT_SUCCESS;
        msg.obj = mSocket;
        msg.sendToTarget();
    }
}
