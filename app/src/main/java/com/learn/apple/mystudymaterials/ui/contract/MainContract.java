package com.learn.apple.mystudymaterials.ui.contract;

import com.learn.apple.mystudymaterials.base.BaseContract;

/**
 * Created by apple on 2017/6/14.
 */

public interface MainContract {

    interface View extends BaseContract.BaseView{
        void loginSuccess();

        void syncBookShelfCompleted();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void login(String uid, String token, String platform);

        void syncBookShelf();
    }
}
