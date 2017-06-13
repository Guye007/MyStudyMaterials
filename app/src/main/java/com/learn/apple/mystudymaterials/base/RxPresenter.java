package com.learn.apple.mystudymaterials.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by apple on 2017/6/13.
 * 基于Rx的Presenter的封装，控制订阅的生命周期
 * unsubscribe()这个方法很重要，
 * 因为在subscribe()之后，Observable会持有Subscriber的引用
 * 这个引用如果不能及时被释放，将有内存泄漏的风险。
 */

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;

    protected CompositeSubscription mCompositeSubscription;

    protected void addSubscribe(Subscription subscription){
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscribe(){
        if (mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
    }
    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
