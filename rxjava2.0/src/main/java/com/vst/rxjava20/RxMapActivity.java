package com.vst.rxjava20;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 有一个登录的例子, 不知大家有没有想过这么一个问题, 如果是一个新用户, 必须先注册, 等注册成功之后再自动登录
 */

public class RxMapActivity extends RxActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxdemo1);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                demo1();
                break;
            case R.id.btn2:
                demo2();
                break;
            case R.id.btn3:
                demo3();
                break;
            case R.id.btn4:

                break;
            case R.id.btn5:

                break;
            case R.id.btn6:

                break;
            case R.id.btn7:

                break;
            case R.id.btn8:

                break;
        }
    }

    private void demo1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(2);
                e.onNext(6);
                e.onNext(8);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "map to " + integer;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.i("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        L.i("onNext>>>" + s);
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

    /**
     * flatMap:将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
     * concatMap:它和flatMap的作用几乎一模一样, 只是它的结果是严格按照上游发送的顺序来发送的
     */
    private void demo2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(3);
                e.onNext(20);
                e.onNext(50);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(@NonNull Integer integer) throws Exception {
                        return Observable.range(integer, 5).delay(1000, TimeUnit.MILLISECONDS);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer i) throws Exception {
                        L.i("onNext:" + i);
                    }
                });
    }

    /**
     * 先登录后注册
     */
    private void demo3() {

        VRetrofit.getInstance().create(ApiServer.class)
                .getInfo2()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mProgressDialog.show();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull ResponseBody responseBody) throws Exception {
                        String resultStr = responseBody.string();
                        L.i("flatMap>>>" + resultStr);
                        JSONObject resultJson = new JSONObject(resultStr);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userName", resultJson.getString("userName"));
                        jsonObject.put("age", resultJson.getString("age"));
                        RequestBody requstBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                        return VRetrofit.getInstance().create(ApiServer.class).post9(requstBody);
                    }
                })
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.i("onSubscribe");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String json = responseBody.string();
                            L.i(json);
                            JSONObject object = new JSONObject(json);
                            L.i("userName:" + object.getString("userName"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("onError>>" + e.getMessage());
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        L.i("onComplete");
                        mProgressDialog.dismiss();
                    }
                });
    }
}
