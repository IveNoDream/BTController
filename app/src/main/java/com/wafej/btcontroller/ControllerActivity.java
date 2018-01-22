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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ListView mLVDevices = null;
    private Set<BluetoothDevice> mNearbyDevices = new HashSet<BluetoothDevice>();
    private ArrayList<HashMap<String, Object>> mListItem = null;
    private Set<BluetoothDevice> mPairedDevices = null;
    private BTDevicesAdapter mAdapter = null;
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
        mLVDevices = (ListView) findViewById(R.id.lv_bt_devices);

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
                        getString(R.string.start_search_bt).toString(),
                        Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                int n = mNearbyDevices.size();
                if (n <= 0) {
                    Toast.makeText(ControllerActivity.this,
                            getString(R.string.no_bt_devices).toString(),
                            Toast.LENGTH_SHORT).show();
                    //show waitting progress
                    return;
                } else {
                    Toast.makeText(ControllerActivity.this,
                            getString(R.string.search_finished).toString(),
                            Toast.LENGTH_LONG).show();
                }
                //updateListView();

                return;
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!mNearbyDevices.contains(device)) {
                    mNearbyDevices.add(device);
                    Log.i(TAG,"device name:" + device.getName()+" addr:" + device.getAddress().toString());
                    updateListView();
                } else {
                }
            } else {
                //faild
            }
        }
    };

    private void updateListView() {
        if (null == mAdapter) {
            mAdapter = new BTDevicesAdapter(this);
        }
        mLVDevices.setAdapter(mAdapter);
    }

    private class BTDevicesAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public BTDevicesAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return getData().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = mInflater.inflate(R.layout.list_bt_devices, null);
                holder.text = (TextView) view.findViewById(R.id.tv_bt_info);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final String string = getData().get(i)
                    .get("ItemText" + i).toString();
            holder.text.setText(string);

            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //connect
                    Log.i(TAG,"onclick");
                }
            });
            return view;
        }

    }

    public final class ViewHolder {
        public TextView text;
    }

    private ArrayList<HashMap<String, Object>> getData() {
        mListItem = new ArrayList<HashMap<String, Object>>();

        String[] messageNearby = namesFor(mNearbyDevices);
        for (int i = 0; i < messageNearby.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText" + i, messageNearby[i]);
            mListItem.add(map);
        }

        return mListItem;
    }

    private String[] namesFor(Set<BluetoothDevice> devices) {
        SparseArray<String> bondStates = new SparseArray<String>();
        bondStates.put(BluetoothDevice.BOND_NONE, "None");
        bondStates.put(BluetoothDevice.BOND_BONDING, "Bonding");
        bondStates.put(BluetoothDevice.BOND_BONDED, "Bonded");

        int n = devices.size();
        String[] names = new String[n];
        int i = 0;
        for (BluetoothDevice device : devices) {
            int state = device.getBondState();
            names[i++] = String.format("%d %s %s %s", i, device.getName(),
                    bondStates.get(state), device.getAddress());
        }

        return names;
    }
}
