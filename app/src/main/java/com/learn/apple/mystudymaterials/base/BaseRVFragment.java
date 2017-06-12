package com.learn.apple.mystudymaterials.base;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.OnLoadMoreListener;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.learn.apple.mystudymaterials.view.recyclerview.swipe.OnRefreshListener;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by apple on 2017/6/12.
 */

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter, T2> extends BaseFragment implements
        OnLoadMoreListener,OnRefreshListener,RecyclerArrayAdapter.OnItemClickListener{

    @Inject
    protected T1 mPresenter;


    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    protected void setupActivityCompoent(AppComponent appComponent) {

    }

    @Override
    protected void configViews() {

    }

    @Override
    protected void initDatas() {

    }

    /*
    * 此方法不可再重写
    * */
    @Override
    protected void attachView() {

    }
}
