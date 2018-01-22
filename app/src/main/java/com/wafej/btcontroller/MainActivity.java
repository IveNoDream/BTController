package com.wafej.btcontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button mBtnMachine = null;
    private Button mBtnController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnMachine = (Button) findViewById(R.id.btn_machine);
        mBtnController = (Button) findViewById(R.id.btn_controller);

        mBtnMachine.setOnClickListener(this);
        mBtnController.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_machine:
                Intent intentMachine = new Intent(MainActivity.this, MachineActivity.class);
                intentMachine.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentMachine);
                finish();
                break;
            case R.id.btn_controller:
                Intent intentContoller = new Intent(MainActivity.this, ControllerActivity.class);
                intentContoller.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentContoller);
                finish();
                break;
            default:
                break;
        }
    }
}
