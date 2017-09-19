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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zwy on 2017/9/15.
 * email:16681805@qq.com
 * 错误处理操作
 */

public class RxErrorActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"主动抛出错误", "抛出异常", "onErrorReturn", "onErrorResumeNext"
            , "onExceptionResumeNext", "onErrorReturnItem", "retry"};

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
                        demo1();
                        break;
                    case 1:
                        demo2();
                        break;
                    case 2:
                        onErrorReturn();
                        break;
                    case 3:
                        onErrorResumeNext();
                        break;
                    case 4:
                        onExceptionResumeNext();
                        break;
                    case 5:
                        onErrorReturnItem();
                        break;
                    case 6:
                        retry();
                        break;
                    case 7:
                        break;

                }
            }
        });
    }

    private void demo1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 1; i < 100; i++) {
                    if (i % 10 == 0) {
                        e.onError(new Throwable("你错了!!!!!!"));
                    } else {
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                L.i("onNext>>>" + integer);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError>>>" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void demo2() {
        Observable.range(1, 100)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
                        if (integer >= 20)
                            return Observable.error(new Throwable("你错了！！！"));
                        return Observable.just(integer);
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                L.i("onNext>>>" + integer);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError>>>" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * catch操作符拦截原Observable的onError通知，将它替换为其它的数据项或数据序列，让产生的Observable能够正常终止或者根本不终止。
     * 在RxJava中，catch实现为三个不同的操作符：
     * onErrorReturn:让Observable遇到错误时发射一个特殊的项并且正常终止。
     * onErrorResumeNext:让Observable在遇到错误时开始发射第二个Observable的数据序列。
     * onExceptionResumeNext:让Observable在遇到错误时继续发射后面的数据项。
     */
    public void onErrorReturn() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext("item" + i);
                    if (i == 6)
                        e.onError(new Throwable());
                }
                e.onComplete();
            }
        }).onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(@NonNull Throwable throwable) throws Exception {
                return "这是错误的内容";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                L.i("onErrorReturn>>>" + s);
            }
        });
    }

    /**
     * onErrorResumeNext:让Observable在遇到错误时开始发射第二个Observable的数据序列。
     * onErrorResumeNext方法返回一个镜像原有Observable行为的新Observable(或者一个备用的Observable)，后者会忽略前者的onError调用，
     * 不会将错误传递给观察者，作为替代，它会开始镜像另一个，备用的Observable。
     */
    public void onErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext("item" + i);
                    if (i == 6)
                        e.onError(new Throwable());
                }
                e.onComplete();
            }
        }).onErrorResumeNext(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello world1");
                e.onNext("hello world2");
                e.onComplete();
            }
        })).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                L.i("onErrorResumeNext>>>" + s);
            }
        });
    }

    /**
     * onExceptionResumeNext方法返回一个镜像原有Observable行为的新Observable，也使用一个备用的Observable，
     * 不同的是，如果onError收到的Throwable不是一个Exception，它会将错误传递给观察者的onError方法，不会使用备用的Observable。
     */
    public void onExceptionResumeNext() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext("item" + i);
                    if (i == 6)
                        e.onError(new Exception());
                }
                e.onComplete();
            }
        }).onExceptionResumeNext(new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                observer.onNext("kkkk");
                observer.onNext("tttt");
                observer.onComplete();
            }
        })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        L.i("onExceptionResumeNext>>>" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("Throwable>>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 遇到错误时发送指定项
     */
    public void onErrorReturnItem() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext("item" + i);
                    if (i == 6)
                        e.onError(new Throwable());
                }
                e.onComplete();
            }
        }).onErrorReturnItem("you are wrong")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("onErrorReturnItem>>>" + s);
                    }
                });
    }

    /**
     * 遇到错误时，重新开始
     */
    private void retry() {
        //重试无限次
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                for (int i = 0; i < 10; i++) {
//                    e.onNext("item" + i);
//                    if (i == 6)
//                        e.onError(new Throwable());
//                }
//                e.onComplete();
//            }
//        }).retry().subscribe(new Consumer<String>() {
//            @Override
//            public void accept(@NonNull String s) throws Exception {
//                L.i("retry>>>" + s);
//            }
//        });

        //指定重试的次数
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                for (int i = 0; i < 10; i++) {
//                    e.onNext("item" + i);
//                    if (i == 6)
//                        e.onError(new Throwable("wrong!!!!!"));
//                }
//                e.onComplete();
//            }
//        }).retry(2).subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                L.i("retry>>>" + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                L.i("onError>>>" + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        //根据条件判断是否重试
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    e.onNext("item" + i);
                    if (i == 6)
                        e.onError(new Throwable("wrong!!!!!"));
                }
                e.onComplete();
            }
        }).retry(new BiPredicate<Integer, Throwable>() {
            @Override
            public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Exception {
                L.i("test>>>" + integer);
                return integer < 2;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                L.i("retry>>>" + s);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError>>>" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
