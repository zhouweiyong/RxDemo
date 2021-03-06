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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by zwy on 2017/9/18.
 * email:16681805@qq.com
 * 数学操作和聚合操作
 */

public class MathActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"count", "reduce", "scan"};

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
                        count();
                        break;
                    case 1:
                        reduce();
                        break;
                    case 2:
                        scan();
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
     * 计算原始Observable发射物的数量，然后只发射这个值
     */
    private void count() {
        Observable.range(1, 10)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        L.i("count>>>" + aLong);
                    }
                });
    }

    /**
     * Reduce操作符应用一个函数接收Observable发射的数据和函数的计算结果作为下次计算的参数，输出最后的结果。
     * 跟scan操作符很类似，只是scan会输出每次计算的结果，而reduce只会输出最后的结果。
     */
    public void reduce() {
        Observable.range(1, 10)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        L.i(integer + ">>>>" + integer2);
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                L.i("subscribe>>>" + integer);
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
}
