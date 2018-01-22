package com.wafej.btcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wafej on 2018/1/22.
 */

public class ControllerActivity extends Activity implements View.OnClickListener{
    private Button mBtnOpenBT = null;
    private Button mBtnSearchBT = null;
    private Button mBtnUp = null;
    private Button mBtnDown = null;
    private Button mBtnLeft = null;
    private Button mBtnRight = null;
    private TextView mTVStatusBack = null;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_bt_controller:
                openBT();
                break;
            case R.id.btn_search_bt_controller:

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
}
