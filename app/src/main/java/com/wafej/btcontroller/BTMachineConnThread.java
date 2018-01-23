package com.wafej.btcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by wafej on 2018/1/23.
 */

public class BTMachineConnThread extends Thread {
    private Handler mHandler = null;
    private BluetoothAdapter mAdapter = null;
    private BluetoothSocket mSocket = null;
    private BluetoothServerSocket mServerSocket = null;

    public BTMachineConnThread(Handler handler) {
        this.mHandler = handler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void run() {
        try {
            mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("server", BTUtils.PRIVATE_UUID);
            Log.i("ssss","acceptttttttttt");
            mSocket = mServerSocket.accept();
            Log.i("ssss","accepted");
        } catch (Exception e) {
            mHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();
            e.printStackTrace();
            return;
        } finally {
            try {
                mServerSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSocket != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = BTUtils.MESSAGE_CONNECT_SUCCESS;
            msg.obj = mSocket;
            msg.sendToTarget();
        } else {
            mHandler.obtainMessage(BTUtils.MESSAGE_CONNECT_ERROR).sendToTarget();
            return;
        }
    }
}
