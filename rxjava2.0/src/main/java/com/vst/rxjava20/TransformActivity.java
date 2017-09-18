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
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * Created by zwy on 2017/9/18.
 * email:16681805@qq.com
 * 转换操作
 */

public class TransformActivity extends Activity {
    private int index;
    private ListView lv_main;
    private String[] items = new String[]{"buffer", "flatMap", "switchMap", "groupBy", "scan", "concatMap", "window"};

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
                        buffer();
                        break;
                    case 1:
                        flatMap();
                        break;
                    case 2:
                        switchMap();
                        break;
                    case 3:
                        groupBy();
                        break;
                    case 4:
                        scan();
                        break;
                    case 5:
                        concatMap();
                        break;
                    case 6:
                        window();
                        break;
                    case 7:
                        break;

                }
            }
        });
    }

    /**
     * buffer操作符周期性地收集源Observable产生的结果到列表中，并把这个列表提交给订阅者，
     * 订阅者处理后，清空buffer列表，同时接收下一次收集的结果并提交给订阅者，周而复始。
     * <p>
     * 需要注意的是，一旦源Observable在产生结果的过程中出现异常，即使buffer已经存在收集到的结果，订阅者也会马上收到这个异常，
     * 并结束整个过程。
     */
    public void buffer() {
        //每5个数据打包发给订阅者，也可以设置一段时间时间内的数据
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(5)
                .subscribe(new Consumer<List<Long>>() {
                    @Override
                    public void accept(@NonNull List<Long> list) throws Exception {
                        for (Long aLong : list) {
                            L.i(index + "rs==" + aLong);
                        }
                        index++;
                    }
                });

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("data1");
        }
        Observable.fromIterable(list)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s + "ttttt";
                    }
                }).buffer(list.size())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        L.i("");
                    }
                });
    }

    /**
     * flatMap操作符是把Observable产生的结果转换成多个Observable，然后把这多个Observable“扁平化”成一个Observable，并依次提交产生的结果给订阅者。
     * flatMap操作符通过传入一个函数作为参数转换源Observable，在这个函数中，你可以自定义转换规则，最后在这个函数中返回一个新的Observable，
     * 然后flatMap操作符通过合并这些Observable结果成一个Observable，并依次提交结果给订阅者。
     * 注意：如果任何一个通过这个flatMap操作产生的单独的Observable调用onError异常终止了，这个Observable自身会立即调用onError并终止。
     */
    public void flatMap() {
        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull Long aLong) throws Exception {
                        if (aLong % 10 == 3) {
                            return Observable.just(aLong).delay(3, TimeUnit.SECONDS).map(new Function<Long, Long>() {
                                @Override
                                public Long apply(@NonNull Long aLong) throws Exception {
                                    return aLong + 100;
                                }
                            });
                        }
                        return Observable.just(aLong);
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                L.i("" + aLong);
            }
        });
    }

    /**
     * RxJava还实现了switchMap操作符。它和flatMap很像，除了一点：当原始Observable发射一个新的数据（Observable）时，
     * 它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个。
     * 与flatMap操作符不同的是，switchMap操作符会保存最新的Observable产生的结果而舍弃旧的结果，
     * 举个例子来说，比如源Observable产生A、B、C三个结果，通过switchMap的自定义映射规则，映射后应该会产生A1、A2、B1、B2、C1、C2，
     * 但是在产生B2的同时，C1已经产生了，这样最后的结果就变成A1、A2、B1、C1、C2，B2被舍弃掉了！
     */
    public void switchMap() {
        Observable.interval(1, TimeUnit.SECONDS)
                .switchMap(new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull Long aLong) throws Exception {
                        if (aLong % 10 == 3) {
                            //3,13...这些数据将永远接收不到，因为延迟发送导致后面的数据会先发送到，这个Observable将被取消订阅
                            return Observable.just(aLong).delay(3, TimeUnit.SECONDS);
                        }
                        return Observable.just(aLong);
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                L.i("" + aLong);
            }
        });
    }

    /**
     * 与flatMap操作符不同的是，concatMap操作符在处理产生的Observable时，采用的是“连接(concat)”的方式，
     * 而不是“合并(merge)”的方式，这就能保证产生结果的顺序性，也就是说提交给订阅者的结果是按照顺序提交的，不会存在交叉的情况。
     */
    public void concatMap() {
        Observable.interval(1, TimeUnit.SECONDS)
                .concatMap(new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull Long aLong) throws Exception {
                        if (aLong % 10 == 3) {
                            //3,13...这些数据将永远接收不到，因为延迟发送导致后面的数据会先发送到，这个Observable将被取消订阅
                            return Observable.just(aLong).delay(3, TimeUnit.SECONDS);
                        }
                        return Observable.just(aLong);
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                L.i("" + aLong);
            }
        });
    }

    /**
     * GroupBy操作符将原始Observable分拆为一些Observables集合，
     * 它们中的每一个是发射原始Observable数据序列的一个子序列。
     * 哪个数据项由哪一个Observable发射是由一个函数判定的，
     * 这个函数给每一项指定一个Key，Key相同的数据会被同一个Observable发射。
     */
    public void groupBy() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(30)
                .groupBy(new Function<Long, String>() {
                    @Override
                    public String apply(@NonNull Long l) throws Exception {
                        if (l % 3 == 1) {
                            return "one";
                        } else if (l % 3 == 2) {
                            return "two";
                        }
                        return "zero";
                    }
                })
                .subscribe(new Consumer<GroupedObservable<String, Long>>() {
                    @Override
                    public void accept(@NonNull final GroupedObservable<String, Long> groupedObservable) throws Exception {
                        groupedObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                L.i(groupedObservable.getKey() + "===" + aLong);
                            }
                        });
                    }
                });
    }


    public void scan() {
        //递加，每次递加都会发送结果
        Observable.range(1, 10)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer sum, @NonNull Integer item) throws Exception {
                        return sum + item;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                L.i("rs===" + integer);
            }
        });

    }

    /**
     * window操作符非常类似于buffer操作符，区别在于buffer操作符产生的结果是一个List缓存，
     * 而window操作符产生的结果是一个Observable，订阅者可以对这个结果Observable重新进行订阅处理。
     */
    public void window() {
        index = 0;
        Observable.interval(1, TimeUnit.SECONDS)
                .window(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(@NonNull Observable<Long> observable) throws Exception {
                        observable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                L.i(index + "rs===" + aLong);
                            }
                        });
                        index++;
                    }
                });
    }
}
