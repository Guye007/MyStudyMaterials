package com.learn.apple.mystudymaterials.ui.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.learn.apple.mystudymaterials.R;
import com.learn.apple.mystudymaterials.ReaderApplication;
import com.learn.apple.mystudymaterials.base.BaseActivity;
import com.learn.apple.mystudymaterials.base.Constant;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.ChapterRead;
import com.learn.apple.mystudymaterials.bean.Recommend;
import com.learn.apple.mystudymaterials.compoent.AppComponent;
import com.learn.apple.mystudymaterials.compoent.DaggerBookComponent;
import com.learn.apple.mystudymaterials.mannager.CacheManager;
import com.learn.apple.mystudymaterials.mannager.CollectionsManager;
import com.learn.apple.mystudymaterials.mannager.EventManager;
import com.learn.apple.mystudymaterials.ui.adapter.TocListAdapter;
import com.learn.apple.mystudymaterials.ui.contract.BookReadContract;
import com.learn.apple.mystudymaterials.ui.presenter.BookReadPresenter;
import com.learn.apple.mystudymaterials.utils.LogUtils;
import com.learn.apple.mystudymaterials.utils.ScreenUtils;
import com.learn.apple.mystudymaterials.utils.SharedPreferencesUtil;
import com.learn.apple.mystudymaterials.utils.ToastUtils;
import com.learn.apple.mystudymaterials.view.readview.BaseReadView;
import com.learn.apple.mystudymaterials.view.readview.OnReadStateChangeListener;
import com.learn.apple.mystudymaterials.view.readview.OverlappedWidget;
import com.learn.apple.mystudymaterials.view.readview.PageWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by apple on 2017/6/17.
 */

public class ReadActivity extends BaseActivity implements BookReadContract.View {


    private static final String INTENT_BEAN = "recommendBooksBean";
    private static final String INTENT_SD = "isFromSD";

    @Inject
    BookReadPresenter mPresenter;

    @Bind(R.id.flReadWidget)
    FrameLayout mFlReadWidget;
    @Bind(R.id.ivBack)
    ImageView mIvBack;
    @Bind(R.id.tvBookReadTocTitle)
    TextView mTvBookReadTocTitle;
    @Bind(R.id.tvBookReadReading)
    TextView mTvBookReadReading;
    @Bind(R.id.tvBookReadCommunity)
    TextView mTvBookReadCommunity;
    @Bind(R.id.tvBookReadIntroduce)
    TextView mTvBookReadIntroduce;
    @Bind(R.id.tvBookReadSource)
    TextView mTvBookReadSource;
    @Bind(R.id.llBookReadTop)
    LinearLayout mLlBookReadTop;
    @Bind(R.id.tvDownloadProgress)
    TextView mTvDownloadProgress;
    @Bind(R.id.ivBrightnessMinus)
    ImageView mIvBrightnessMinus;
    @Bind(R.id.seekbarLightness)
    SeekBar mSeekbarLightness;
    @Bind(R.id.ivBrightnessPlus)
    ImageView mIvBrightnessPlus;
    @Bind(R.id.tvFontsizeMinus)
    TextView mTvFontsizeMinus;
    @Bind(R.id.seekbarFontSize)
    SeekBar mSeekbarFontSize;
    @Bind(R.id.tvFontsizePlus)
    TextView mTvFontsizePlus;
    @Bind(R.id.cbVolume)
    CheckBox mCbVolume;
    @Bind(R.id.cbAutoBrightness)
    CheckBox mCbAutoBrightness;
    @Bind(R.id.gvTheme)
    GridView mGvTheme;
    @Bind(R.id.rlReadAaSet)
    LinearLayout mRlReadAaSet;
    @Bind(R.id.tvAddMark)
    TextView mTvAddMark;
    @Bind(R.id.tvClear)
    TextView mTvClear;
    @Bind(R.id.lvMark)
    ListView mLvMark;
    @Bind(R.id.rlReadMark)
    LinearLayout mRlReadMark;
    @Bind(R.id.tvBookReadMode)
    TextView mTvBookReadMode;
    @Bind(R.id.tvBookReadSettings)
    TextView mTvBookReadSettings;
    @Bind(R.id.tvBookReadDownload)
    TextView mTvBookReadDownload;
    @Bind(R.id.tvBookMark)
    TextView mTvBookMark;
    @Bind(R.id.tvBookReadToc)
    TextView mTvBookReadToc;
    @Bind(R.id.llBookReadBottom)
    LinearLayout mLlBookReadBottom;
    @Bind(R.id.rlBookReadRoot)
    RelativeLayout mRlBookReadRoot;

    private Recommend.RecommendBooks recommendBooks;
    private String bookId;
    private boolean isFromSD;
    private View decodeView;
    private int currentChapter = 1;

    private List<BookMixAToc.mixToc.Chapters> mChapterList = new ArrayList<>();
    private TocListAdapter mTocListAdapter;
    private ListPopupWindow mTocListPopupWindow;

    /**
     * 是否开始阅读章节
     **/
    private boolean startRead = false;
    private BaseReadView mPageWidget;

    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    private int curTheme = -1;

    //添加收藏需要，所以跳转的时候传递整个实体类
    public static void startActivity(Context context, Recommend.RecommendBooks recommendBooks) {
        startActivity(context, recommendBooks, false);
    }

