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
import com.learn.apple.mystudymaterials.ui.contract.RecommendContract;
import com.learn.apple.mystudymaterials.ui.easyadapter.RecommendAdapter;
import com.learn.apple.mystudymaterials.ui.presenter.RecommendPresenter;
import com.learn.apple.mystudymaterials.view.recyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.eventbus.EventBus;

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

    @Override
    protected void initDatas() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void configViews() {
        initAdapter(RecommendAdapter.class, true, false);


    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> list) {

    }

    @Override
    public void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    @Override
    protected void setupActivityCompoent(AppComponent appComponent) {

    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
