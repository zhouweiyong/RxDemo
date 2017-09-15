package com.vst.rxjava20;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2017/3/31.
 */

public class RxDemo1Activity extends Activity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo1);
        initView();
        mCompositeDisposable = new CompositeDisposable();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                demo1();
                break;
            case R.id.btn2:
                demo2();
                break;
            case R.id.btn3:

                break;
            case R.id.btn4:
                demo4();
                break;
            case R.id.btn5:
                demo5();
                break;
            case R.id.btn6:

                break;
            case R.id.btn7:

                break;
            case R.id.btn8:

                break;
        }
    }

    //简单实例
    private void demo1() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onNext("shenzhen");
                e.onComplete();
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                L.i("onSubscribe");
            }


            @Override
            public void onNext(String s) {
                L.i("onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError");
            }

            @Override
            public void onComplete() {
                L.i("onComplete");
            }
        };
        observable.subscribe(observer);
    }

    /**
     * Disposable切断观察者和被观察者之间的联系
     */
    private void demo2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onNext("shenzhen");
                e.onComplete();
            }
        }).subscribe(new Observer<String>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                L.i("onSubscribe");
                mDisposable = d;
            }


            @Override
            public void onNext(String s) {
                L.i("onNext:" + s);
                if ("world".equals(s)) {
                    L.i("dispose");
                    mDisposable.dispose();
                    L.i("" + mDisposable.isDisposed());
                }

            }

            @Override
            public void onError(Throwable e) {
                L.i("onError");
            }

            @Override
            public void onComplete() {
                L.i("onComplete");
            }
        });
    }

    /**
     * Consumer表示观察者只关心onNext事件
     */
    private void demo3() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onNext("shenzhen");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                L.i("onNext:" + s);
            }

        });
    }

    /**
     * 线程切换
     * subscribeOn() 指定的是上游发送事件的线程
     * observeOn() 指定的是下游接收事件的线程.
     * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     * Schedulers.newThread() 代表一个常规的新线程
     * AndroidSchedulers.mainThread() 代表Android的主线程
     */
    private void demo4() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                L.i("Observable>>>" + Thread.currentThread().getName());
                e.onNext(2);
                e.onNext(6);
            }
        }).subscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                L.i("doOnSubscribe>>>" + Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                L.i("doOnNext>>>" + Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("subscribe>>>" + Thread.currentThread().getName());
                        L.i("" + integer);
                    }
                });

    }

    /**
     * 断开被观察者和观察者的连接
     */
    private void demo5() {
        mDisposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    e.onNext(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("onNext:" + integer);
                    }
                });
        mCompositeDisposable.add(mDisposable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDisposable.dispose();
        mCompositeDisposable.clear();
    }


    private void demo6(){

    }

}
