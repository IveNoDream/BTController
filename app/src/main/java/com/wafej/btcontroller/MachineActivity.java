package com.wafej.btcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wafej on 2018/1/22.
 */

public class MachineActivity extends Activity implements View.OnClickListener{

    Button mBtnOpenBT = null;
    Button mBtnVisibleBT = null;
    TextView mTVStatus = null;
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
                    break;

                case BTUtils.MESSAGE_CONNECT_ERROR:
                    //error
                    Log.i("ssss","connect_failed");
                    break;
            }
        }
    };
}
