package com.learn.apple.mystudymaterials.ui.presenter;

import android.content.Context;

import com.learn.apple.mystudymaterials.api.BookApi;
import com.learn.apple.mystudymaterials.base.RxPresenter;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.Recommend;
import com.learn.apple.mystudymaterials.mannager.SettingManager;
import com.learn.apple.mystudymaterials.ui.contract.RecommendContract;
import com.learn.apple.mystudymaterials.utils.ACache;
import com.learn.apple.mystudymaterials.utils.LogUtils;
import com.learn.apple.mystudymaterials.utils.RxUtil;
import com.learn.apple.mystudymaterials.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2017/6/13.
 */

public class RecommendPresenter extends RxPresenter<RecommendContract.View>
        implements RecommendContract.Presenter<RecommendContract.View>{

    private Context mContext;
    private BookApi bookApi;

    @Inject
    public RecommendPresenter(Context context, BookApi bookApi){
        this.mContext = context;
        this.bookApi = bookApi;
    }
    @Override
    public void getRecommendList() {
        String key = StringUtils.creatAcacheKey("recommend-list", SettingManager.getInstance().getUserChooseSex());
        Observable<Recommend> fromNetWork = bookApi.getRecommend(SettingManager.getInstance().getUserChooseSex())
                .compose(RxUtil.<Recommend>rxCacheListHelper(key));

        //依次检查disk、network
        Subscription rxSubscription = Observable.concat(RxUtil.rxCreateDiskObservable(key, Recommend.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Recommend>() {
                    @Override
                    public void onCompleted() {
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getRecommendList", e.toString());
                        mView.showError();
                    }

                    @Override
                    public void onNext(Recommend recommend) {
                        if (recommend != null){
                            List<Recommend.RecommendBooks> list = recommend.books;
                            if (list != null && !list.isEmpty() && mView != null){
                                mView.showRecommendList(list);
                            }
                        }
                    }
                });
        addSubscribe(rxSubscription);
    }

    public void getTocList(final String bookId){
        bookApi.getBookMixAToc(bookId, "chapters").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError" + e);
                        mView.showError();
                    }

                    @Override
                    public void onNext(BookMixAToc data) {
                        ACache.get(mContext).put(bookId + "bookToc", data);
                        List<BookMixAToc.mixToc.Chapters> list = data.mixToc.chapters;
                        if (list != null && !list.isEmpty() && mView != null){
                            mView.showBookToc(bookId, list);
                        }
                    }
                });
    }




















}
