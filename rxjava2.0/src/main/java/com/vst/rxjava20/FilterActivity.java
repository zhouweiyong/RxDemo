package com.vst.rxjava20;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by zwy on 2017/9/18.
 * email:16681805@qq.com
 * 过滤操作
 */

public class FilterActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"debounce", "distinct", "elementAt"
            , "filter", "ofType", "first", "single", "last", "ignoreElements"
            , "sample", "skip", "skipLast", "skipUntil", "skipWhile", "take"
            , "takeLast"};

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
                        debounce();
                        break;
                    case 1:
                        distinct();
                        break;
                    case 2:
                        elementAt();
                        break;
                    case 3:
                        filter();
                        break;
                    case 4:
                        ofType();
                        break;
                    case 5:
                        first();
                        break;
                    case 6:
                        single();
                        break;
                    case 7:
                        last();
                        break;
                    case 8:
                        ignoreElements();
                        break;
                    case 9:
                        sample();
                        break;
                    case 10:
                        skip();
                        break;
                    case 11:
                        skipLast();
                        break;
                    case 12:
                        skipUntil();
                        break;
                    case 13:
                        skipWhile();
                        break;
                    case 14:
                        take();
                        break;
                    case 15:
                        takeLast();
                        break;
                }
            }
        });
    }

    /**
     * debounce设置数据的发送间隔，如果在间隔时间内没有新的数据产生，则发送当前数据
     * 如果在间隔时间内有新的数据产生，则不发送，直到间隔时间达到设置值才会发送
     * 如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted，那么通过debounce操作符也会把这个结果提交给订阅者。
     * 可以用于过滤产生过快的数据
     */
    public void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    Random random = new Random(System.currentTimeMillis());
                    int t = (random.nextInt(5) + 1) * 1000;
                    L.i("time===" + t);
                    Thread.sleep(t);
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).debounce(2, TimeUnit.SECONDS)//两秒内没有新数据产生，则发送当前数据，如果有，则不发
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("rs===" + integer);
                    }
                });
    }

    /**
     * distinct操作符对源Observable产生的结果进行过滤，把重复的结果过滤掉，只输出不重复的结果给订阅者
     */
    public void distinct() {
        Observable.just(1, 2, 3, 1, 1, 6, 1, 8)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("distinct>>" + integer);
                    }
                });
    }

    /**
     * elementAt操作符在源Observable产生的结果中，仅仅把指定索引的结果提交给订阅者，索引是从0开始的。
     */
    public void elementAt() {
        Observable.just("h1", "h2", "h3", "h4", "h4")
                .elementAt(3)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("elementAt>>" + s);
                    }
                });
    }

    /**
     * filter操作符是对源Observable产生的结果按照指定条件进行过滤，只有满足条件的结果才会提交给订阅者
     */
    public void filter() {
        Observable.range(1, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                L.i("filter>>>" + integer);
            }
        });
    }

    /**
     * ofType操作符类似于filter操作符，区别在于ofType操作符是按照类型对结果进行过滤
     */
    public void ofType() {
        Observable.just("hell1", 100, 1.0f, true, 2l, 2f)
                .ofType(Float.class)//只发送Float类型的数据
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(@NonNull Float aFloat) throws Exception {
                        L.i("ofType>>>" + aFloat);
                    }
                });
    }

    /**
     * first操作符是把源Observable产生的结果的第一个提交给订阅者，first操作符可以使用elementAt(0)和take(1)替代。
     */
    public void first() {
        Observable.just(102, "hell1", 100, 1.0f, true, 2l, 2f)
                .first(DEFAULT_KEYS_DIALER)
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(@NonNull Serializable serializable) throws Exception {
                        L.i("first>>>" + serializable);
                    }
                });
    }

    /**
     * single操作符是对源Observable的结果进行判断，如果满足条件的结果的数量大于1，则抛出异常，否则把满足条件的结果提交给订阅者
     * 如果没有满足条件的结果，则发送设置的默认值
     */
    public void single() {
        Observable.range(1, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 8;
                    }
                })
                .single(6)//这里的默认值设置为6
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("single>>>" + integer);
                    }
                });
    }

    /**
     * last操作符把源Observable产生的结果的最后一个提交给订阅者，last操作符可以使用takeLast(1)替代。
     */
    public void last() {
        Observable.range(1, 12)
                .last(DEFAULT_KEYS_DIALER)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("last>>>" + integer);
                    }
                });
    }

    /**
     * ignoreElements操作符忽略所有源Observable产生的结果，只把Observable的onCompleted和onError事件通知给订阅者。
     * ignoreElements操作符适用于不太关心Observable产生的结果，只是在Observable结束时(onCompleted)或者出现错误时能够收到通知。
     */
    public void ignoreElements() {
        Observable.range(1, 16)
                .ignoreElements()
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        L.i("ignoreElements>>>onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        L.i("ignoreElements>>>onError");
                    }
                });
    }

    /**
     * sample操作符定期扫描源Observable产生的结果，在指定的时间间隔范围内对源Observable产生的结果进行采样
     * 它将在一个指定的时间间隔里由Observable发射最近一次的数值
     */
    public void sample() {
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        L.i("map>>>" + aLong);
                        return aLong;
                    }
                })
                .sample(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        L.i("sample>>>" + aLong);
                    }
                });
    }

    /**
     * skip操作符针对源Observable产生的结果，跳过前面n个不进行处理，而把后面的结果提交给订阅者处理
     * 也可以设置跳过指定时间
     */
    public void skip() {
        Observable.range(1, 10)
                .skip(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("skip>>>" + integer);
                    }
                });
    }

    /**
     * skipLast操作符针对源Observable产生的结果，忽略Observable最后产生的n个结果，而把前面产生的结果提交给订阅者处理
     * 值得注意的是，skipLast操作符提交满足条件的结果给订阅者是存在延迟效果的
     */
    public void skipLast() {
        Observable.range(1, 16)
                .skipLast(5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("skipLast>>>" + integer);
                    }
                });
    }

    /**
     * SkipUntil订阅原始的Observable，但是忽略它的发射物，直到第二个Observable发射了一项数据那一刻，它开始发射原始Observable。
     * 即：第二个Observable发射数据后，第一个Observable才开始发射数据，之前的数据都过滤掉
     */
    public void skipUntil() {
        Observable.interval(1, TimeUnit.SECONDS)
                .skipUntil(Observable.just(10).delay(3, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long integer) throws Exception {
                        L.i("skipUntil>>>" + integer);
                    }
                });
    }

    /**
     * kipWhile订阅原始的Observable，但是忽略它的发射物，直到你指定的某个条件变为false的那一刻，它开始发射原始Observable。
     * 即：满足条件的数据都过滤掉，跟filter相反
     */
    private void skipWhile() {
        Observable.range(1, 10)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer < 6;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                L.i("skipWhile>>>" + integer);
            }
        });
    }

    /**
     * take操作符是把源Observable产生的结果，提取前面的n个提交给订阅者，而忽略后面的结果
     */
    public void take() {
        Observable.range(1, 20)
                .take(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("take>>>" + integer);
                    }
                });
    }

    /**
     * takeLast操作符是把源Observable产生的结果的后n项提交给订阅者，提交时机是Observable发布onCompleted通知之时。
     */
    public void takeLast() {
        Observable.range(1, 50)
                .takeLast(5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("takeLast>>>" + integer);
                    }
                });
    }
}
