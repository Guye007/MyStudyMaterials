package com.learn.apple.mystudymaterials.ui.presenter;

import android.content.Context;

import com.learn.apple.mystudymaterials.api.BookApi;
import com.learn.apple.mystudymaterials.base.RxPresenter;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.ChapterRead;
import com.learn.apple.mystudymaterials.ui.contract.BookReadContract;
import com.learn.apple.mystudymaterials.utils.LogUtils;
import com.learn.apple.mystudymaterials.utils.RxUtil;
import com.learn.apple.mystudymaterials.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2017/6/17.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View> {


    private Context context;
    private BookApi bookApi;

    @Inject
    public BookReadPresenter(Context context, BookApi bookApi){
        this.context = context;
        this.bookApi = bookApi;
    }
    @Override
    public void getBookMixAToc(String bookId, String viewChapters) {
        String key = StringUtils.creatAcacheKey("book-toc", bookId, viewChapters);
        Observable<BookMixAToc.mixToc> fromNewWork = bookApi.getBookMixAToc(bookId, viewChapters)
                .map(new Func1<BookMixAToc, BookMixAToc.mixToc>() {
                    @Override
                    public BookMixAToc.mixToc call(BookMixAToc bookMixAToc) {
                        return bookMixAToc.mixToc;
                    }
                })
                .compose(RxUtil.<BookMixAToc.mixToc>rxCacheListHelper(key));
          //依次检查disk、network
        Subscription rxSubscription = Observable.concat(RxUtil.rxCreateDiskObservable(key, BookMixAToc.mixToc.class), fromNewWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc.mixToc>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError:" + e);
                        mView.netError(0);
                    }

                    @Override
                    public void onNext(BookMixAToc.mixToc data) {

                        List<BookMixAToc.mixToc.Chapters> list = data.chapters;
                        if (list != null && !list.isEmpty() && mView != null){
                            mView.showBookToc(list);
                        }
                    }
                });
        addSubscribe(rxSubscription);
    }

    @Override
    public void getChapterRead(String url, final int chapter) {

        Subscription rxSubscription = bookApi.getChapterRead(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
                        mView.netError(chapter);
                    }

                    @Override
                    public void onNext(ChapterRead data) {

                        if (data.chapter != null && mView != null){
                            mView.showChapterRead(data.chapter, chapter);
                        }else {
                            mView.netError(chapter);
                        }
                    }
                });
        addSubscribe(rxSubscription);
    }
}





















