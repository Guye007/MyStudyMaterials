package com.learn.apple.mystudymaterials.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.view.recyclerview.EasyRecyclerView;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.OnLoadMoreListener;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.learn.apple.mystudymaterials.view.recyclerview.swipe.OnRefreshListener;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by apple on 2017/6/12.
 */

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter, T2> extends BaseFragment implements
        OnLoadMoreListener,OnRefreshListener,RecyclerArrayAdapter.OnItemClickListener{

    @Inject
    protected T1 mPresenter;
    protected RecyclerArrayAdapter<T2> mAdapter;

    @Bind(R.id.recyclerview)
    protected EasyRecyclerView mRecyclerView;

    protected int start = 0;
    protected int limit = 20;

    /*
    * 此方法不可再重写
    * */
    @Override
    public void attachView() {
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    protected void initAdapter(boolean refreshable, boolean loadmoreable){
        if (mRecyclerView != null){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getSupportActivity()));
            mRecyclerView.setItemDecoration(ContextCompat.getColor(activity, R.color.common_divider_narrow), 1, 0, 0);
            mRecyclerView.setAdapterWithProgress(mAdapter);
        }

        if (mAdapter != null){
            mAdapter.setOnItemClickListener(this);
            mAdapter.setError(R.layout.common_error_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.resumeMore();
                }
            });
            if (loadmoreable){
                mAdapter.setMore(R.layout.common_more_view, this);
                mAdapter.setNoMore(R.layout.common_nomore_view);
            }
            if (refreshable && mRecyclerView != null){
                mRecyclerView.setRefreshListener(this);
            }
        }
    }

    protected void initAdapter(Class<? extends RecyclerArrayAdapter<T2>> clazz, boolean refreshable, boolean loadmoreable){
        mAdapter = (RecyclerArrayAdapter<T2>) createInstance(clazz);
        initAdapter(refreshable, loadmoreable);
    }

    public Object createInstance(Class<?> cls){
        Object obj;

        try {
            Constructor constructor = cls.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            obj = constructor.newInstance(mContext);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onRefresh() {
        mRecyclerView.setRefreshing(true);
    }

    protected void loaddingError(){
        //说明缓存也没有加载，那就显示errorview，如果有缓存，即使刷新失败也不显示error
        if (mAdapter.getCount() < 1){
            mAdapter.clear();
        }
        mAdapter.pauseMore();
        mRecyclerView.setRefreshing(false);
        mRecyclerView.showTipViewAndDelayClose("似乎没有网络哦");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }
}
