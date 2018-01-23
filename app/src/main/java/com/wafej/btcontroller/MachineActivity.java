package com.wafej.btcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by wafej on 2018/1/22.
 */

public class MachineActivity extends Activity implements View.OnClickListener{

    private Button mBtnOpenBT = null;
    private Button mBtnVisibleBT = null;
    private TextView mTVStatus = null;
    private BTCommunThread mBTCommunThread = null;
    private static String CUR_STATUS = "NULL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        mBtnOpenBT = (Button) findViewById(R.id.btn_open_bt_machine);
        mBtnVisibleBT = (Button) findViewById(R.id.btn_discover_bt_machine);
        mTVStatus = (TextView) findViewById(R.id.tv_cur_status);

        mBtnOpenBT.setOnClickListener(this);
        mBtnVisibleBT.setOnClickListener(this);

        new BTMachineConnThread(mHandler).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_bt_machine:
                openBT();
                break;
            case R.id.btn_discover_bt_machine:
                discoverBlueTooth();
                break;
            default:
                break;
        }
    }

    private void openBT() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // can't support bt
        if (null == adapter) {
            return;
        }

        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(enableBtIntent);
        } else {
            Toast.makeText(MachineActivity.this,R.string.bt_already_enable,Toast.LENGTH_SHORT).show();
        }
    }


    public void discoverBlueTooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // can't support bt
        if (null == adapter) {
            return;
        }

        if (adapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            return;
        }

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BTUtils.MESSAGE_CONNECT_SUCCESS:
                    //start commun thread
                    Log.i("ssss","connect_success");
                    mBTCommunThread = new BTCommunThread(mHandler,(BluetoothSocket) msg.obj);
                    mBTCommunThread.start();

                    new BackToControllerThread().start();
                    break;

                case BTUtils.MESSAGE_CONNECT_ERROR:
                    //error
                    Log.i("ssss","connect_failed");
                    break;
                case BTUtils.MESSAGE_READ_OBJECT:
                    //rev msg from controller
                    Log.i("ssss","rev_msg: " + msg.obj.toString());
                    CUR_STATUS = msg.obj.toString();
                    mTVStatus.setText(CUR_STATUS);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != this.mBTCommunThread) {
            this.mBTCommunThread.mIsRunning = false;
        }
    }

    private class BackToControllerThread extends Thread {
        @Override
        public void run() {
            while (mBTCommunThread.mIsRunning) {
                mBTCommunThread.writeObject(CUR_STATUS.toString() + "  " + getResources().getString(R.string.update_time)
                    + new Date(System.currentTimeMillis()));
                Log.i("ssss","CUR_STATUS: " + CUR_STATUS);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
    }
}
