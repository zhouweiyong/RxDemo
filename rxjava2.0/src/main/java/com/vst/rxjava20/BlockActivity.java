package com.vst.rxjava20;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by zwy on 2017/9/20.
 * email:16681805@qq.com
 */

public class BlockActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"blockingGet", "blockingIterable", "blockingSubscribe"};

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
                        blockingGet();
                        break;
                    case 1:
                        blockingIterable();
                        break;
                    case 2:
                        blockingSubscribe();
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
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
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

    private void blockingGet() {
        List<Integer> list = Observable.range(1, 10)
                .toList()
                .blockingGet();
        for (Integer integer : list) {
            L.i("blockingGet>>>" + integer);
        }

    }
    //将一个发射数据序列的Observable转换为一个Iterable
    private void blockingIterable() {
        Iterable<Integer> integers = Observable.range(1, 10)
                .blockingIterable();
        Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            L.i("blockingIterable>>>" + next.intValue());
        }


    }

    public void blockingSubscribe() {
        Observable.range(1, 10)
                .delay(2, TimeUnit.SECONDS)
                .blockingSubscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        L.i("blockingSubscribe>>>" + integer);
                    }
                });

        L.i("------------end-----------");
    }
}
