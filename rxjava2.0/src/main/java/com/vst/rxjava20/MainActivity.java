package com.vst.rxjava20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends Activity {

    private ListView lv_main;
    private String[] items = new String[]{"RxJava2.0", "Rxlifecycle", "Retrofit+Rxjava2.0"
            , "map操作符", "操作符", "背压", "RxBinding", "RxFragment", "异常处理"
            , "创建Observable", "组合操作符", "变换操作符", "过滤操作符", "条件操作符", "数学操作符"
            , "连接操作符", "Rx生命周期", "block同步研究", "Rx线程研究"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                        startActivity(new Intent(MainActivity.this, RxDemo1Activity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, RxRxlifecycleActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, RxRetrofitActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, RxMapActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, RxActionActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, RxBackPressActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, RxBindActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, RxFragmentActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, RxErrorActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this, RxCreateActivity.class));
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this, CombinationActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(MainActivity.this, TransformActivity.class));
                        break;
                    case 12:
                        startActivity(new Intent(MainActivity.this, FilterActivity.class));
                        break;
                    case 13:
                        startActivity(new Intent(MainActivity.this, JudgeActivity.class));
                        break;
                    case 14:
                        startActivity(new Intent(MainActivity.this, MathActivity.class));
                        break;
                    case 15:
                        startActivity(new Intent(MainActivity.this, LinkActivity.class));
                        break;
                    case 16:
                        startActivity(new Intent(MainActivity.this, RxLifeActivity.class));
                        break;
                    case 17:
                        startActivity(new Intent(MainActivity.this, BlockActivity.class));
                        break;
                    case 18:
                        startActivity(new Intent(MainActivity.this, ThreadActivitgy.class));
                        break;
                }
            }
        });
    }
}
