package com.learn.apple.mystudymaterials.ui.contract;

import com.learn.apple.mystudymaterials.base.BaseContract;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.ChapterRead;
import com.learn.apple.mystudymaterials.bean.Recommend;

import java.util.List;

/**
 * Created by apple on 2017/6/17.
 */

public interface BookReadContract {

    interface View extends BaseContract.BaseView{

        void showBookToc(List<BookMixAToc.mixToc.Chapters> list);

        void showChapterRead(ChapterRead.Chapter data, int chapter);

        void netError(int chapter);//添加网络处理异常接口
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getBookMixAToc(String bookId,String view);

        void getChapterRead(String url, int chapter);

    }
}