    public static void startActivity(Context context, Recommend.RecommendBooks recommendBooks, boolean isFromSD) {
        context.startActivity(new Intent(context, ReadActivity.class)
                .putExtra(INTENT_BEAN, recommendBooks)
                .putExtra(INTENT_SD, isFromSD));
    }

    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        statusBarColor = ContextCompat.getColor(this, R.color.reader_menu_bg_color);
        return R.layout.activity_read;
    }

    @Override
    public void initDatas() {

        recommendBooks = (Recommend.RecommendBooks) getIntent().getSerializableExtra(INTENT_BEAN);
        bookId = recommendBooks._id;
        isFromSD = getIntent().getBooleanExtra(INTENT_SD, false);

        mTvBookReadTocTitle.setText(recommendBooks.title);


        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        CollectionsManager.getInstance().setRecentReadingTime(bookId);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //延迟1秒刷新书架
                        EventManager.refreshCollectionList();
                    }
                });
    }

    @Override
    public void configViews() {

        hideStatusBar();
        decodeView = getWindow().getDecorView();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLlBookReadTop.getLayoutParams();
        params.topMargin = ScreenUtils.getStatusBarHeight(this) - 2;
        mLlBookReadTop.setLayoutParams(params);

        initTocList();
        initPagerWidget();
        mPresenter.attachView(this);

        mPresenter.getBookMixAToc(bookId, "chapters");


    }

    private void initTocList() {
        mTocListAdapter = new TocListAdapter(this, mChapterList, bookId, currentChapter);
        mTocListPopupWindow = new ListPopupWindow(this);
        mTocListPopupWindow.setAdapter(mTocListAdapter);
        mTocListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mTocListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mTocListPopupWindow.setAnchorView(mLlBookReadTop);
        mTocListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTocListPopupWindow.dismiss();
                currentChapter = position + 1;
                mTocListAdapter.setCurrentChapter(currentChapter);
                startRead = false;
                showDialog();
                readCurrentChapter();
                hideReadBar();
            }
        });
    }


    private void initPagerWidget() {
        if (SharedPreferencesUtil.getInstance().getInt(Constant.FLIP_STYLE, 0) == 0){
            mPageWidget = new PageWidget(this, bookId, mChapterList, new ReadListener());
        }else {
            mPageWidget = new OverlappedWidget(this, bookId, mChapterList, new ReadListener());
        }
        registerReceiver(receiver, intentFilter);
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)){
            mPageWidget.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
                    ContextCompat.getColor(this, R.color.chapter_title_night));
        }
        mFlReadWidget.removeAllViews();
        mFlReadWidget.addView(mPageWidget);
    }

    @Override
    public void initToolBar() {
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showBookToc(List<BookMixAToc.mixToc.Chapters> list) {

        mChapterList.clear();
        mChapterList.addAll(list);

        readCurrentChapter();
    }

    /*
    * 获取当前章节，章节文件存在则直接阅读，不存在则请求
    * */

    public void readCurrentChapter(){
        if (CacheManager.getInstance().getChapterFile(bookId,currentChapter) != null){
            showChapterRead(null, currentChapter);
        }else {
            mPresenter.getChapterRead(mChapterList.get(currentChapter - 1).link, currentChapter);
        }
    }

    //加载章节内容
    @Override
    public synchronized void showChapterRead(ChapterRead.Chapter data, int chapter) {
        if (data != null) {
            CacheManager.getInstance().saveChapterFile(bookId, chapter, data);
        }

        if (!startRead) {
            startRead = true;
            currentChapter = chapter;
            if (!mPageWidget.isPrepared) {
                mPageWidget.init(curTheme);
            } else {
                mPageWidget.jumpToChapter(currentChapter);
            }
            hideDialog();
        }

    }

    private synchronized void hideReadBar() {
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, mRlReadAaSet, mRlReadMark);
        hideStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private synchronized void showReadBar() { // 显示工具栏
        visible(mLlBookReadBottom, mLlBookReadTop);
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private synchronized void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(mLlBookReadTop)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }

    @Override
    public void netError(int chapter) {
        hideDialog();//防止因为网络问题而出现dialog不消失
        if (Math.abs(chapter - currentChapter) <= 1) {
            ToastUtils.showToast(R.string.net_error);
        }
    }

    @Override
    public void showError() {
        hideDialog();
    }

    @Override
    public void complete() {
        hideDialog();
    }

    private class ReadListener implements OnReadStateChangeListener {
        @Override
        public void onChapterChanged(int chapter) {
            LogUtils.i("onChapterChanged:" + chapter);
            currentChapter = chapter;
            mTocListAdapter.setCurrentChapter(currentChapter);
            // 加载前一节 与 后三节
            for (int i = chapter - 1; i <= chapter + 3 && i <= mChapterList.size(); i++) {
                if (i > 0 && i != chapter
                        && CacheManager.getInstance().getChapterFile(bookId, i) == null) {
                    mPresenter.getChapterRead(mChapterList.get(i - 1).link, i);
                }
            }
        }

        @Override
        public void onPageChanged(int chapter, int page) {
            LogUtils.i("onPageChanged:" + chapter + "-" + page);
        }

        @Override
        public void onLoadChapterFailure(int chapter) {
            LogUtils.i("onLoadChapterFailure:" + chapter);
            startRead = false;
            if (CacheManager.getInstance().getChapterFile(bookId, chapter) == null)
                mPresenter.getChapterRead(mChapterList.get(chapter - 1).link, chapter);
        }

        @Override
        public void onCenterClick() {
            LogUtils.i("onCenterClick");
            toggleReadBar();
        }

        @Override
        public void onFlip() {
            hideReadBar();
        }
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPageWidget != null) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    mPageWidget.setBattery(100 - level);
                } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    mPageWidget.setTime(sdf.format(new Date()));
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        // 观察内存泄漏情况
        ReaderApplication.getRefWatcher(this).watch(this);
    }
}
