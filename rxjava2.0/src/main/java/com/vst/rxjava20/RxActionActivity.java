package com.vst.rxjava20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trello.rxlifecycle2.components.RxActivity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * Created by user on 2017/4/1.
 */

public class RxActionActivity extends RxActivity {

    private ListView lv_main;
    private String[] items = new String[]{"throttleFirst", "materialize", "timeInterval", "timeout", "timestamp"
            , "using", "to", "sorted", "toList", "toSortedList", "toMap"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo2);
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
                        throttleFirst();
                        break;
                    case 1:
                        materialize();
                        break;
                    case 2:
                        timeInterval();
                        break;
                    case 3:
                        timeout();
                        break;
                    case 4:
                        timestamp();
                        break;
                    case 5:
                        using();
                        break;
                    case 6:
                        to();
                        break;
                    case 7:
                        sorted();
                        break;
                    case 8:
                        toList();
                        break;
                    case 9:
                        toSortedList();
                        break;
                    case 10:
                        toMap();
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        break;
                    case 16:
                        break;
                    case 17:
                        break;
                    case 18:
                        break;
                    case 19:
                        break;
                    case 20:
                        break;
                    case 21:
                        break;
                    case 22:
                        break;
                    case 23:
                        break;
                    case 24:
                        break;
                    case 25:
                        break;
                }
            }
        });
    }


    /**
     * 学习throttleFirst操作符
     * 屏蔽指定时间内的信息
     */
    private void throttleFirst() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .throttleFirst(10, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        L.i("" + aLong);
                    }
                });
    }

    /**
     * 将Observable转换成一个通知列表
     * 一个合法的有限的Obversable将调用它的观察者的onNext方法零次或多次，然后调用观察者的onCompleted或onError正好一次。
     * Materialize操作符将这一系列调用，包括原来的onNext通知和终止通知onCompleted或onError都转换为一个Observable发射的数据序列。
     * <p>
     * dematerialize相反，把通知转成Observable
     */
    private void materialize() {
//        Observable.range(1, 10)
//                .materialize()
//                .subscribe(new Consumer<Notification<Integer>>() {
//                    @Override
//                    public void accept(@NonNull Notification<Integer> notification) throws Exception {
//                        if (notification.isOnNext()) {
//                            L.i("materialize>>>OnNext>>" + notification.getValue());
//                        } else if (notification.isOnComplete()) {
//                            L.i("materialize>>>OnComplete");
//                        } else if (notification.isOnError()) {
//                            L.i("materialize>>>OnError>>" + notification.getError());
//                        }
//
//                    }
//                });

        Observable.create(new ObservableOnSubscribe<Notification<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Notification<String>> e) throws Exception {
                e.onNext(Notification.createOnNext("Hello!"));
                e.onNext(Notification.createOnNext("Hello2"));
                e.onNext(Notification.<String>createOnComplete());
            }
        }).dematerialize()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        L.i("" + o);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 将一个Observable转换为发射两个数据之间所耗费时间的Observable
     */
    private void timeInterval() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    e.onNext("data" + i);
                    Thread.sleep(500);
                }
                e.onComplete();
            }
        })
                .timeInterval(TimeUnit.MILLISECONDS, Schedulers.newThread())
                .subscribe(new Consumer<Timed<String>>() {
                    @Override
                    public void accept(@NonNull Timed<String> timed) throws Exception {
                        L.i("timeInterval>>>" + timed.time(TimeUnit.MILLISECONDS) + "===value>>>" + timed.value());
                    }
                });
    }

    /**
     * 如果过了指定的一段时间没有发射数据，则发射错误通知
     */
    private void timeout() {
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("hello").delay(1, TimeUnit.SECONDS);
            }
        }).subscribeOn(Schedulers.io())
                .timeout(800, TimeUnit.MILLISECONDS)
                .onErrorReturnItem("you are wrong!!!")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        L.i("timeout>>" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("timeout>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 给Observable发射的每个数据项添加一个时间戳
     */
    private void timestamp() {
        Observable.range(1, 5)
                .timestamp()
                .subscribe(new Consumer<Timed<Integer>>() {
                    @Override
                    public void accept(@NonNull Timed<Integer> timed) throws Exception {
                        L.i("timestamp>>" + timed.time() + "----value>>>" + timed.value());
                    }
                });
    }

    /**
     * 创建一个与Observable相同生命周期内存在的一次性资源
     */
    private void using() {
        Observable.using(new Callable<String>() {
            @Override
            public String call() throws Exception {//创建资源
                L.i("Callable");
                return "helll";
            }
        }, new Function<String, ObservableSource<String>>() {//使用资源创建Observable
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                L.i("Function");
                return Observable.just(s);
            }
        }, new Consumer<String>() {//这是最后调用的，销毁资源
            @Override
            public void accept(@NonNull String s) throws Exception {
                L.i("Consumer");
                s = null;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                L.i("onNext>>" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                L.i("onComplete");
            }
        });

    }

    /**
     * 将Observable转换为其它的对象或数据结构
     */
    public void to() {
        String to = Observable.range(1, 10)
                .to(new Function<Observable<Integer>, String>() {
                    @Override
                    public String apply(@NonNull Observable<Integer> observable) throws Exception {
                        final String[] str = {""};
                        observable.buffer(2, TimeUnit.SECONDS)
                                .subscribe(new Consumer<List<Integer>>() {
                                    @Override
                                    public void accept(@NonNull List<Integer> list) throws Exception {
                                        StringBuilder sb = new StringBuilder();
                                        for (Integer integer : list) {
                                            sb.append(integer);
                                        }
                                        str[0] = sb.toString();
                                    }
                                });
                        return str[0];
                    }
                });
        L.i("to>>>" + to);
    }

    //排序
    public void sorted() {
        Observable.just(8, 5, 9, 2, 6, 3, 7, 1)
                .sorted()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("sorted>>>" + integer);
                    }
                });

    }

    // 收集原始Observable发射的所有数据到一个列表，然后返回这个列表
    public void toList() {
        Observable.just(8, 5, 9, 2, 6, 3, 7, 1)
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> list) throws Exception {
                        L.i("subscribe");
                        for (Integer integer : list) {
                            L.i(">>" + integer);

                        }
                    }
                });
    }

    //toSortedList( ) — 收集原始Observable发射的所有数据到一个有序列表，然后返回这个列表
    public void toSortedList() {
        Observable.just(8, 5, 9, 2, 6, 3, 7, 1)
                .toSortedList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> list) throws Exception {
                        L.i("subscribe");
                        for (Integer integer : list) {
                            L.i(">>" + integer);

                        }
                    }
                });
    }

    //将序列数据转换为一个Map，Map的key是根据一个函数计算的
    public void toMap() {
        Observable.just(8, 5, 9, 2, 6, 3, 7, 1)
                .toMap(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "k" + integer;
                    }
                }).subscribe(new Consumer<Map<String, Integer>>() {
            @Override
            public void accept(@NonNull Map<String, Integer> map) throws Exception {
                Set<Map.Entry<String, Integer>> entries = map.entrySet();
                Iterator<Map.Entry<String, Integer>> it = entries.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Integer> next = it.next();
                    L.i(next.getKey() + ">>>" + next.getValue());
                }
            }
        });
    }

}
