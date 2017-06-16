package com.learn.apple.mystudymaterials.ui.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.base.BaseActivity;
import com.learn.apple.mystudymaterials.base.Constant;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.compoent.DaggerMainComponent;
import com.learn.apple.mystudymaterials.mannager.EventManager;
import com.learn.apple.mystudymaterials.ui.Fragment.CommunityFragment;
import com.learn.apple.mystudymaterials.ui.Fragment.FindFragment;
import com.learn.apple.mystudymaterials.ui.Fragment.RecommendFragment;
import com.learn.apple.mystudymaterials.ui.contract.MainContract;
import com.learn.apple.mystudymaterials.ui.presenter.MainActivityPresenter;
import com.learn.apple.mystudymaterials.utils.SharedPreferencesUtil;
import com.learn.apple.mystudymaterials.utils.ToastUtils;
import com.learn.apple.mystudymaterials.view.RVPIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View{

    @Bind(R.id.common_toolbar)
    Toolbar commonToolbar;
    @Bind(R.id.indicator)
    RVPIndicator indicator;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<String> mDatas;
    private List<Fragment> mTabContents;
    private FragmentPagerAdapter mAdapter;

    @Inject
    MainActivityPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initDatas() {

        mDatas = Arrays.asList(getResources().getStringArray(R.array.home_tabs));
        mTabContents = new ArrayList<>();
        mTabContents.add(new RecommendFragment());
//        mTabContents.add(new CommunityFragment());
//        mTabContents.add(new FindFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }

            @Override
            public int getCount() {
                return mTabContents.size();
            }
        };
    }

    @Override
    public void configViews() {

        indicator.setTabItemTitles(mDatas);
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(3);
        indicator.setViewPager(viewpager,0);

        mPresenter.attachView(this);

        indicator.postDelayed(new Runnable() {
            @Override
            public void run() {
             showChooseSexPopuWindow();
            }
        },500);

    }

    private void showChooseSexPopuWindow() {
        //TODO
    }

    public void setCurrentItem(int position){
        viewpager.setCurrentItem(position);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.icon_2);
        mCommonToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                break;
            case R.id.action_night_mode:
                if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void syncBookShelfCompleted() {

        dismissDialog();
        EventManager.refreshCollectionList();
    }

    @Override
    public void showError() {

        ToastUtils.showSingleToast("同步异常");
        dismissDialog();
    }

    @Override
    public void complete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }


    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL){
                currentBackPressedTime = System.currentTimeMillis();
                ToastUtils.showToast("再按一次退出程序");
                return true;
            }else {
                finish();//退出
            }
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_MENU){
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}














