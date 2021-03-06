package com.learn.apple.mystudymaterials.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.ReaderApplication;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.utils.SharedPreferencesUtil;
import com.learn.apple.mystudymaterials.utils.StatusBarCompat;
import com.learn.apple.mystudymaterials.view.loadding.CustomDialog;

import butterknife.ButterKnife;

/**
 * Created by apple on 2017/6/12.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected int statusBarColor = 0;
    protected View statusBarView = null;
    private Context mContext;
    public Toolbar mCommonToolbar;
    private boolean mNowMode;
    private CustomDialog dialog;//进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        if (statusBarColor == 0){
            statusBarView = StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }else if (statusBarColor != -1){
            statusBarView = StatusBarCompat.compat(this,statusBarColor);
        }
        transparent19and20();
        mContext = this;
        ButterKnife.bind(this);

        setupActivityComponent(ReaderApplication.getsInstance().getAppComponent());
        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (mCommonToolbar != null){
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }

        initDatas();
        configViews();
        mNowMode = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT,false) != mNowMode){
            if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT,false)){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        dismissDialog();

    }

    protected void gone(final View... views){
        if (views != null && views.length > 0){
            for (View view : views){
                if (view != null){
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views){
        if (views != null && views.length > 0){
            for (View view : views){
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    protected boolean isVisible(View view){
        return view.getVisibility() == View.VISIBLE;
    }

    //dialog
    public CustomDialog getDialog(){
        if (dialog == null){
            dialog = CustomDialog.instance(this);
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public void hideDialog(){
        if (dialog != null){
            dialog.hide();
        }
    }

    public void showDialog(){
        getDialog().show();
    }
    public void dismissDialog() {
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideStatusBar(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null){
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void showStatusBar(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null){
            statusBarView.setBackgroundColor(statusBarColor);
        }
    }

    private void transparent19and20() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public abstract int getLayoutId() ;
    /*
    *  对各种控件进行设置，适配，填充数据
    */
    public abstract void configViews();

    public abstract void initDatas();

    public abstract void initToolBar();

    protected abstract void setupActivityComponent(AppComponent appComponent);
}
