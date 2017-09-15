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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zwy on 2017/9/15.
 * email:16681805@qq.com
 */

public class RxCreateActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"defer", "from", "just","timer", "amb"};

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
                        from();
                        break;
                    case 2:
                        just();
                        break;
                    case 3:
                        timer();
                        break;
                    case 4:
                        amb();
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
     * 创建Observable
     * 特殊的Observable
     * Observable.empty() 毫无理由的不再发射数据并正常结束。
     * Observable.never() 不发射数据并且永远不会结束。
     * Observable.error() 不发射数据并且以错误结束。
     */
    private void demo1() {
        UserBean userBean = new UserBean();
        Observable<String> observable = userBean.getObservable();
        userBean.setName("HelloTom");
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                L.i("name>>>" + s);
            }
        });


    }

    /**
     * 通过from创建Observable
     * 把集合或者数组分解成一个元素
     */
    private void from() {
        List<String> list = Arrays.asList("from1", "from2", "from3");
        String[] s = new String[]{"hell01", "hell02", "hell03"};
        //分解集合
//        Observable.fromIterable(list)
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        L.i("form>>>>" + s);
//                    }
//                });
        //分解数组
        Observable.fromArray(s)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("form>>>>" + s);
                    }
                });
    }

    /**
     * 通过just创建Observable
     * 可以有1到9个参数，按顺序发射。参数可以是列表或数组，不同于from(),会发射整个列表。
     */
    private void just() {
        List<String> list = Arrays.asList("from1", "from2", "from3");
        Observable.just("from1", "from2", "from3")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("accept>>>" + s);
                    }
                });

        Observable.just(list)
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> list) throws Exception {

                    }
                });
    }

    private void timer() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        L.i("accept>>>" + aLong);
                    }
                });
    }

    private void amb() {
        List<Observable<String>> list = new ArrayList<>();
        list.add(Observable.just("hello1"));
        list.add(Observable.just("hello2"));
        list.add(Observable.just("hello3"));

        Observable.amb(list)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        L.i("accept>>>" + s);
                    }
                });
    }
}
