package com.learn.apple.mystudymaterials.ui.Activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.utils.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    protected View statusBarView = null;
    protected int statusBarColor = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (statusBarColor == 0) {
            statusBarView = StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        } else if (statusBarColor != -1) {
            statusBarView = StatusBarCompat.compat(this, statusBarColor);
        }
        toolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (toolbar != null){
            initToolBar();
            setSupportActionBar(toolbar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    private void initToolBar() {
        toolbar.setLogo(R.mipmap.icon_2);
        toolbar.setTitle("");
    }

    @OnClick(R.id.bt_go_home)
    public void onViewClicked() {
        Log.e("main","--------");
        Intent intent = new Intent(this,FirstActivity.class);
        startActivity(intent);
    }
}
