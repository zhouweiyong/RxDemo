package com.vst.rxjava20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle2.components.RxActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2017/4/1.
 */

public class RxZipActivity extends RxActivity implements View.OnClickListener {

    private Button btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo2);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                demo1();
                break;
        }
    }

    /**
     * Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
     * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
     */
    private void demo1() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
                e.onNext("D");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(6);
                e.onNext(36);
                e.onNext(12);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                L.i("zip>>>" + Thread.currentThread().getName());
                return s + integer;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("onNext:" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        L.i("onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        L.i("onComplete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        L.i("onDisposable");
                    }
                });
    }
}
