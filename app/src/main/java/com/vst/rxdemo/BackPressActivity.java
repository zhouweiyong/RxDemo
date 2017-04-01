package com.vst.rxdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle.components.RxActivity;

/**
 * Created by user on 2017/3/30.
 */

public class BackPressActivity extends RxActivity implements View.OnClickListener ,ICallBack{

    private Button btn_abp;
    private Option option;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_press);
        initView();
    }

    private void initView() {
        btn_abp = (Button) findViewById(R.id.btn_abp);

        btn_abp.setOnClickListener(this);
        option = new Option(this,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_abp:
                option.toDo();
                break;
        }
    }


    @Override
    protected void onStop() {
        Log.i("state", "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("state", "onPause");
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("state", "onDestroy");
    }

    @Override
    public void toDoCallBack(int i) {
        Log.i("BackPressActivity",""+i);
    }
}
