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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by zwy on 2017/9/18.
 * email:16681805@qq.com
 *条件和布尔操作
 */

public class JudgeActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{ "all"};

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
                        all();
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



    /**
     * 判定是否Observable发射的所有数据都满足某个条件
     * 传递一个谓词函数给All操作符，这个函数接受原始Observable发射的数据，根据计算返回一个布尔值。
     * All返回一个只发射一个单个布尔值的Observable，如果原始Observable正常终止并且每一项数据都满足条件，就返回true；
     * 如果原始Observable的任意一项数据不满足条件就返回False。
     */
    private void all() {
        Observable.range(10, 20)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer i) throws Exception {
                        return i>11;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean rs) throws Exception {
                L.i("accept>>>" + rs);
            }
        });
    }
}
