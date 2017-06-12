package com.learn.apple.mystudymaterials;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.learn.apple.mystudymaterials.base.Constant;
import com.learn.apple.mystudymaterials.base.CrashHandler;
import com.learn.apple.mystudymaterials.utils.AppUtils;
import com.learn.apple.mystudymaterials.utils.LogUtils;
import com.learn.apple.mystudymaterials.utils.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by apple on 2017/6/9.
 */

public class ReaderApplication extends Application{

    private static ReaderApplication sInstance;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context){
        ReaderApplication application = (ReaderApplication) context.getApplicationContext();
        return application.refWatcher;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        sInstance = this;
        initCompoent();
        AppUtils.init(this);
        CrashHandler.getInstance().init(this);
        initPrefs();
        initNightMode();
    }

    public static ReaderApplication getsInstance(){
        return sInstance;
    }


    protected void initNightMode() {
        boolean isNight = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT,false);
        LogUtils.d("isNight=" + isNight);
        if (isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /*
    * 初始化SharedPreference
    * */
    private void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(),getPackageName()+"_preference",Context.MODE_MULTI_PROCESS);
    }

    private void initCompoent() {

    }
}
