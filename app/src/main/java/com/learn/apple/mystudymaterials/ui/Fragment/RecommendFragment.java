package com.learn.apple.mystudymaterials.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.base.BaseRVFragment;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.Recommend;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.compoent.DaggerMainComponent;
import com.learn.apple.mystudymaterials.mannager.CollectionsManager;
import com.learn.apple.mystudymaterials.ui.Activitys.MainActivity;
import com.learn.apple.mystudymaterials.ui.contract.RecommendContract;
import com.learn.apple.mystudymaterials.ui.easyadapter.RecommendAdapter;
import com.learn.apple.mystudymaterials.ui.presenter.RecommendPresenter;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 2017/6/13.
 */

public class RecommendFragment extends BaseRVFragment<RecommendPresenter, Recommend.RecommendBooks>
        implements RecommendContract.View, RecyclerArrayAdapter.OnItemLongClickListener {

    @Bind(R.id.tvSelectAll)
    TextView tvSelectAll;
    @Bind(R.id.tvDelete)
    TextView tvDelete;
    @Bind(R.id.llBatchManagement)
    LinearLayout llBatchManagement;

    private boolean isSelectAll = false;

    private List<BookMixAToc.mixToc.Chapters> chaptersList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Subscribe
    public void initDatas() {

    }

    @Override
    public void configViews() {
        initAdapter(RecommendAdapter.class, true, false);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headView = LayoutInflater.from(activity).inflate(R.layout.foot_view_shelf,null);
                return headView;
            }

            @Override
            public void onBindView(final View headerView) {
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) activity).setCurrentItem(2);
                    }
                });
            }
        });

        mPresenter.getRecommendList();
    }

    @Override
    public void showError() {
        loaddingError();
        dismissDialog();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
        //推荐列表默认加入收藏
        for (Recommend.RecommendBooks bean : list){
            //
            CollectionsManager.getInstance().add(bean);
        }
    }

    @Override
    public void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list) {
        chaptersList.clear();
        chaptersList.addAll(list);
        dismissDialog();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        gone(llBatchManagement);
        List<Recommend.RecommendBooks> data = CollectionsManager.getInstance().getCollectionListBySort();
        mAdapter.clear();
        mAdapter.addAll(data);
        //不加下面这句代码会导致，添加本地书籍的时候，部分书籍添加后直接崩溃
        //报错：Scrapped or attached views may not be recycled. isScrap:false isAttached:true
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setRefreshing(false);

    }

    @Override
    public void onLoadMore() {

    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tvSelectAll, R.id.tvDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSelectAll:
                break;
            case R.id.tvDelete:
                break;
        }
    }
}
