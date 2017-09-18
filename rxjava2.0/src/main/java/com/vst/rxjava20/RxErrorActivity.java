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
import io.reactivex.functions.Function;

/**
 * Created by zwy on 2017/9/15.
 * email:16681805@qq.com
 * 错误处理操作
 */

public class RxErrorActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"主动抛出错误","抛出异常"};

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
                       if (integer>=20)
                           return Observable.error(new Throwable("你错了！！！"));
                       return Observable.just(integer);
                   }
               }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                L.i("onNext>>>"+integer);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError>>>"+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
