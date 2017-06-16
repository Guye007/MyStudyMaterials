package com.learn.apple.mystudymaterials.ui.presenter;

import com.learn.apple.mystudymaterials.api.BookApi;
import com.learn.apple.mystudymaterials.base.RxPresenter;
import com.learn.apple.mystudymaterials.bean.user.Login;
import com.learn.apple.mystudymaterials.ui.contract.MainContract;
import com.learn.apple.mystudymaterials.utils.LogUtils;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2017/6/14.
 */

public class MainActivityPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View> {

    private BookApi bookApi;

    @Inject
    public MainActivityPresenter(BookApi bookApi){
        this.bookApi = bookApi;
    }
    @Override
    public void login(String uid, String token, String platform) {

        Subscription rxSubscription = bookApi.login(uid, token, platform).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("login" + e.toString());
                    }

                    @Override
                    public void onNext(Login data) {

                        if (data != null && mView != null && data.ok){
                            mView.loginSuccess();
                            LogUtils.e(data.user.toString());
                        }
                    }
                });
        addSubscribe(rxSubscription);

    }

    @Override
    public void syncBookShelf() {

    }
}




















