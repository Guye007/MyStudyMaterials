package com.learn.apple.mystudymaterials.base;

/**
 * Created by apple on 2017/6/12.
 */

public interface BaseContract {

    interface BasePresenter<T>{

        void attachView(T view);

        void detachView();
    }

    interface BaseView {

        void showError();

        void complete();
    }
}
