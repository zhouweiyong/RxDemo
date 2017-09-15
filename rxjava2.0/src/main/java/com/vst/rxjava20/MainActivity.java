package com.vst.rxjava20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView lv_main;
    private String[] items = new String[]{"RxJava2.0", "Rxlifecycle", "Retrofit+Rxjava2.0", "map操作符", "操作符", "背压", "RxBinding", "RxFragment", "异常处理", "创建Observable"};

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
                }
            }
        });
    }
}
