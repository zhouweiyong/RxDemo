package com.vst.rxjava20;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * Created by zwy on 2017/9/18.
 * email:16681805@qq.com
 * 组合操作，处理多个Observable情况
 */

public class CombinationActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"amb", "concat", "zip"};

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
                        amb();
                        break;
                    case 1:
                        concat();
                        break;
                    case 2:
                        zip();
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


    /**
     * 传递多个Observable给Amb时，它只发射其中一个Observable的数据和通知：最先发送通知给Amb的那个，
     * 不管发射的是一项数据还是一个onError或onCompleted通知。Amb将忽略和丢弃其它所有Observables的发射物。
     */
    private void amb() {
        List<Observable<String>> list = new ArrayList<>();
        list.add(Observable.just("hello1"));
        list.add(Observable.just("hello2"));
        list.add(Observable.just("hello3"));

//        Observable.amb(list)
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        L.i("accept>>>" + s);
//                    }
//                });

        Observable.ambArray(Observable.just("hello3").delay(2, TimeUnit.SECONDS), Observable.just("hello1").delay(2, TimeUnit.SECONDS), Observable.just("hello6").delay(2, TimeUnit.SECONDS))
                .ambWith(Observable.just("hell7", "hello8"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("accept>>>" + s);


                    }
                });

    }


    /**
     * 顺序合并，merge是交叉合并
     * Concat操作符连接多个Observable的输出，就好像它们是一个Observable，
     * 第一个Observable发射的所有数据在第二个Observable发射的任何数据前面，以此类推。
     */
    private void concat() {
        List<Observable<String>> list = new ArrayList<>();
        list.add(Observable.just("hello1", "hello6"));
        list.add(Observable.just("hello2", "hello5"));
        list.add(Observable.just("hello3"));
//        Observable.concat(list)
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        L.i("accept>>>" + s);
//                    }
//                });

        Observable.concat(Observable.just("hello1", "hello6"), Observable.just("hello2", "hello5"), Observable.just("hello3"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("accept>>>" + s);
                    }
                });
    }


    /**
     * Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
     * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
     */
    private void zip() {
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
