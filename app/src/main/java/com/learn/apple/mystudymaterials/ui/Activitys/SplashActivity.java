package com.learn.apple.mystudymaterials.ui.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.learn.apple.mystudymaterials.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 2017/6/9.
 */

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.tv_skip)
    TextView tvSkip;
    private Runnable runnable;
    private boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        runnable = new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        };

        tvSkip.postDelayed(runnable,2000);
    }

    private synchronized void goHome() {
        if (!flag){
            flag = true;
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.tv_skip)
    public void onViewClicked() {
        goHome();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        tvSkip.removeCallbacks(runnable);
        ButterKnife.unbind(this);
    }
}
