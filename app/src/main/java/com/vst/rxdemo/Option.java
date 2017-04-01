package com.vst.rxdemo;

import android.util.Log;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by user on 2017/3/30.
 */

public class Option {
    private Subscription subscribe;
    private LifecycleProvider mProvider;

    private ICallBack mICallBack;

    public Option(LifecycleProvider mProvider, ICallBack mICallBack) {
        this.mProvider = mProvider;
        this.mICallBack = mICallBack;
    }

    public void toDo() {
        subscribe = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(580);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(mProvider.<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer aLong) {
                        Log.i("Option", "" + aLong);
                        mICallBack.toDoCallBack(aLong);
                    }
                });
    }

    public void ondestory() {
        subscribe.unsubscribe();
    }
}
