package com.learn.apple.mystudymaterials;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

/**
 * Created by apple on 2017/6/9.
 */

public class ReaderApplication extends Application{

    private ReaderApplication readerApplication;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context){
        ReaderApplication application = (ReaderApplication) context.getApplicationContext();
        return application.refWatcher;

    }
}
