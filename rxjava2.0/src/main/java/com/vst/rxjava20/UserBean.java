package com.vst.rxjava20;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by zwy on 2017/9/15.
 * email:16681805@qq.com
 */

public class UserBean {
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Observable<String> getObservable() {
        //一开始创建Observable，订阅时候发送信息
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                e.onNext(name);
//                e.onComplete();
//            }
//        });
        //一开始创建Observable，创建时候发送信息
//        return Observable.just(name);
        //订阅时候创建Observable
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just(name);
            }
        });
    }
}
