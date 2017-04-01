package com.vst.rxjava20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by user on 2017/3/31.
 */

public class RxRetrofitActivity extends RxActivity implements View.OnClickListener {

    private Button btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo2);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                demo1();
                break;
        }
    }

    private void demo1() {
        ApiServer apiServer = VRetrofit.getInstance().create(ApiServer.class);
        apiServer.getInfo2().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.i("onSubscribe");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            L.i("onNext>>>" + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("onError");
                    }

                    @Override
                    public void onComplete() {
                        L.i("onComplete");
                    }
                });
    }
}
