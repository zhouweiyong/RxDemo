package com.vst.rxjava20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
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
 * Backpressure其实就是为了控制流量, 水缸存储的能力毕竟有限, 因此我们还得从源头去解决问题,
 * 既然你发那么快, 数据量那么大, 那我就想办法不让你发那么快呗
 */

public class RxBackPressActivity extends RxActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo1);
        initView();
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
                demo3();
                break;
            case R.id.btn4:
                mSubscription.request(10);
                break;
            case R.id.btn5:

                break;
            case R.id.btn6:

                break;
            case R.id.btn7:

                break;
            case R.id.btn8:

                break;
        }
    }

    /**
     * 模拟产生背压BUG
     */
    private void demo1() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                L.i("zip>>>" + Thread.currentThread().getName());
                return s + integer;
            }
        }).subscribeOn(Schedulers.newThread())
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
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

    /**
     * 模拟产生背压BUG
     */
    private void demo2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .compose(this.<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        try {
                            L.i("onNext:" + integer);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private Subscription mSubscription;
    private FlowableEmitter emitter;

    /**
     * 解决背压问题
     */
    private void demo3() {
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                emitter = e;
                for (int i = 0; i < 1000; i++) {
                    while (e.requested() == 0) {
                        if (e.isCancelled())
                            break;
                    }
                    e.onNext(i);
                    L.i("requested>>>>>>" + e.requested());
                }

                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread());

        FlowableSubscriber<Integer> subscriber = new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                L.i("onSubscribe");
                mSubscription = s;
                s.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                L.i("onNext:" + integer);
//                if (emitter.requested() == 1)
//                    mSubscription.request(3);
            }

            @Override
            public void onError(Throwable t) {
                L.i("onError");
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                L.i("onComplete");
            }
        };

        flowable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
