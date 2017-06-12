package com.learn.apple.mystudymaterials.base;


import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.apple.mystudymaterials.ReaderApplication;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.view.loadding.CustomDialog;

import butterknife.ButterKnife;

/**
 * Created by apple on 2017/6/12.
 */

public  abstract class BaseFragment extends Fragment {

    protected View parentView;
    protected LayoutInflater inflater;
    protected FragmentActivity activity;
    protected Context mContext;
    private CustomDialog dialog;

    public abstract
    @LayoutRes
    int getLayoutResId();

    protected abstract void setupActivityCompoent(AppComponent appComponent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutResId(),container,false);
        activity = getSupportActivity();
        mContext = activity;
        this.inflater = inflater;
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setupActivityCompoent(ReaderApplication.getsInstance().getAppComponent());
        attachView();
        initDatas();
        configViews();
    }

    /*
    * 对各种控件进行设置，适配，填充数据
    * */
    protected abstract void configViews();

    protected abstract void initDatas();

    protected abstract void attachView();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    public Context getApplicationContext(){
        return this.activity == null ? (getActivity() == null ? null :
                getActivity().getApplicationContext()) : this.activity.getApplicationContext();
    }

    protected LayoutInflater getLayoutInflater(){
        return inflater;
    }

    protected View getParentView(){
        return parentView;
    }

    public CustomDialog getDialog(){
        if (dialog == null){
            dialog = CustomDialog.instance(getActivity());
            dialog.setCancelable(false);
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

    public void dismissDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    protected void gone(final View...views){
        if (views != null && views.length > 0){
            for (View view : views){
                if (view != null){
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View...views){
        if (views != null && views.length > 0){
            for (View view : views){
                if (view != null){
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected boolean isVisible(View view){
        return view.getVisibility() == View.VISIBLE;
    }
}
