package com.learn.apple.mystudymaterials.ui.Activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.base.BaseActivity;
import com.learn.apple.mystudymaterials.base.Constant;
import com.learn.apple.mystudymaterials.utils.SharedPreferencesUtil;
import com.learn.apple.mystudymaterials.utils.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.icon_2);
        mCommonToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
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

    @OnClick(R.id.bt_go_home)
    public void onViewClicked() {
        Log.e("main","--------");
        Intent intent = new Intent(this,FirstActivity.class);
        startActivity(intent);
    }
}
