package com.vst.rxjava20;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by zwy on 2017/9/19.
 * email:16681805@qq.com
 */

public class RxLifeActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"doo"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        lv_main = (ListView) findViewById(R.id.lv_main);
        lv_main.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, Arrays.asList(items)));
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        doo();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;

                }
            }
        });
    }


    private Disposable mDisposable;

    private void doo() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext(i);
                    if (i == 6)
                        e.onError(new Throwable("wrong!!"));
                }
                e.onComplete();
            }
        })
                .doOnEach(new Observer<Integer>() {//注册一个动作，对Observable发射的每个数据项使用
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        L.i("doOnEach>>>onNext>>>" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {//注册一个动作，在观察者订阅时使用
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        L.i("@@@doOnSubscribe");
                    }
                })
                .doOnDispose(new Action() {//注册一个动作，在观察者取消订阅时使用
                    @Override
                    public void run() throws Exception {
                        L.i("@@@doOnDispose");
                    }
                })
                .doOnLifecycle(new Consumer<Disposable>() {//可以在订阅之后 设置是否取消订阅
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        L.i("@@@doOnLifecycle>>>accept");
//                        if (!disposable.isDisposed())
//                            disposable.dispose();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        L.i("@@@doOnLifecycle>>>run");
                    }
                })
                .doOnNext(new Consumer<Integer>() {//在onNext前执行
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("@@@doOnNext");
                    }
                })
                .doAfterNext(new Consumer<Integer>() {//	在onNext之后执行
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("@@@doAfterNext");
                    }
                })
                .doOnComplete(new Action() {//	注册一个动作，对正常完成的Observable使用
                    @Override
                    public void run() throws Exception {
                        L.i("@@@doOnComplete");
                    }
                })
                .doOnError(new Consumer<Throwable>() {//注册一个动作，对发生错误的Observable使用
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        L.i("@@@doOnError");
                    }
                })
                .doAfterTerminate(new Action() {//调用onComplete或者onError终止发送时候调用
                    @Override
                    public void run() throws Exception {
                        L.i("@@@doAfterTerminate");
                    }
                })
                .doFinally(new Action() {//在最后一定执行
                    @Override
                    public void run() throws Exception {
                        L.i("@@@doFinally");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.i("subscribe>>>onSubscribe");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        L.i("subscribe>>>onNext>>>" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("subscribe>>>onError>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        L.i("subscribe>>>onComplete");
                    }
                });
    }
}
