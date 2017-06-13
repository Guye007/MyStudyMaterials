package com.learn.apple.mystudymaterials.ui.contract;

import com.learn.apple.mystudymaterials.base.BaseContract;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.Recommend;

import java.util.List;

/**
 * Created by apple on 2017/6/13.
 */

public interface RecommendContract {

    interface View extends BaseContract.BaseView{

        void showRecommendList(List<Recommend.RecommendBooks> list);

        void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getRecommendList();
    }
}
