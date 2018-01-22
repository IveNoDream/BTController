package com.wafej.btcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wafej on 2018/1/22.
 */

public class ControllerActivity extends Activity implements View.OnClickListener{
    private final static String TAG = "ssss";
    private Button mBtnOpenBT = null;
    private Button mBtnSearchBT = null;
    private Button mBtnUp = null;
    private Button mBtnDown = null;
    private Button mBtnLeft = null;
    private Button mBtnRight = null;
    private TextView mTVStatusBack = null;
    private Set<BluetoothDevice> nearbyDevices = new HashSet<BluetoothDevice>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);


        mBtnOpenBT = (Button) findViewById(R.id.btn_open_bt_controller);
        mBtnSearchBT = (Button) findViewById(R.id.btn_search_bt_controller);
        mBtnUp = (Button) findViewById(R.id.btn_up);
        mBtnDown = (Button) findViewById(R.id.btn_down);
        mBtnLeft = (Button) findViewById(R.id.btn_left);
        mBtnRight = (Button) findViewById(R.id.btn_right);
        mTVStatusBack = (TextView) findViewById(R.id.tv_status_back_show);

        mBtnOpenBT.setOnClickListener(this);
        mBtnSearchBT.setOnClickListener(this);
        mBtnUp.setOnClickListener(this);
        mBtnDown.setOnClickListener(this);
        mBtnLeft.setOnClickListener(this);
        mBtnDown.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilterDiscoveryStarted = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, intentFilterDiscoveryStarted);

        IntentFilter intentFilterDiscoveryFinished = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, intentFilterDiscoveryFinished);

        IntentFilter intentFilterFound = new IntentFilter(
                BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilterFound);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_bt_controller:
                openBT();
                break;
            case R.id.btn_search_bt_controller:
                searchNearby();
                break;
            case R.id.btn_up:

                break;
            case R.id.btn_down:

                break;
            case R.id.btn_left:

                break;
            case R.id.btn_right:

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
            Toast.makeText(ControllerActivity.this,R.string.bt_already_enable,Toast.LENGTH_SHORT).show();
        }
    }

    public void searchNearby() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // can't support bt
        if (null == adapter) {
            return;
        }

        if (!adapter.isEnabled()) {
            return;
        }

        if (!adapter.isDiscovering()) {
            adapter.startDiscovery();
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(ControllerActivity.this,
                        getString(R.string.no_bt_devices).toString(),
                        Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                int n = nearbyDevices.size();
                if (n <= 0) {
                    Toast.makeText(ControllerActivity.this,
                            getString(R.string.no_bt_devices).toString(),
                            Toast.LENGTH_LONG).show();
                    //show waitting progress
                    return;
                }
                //updateListView();

                return;
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!nearbyDevices.contains(device)) {
                    nearbyDevices.add(device);
                    Log.i(TAG,"device name:" + device.getName()+" addr:" + device.getAddress().toString());
                } else {
                }
            } else {
                //faild
            }
        }


    };
}
