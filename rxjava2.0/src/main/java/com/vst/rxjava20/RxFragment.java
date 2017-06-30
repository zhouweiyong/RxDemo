package com.vst.rxjava20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zwy on 2017/4/12.
 * email:16681805@qq.com
 */

public class RxFragment extends com.trello.rxlifecycle2.components.RxFragment implements View.OnClickListener {

    private Button btn1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rxdemo2, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        btn1 = (Button) view.findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:

                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                        try {
                            for (int i = 0; i < 1000; i++) {
                                e.onNext(i);
                                Thread.sleep(500);
                            }
                            e.onComplete();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<Integer>bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.i("zwy", "" + integer);
                            }
                        });


                break;
        }
    }
}
