package com.vst.rxdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by user on 2017/3/30.
 */

public class ThreadActivity extends RxActivity implements View.OnClickListener {

    private Button btn_abp;
    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_press);
        initView();
    }

    private void initView() {
        btn_abp = (Button) findViewById(R.id.btn_abp);

        btn_abp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_abp:
                mSubscription = Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
//                        if (!subscriber.isUnsubscribed()) {
                        int t = 0;
                        for (int i = 0; i < 100000000 * 100000000; i++) {
                            t += i;
                        }
                        subscriber.onNext(t);
                        subscriber.onCompleted();
//                        }
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.i("zwy", "" + integer);
                            }
                        });
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
}
