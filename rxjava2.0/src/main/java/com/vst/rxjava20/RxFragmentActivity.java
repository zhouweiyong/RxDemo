package com.vst.rxjava20;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

/**
 * Created by zwy on 2017/4/12.
 * email:16681805@qq.com
 */

public class RxFragmentActivity extends Activity {


    private FrameLayout flyt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_fragment);
        initView();
    }

    private void initView() {
        flyt = (FrameLayout) findViewById(R.id.flyt);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flyt,new RxFragment());
        fragmentTransaction.commit();
    }
}
