package com.giant.knowledgebase.mvp.ui.news.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.KeyBoardUtils;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerNewsDetailsComponent;
import com.giant.knowledgebase.di.module.NewsDetailsModule;
import com.giant.knowledgebase.mvp.contract.NewsDetailsContract;
import com.giant.knowledgebase.mvp.model.api.service.CommentService;
import com.giant.knowledgebase.mvp.model.api.service.FavorService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.CommentJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.UserBean;
import com.giant.knowledgebase.mvp.presenter.NewsDetailsPresenter;
import com.giant.knowledgebase.mvp.ui.news.adapter.CommentRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.race604.drawable.wave.WaveDrawable;
import com.wx.goodview.GoodView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import im.delight.android.webview.AdvancedWebView;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.Api.NEWS_CATALOG_RECOMMEND;
import static com.giant.knowledgebase.app.constant.EventBusTags.NEWS_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QZONE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WXCIRCLE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WX_ACTION;
import static com.giant.knowledgebase.mvp.model.db.GreenDaoManager.getDaoInstant;
import static com.jess.arms.utils.Preconditions.checkNotNull;

public class NewsDetailsActivity extends BaseActivity<NewsDetailsPresenter> implements NewsDetailsContract.View, AdvancedWebView.Listener, BGAOnRVItemClickListener, BGAOnItemChildClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    NewsBean newsListBean;
    Timer timer;
    GoodView goodView;
    PopupWindow commentPopWindow;
    CustomPopWindow sharePopWindow;
    Context context;
    CommentRecyclerViewAdapter mAdapter;
    boolean isSmoothComment;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    AdvancedWebView webview;
    @BindView(R.id.shareCount_tv)
    TextView shareCountTv;
    @BindView(R.id.share_ll)
    AutoLinearLayout shareLl;
    @BindView(R.id.commentCount_tv)
    TextView commentCountTv;
    @BindView(R.id.comment_ll)
    AutoLinearLayout commentLl;
    @BindView(R.id.dislikeCount_tv)
    TextView dislikeCountTv;
    @BindView(R.id.dislike_ll)
    AutoLinearLayout dislikeLl;
    @BindView(R.id.likeCount_tv)
    TextView likeCountTv;
    @BindView(R.id.like_ll)
    AutoLinearLayout likeLl;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_comment_tv)
    TextView barCommentTv;
    @BindView(R.id.share_ib)
    ImageView shareIb;
    @BindView(R.id.comment_ib)
    ImageView commentIb;
    @BindView(R.id.dislike_ib)
    ImageView dislikeIb;
    @BindView(R.id.like_ib)
    ImageView likeIb;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.loadView)
    ImageView loadView;
    @BindView(R.id.loading_rl)
    AutoRelativeLayout loadingRl;

    WaveDrawable mWaveDrawable;
    @BindView(R.id.bar_refresh_iv)
    ImageView barRefreshIv;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        context = this;
        DaggerNewsDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .newsDetailsModule(new NewsDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_news_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        //获取传递对象
        newsListBean = (NewsBean) getIntent().getSerializableExtra("bean");
        isSmoothComment = (boolean) getIntent().getBooleanExtra("isSmoothComment", false);

        timer = new Timer();
        goodView = new GoodView(this);

        //初始化底部操作栏
        initActionBar();

        //初始化加载动画
        initLoadingView();

        //初始化webview
        initWebView();

        //初始化评论
        initRefreshLayout();
    }

    private void initActionBar() {
        shareCountTv.setText(String.valueOf(newsListBean.getShareCount()));
        commentCountTv.setText(String.valueOf(newsListBean.getCommentCount()));
        dislikeCountTv.setText(String.valueOf(newsListBean.getDislikeCount()));
        likeCountTv.setText(String.valueOf(newsListBean.getLikeCount()));

        if (1 == newsListBean.getIsLike()) {
            likeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
            likeIb.setBackgroundResource(R.mipmap.icon_action_like_click);
            dislikeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            dislikeIb.setBackgroundResource(R.mipmap.icon_action_dislike_normal);
        } else if (2 == newsListBean.getIsLike()) {
            likeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            likeIb.setBackgroundResource(R.mipmap.icon_action_like_normal);
            dislikeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
            dislikeIb.setBackgroundResource(R.mipmap.icon_action_dislike_click);
        } else {
            likeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            likeIb.setBackgroundResource(R.mipmap.icon_action_like_normal);
            dislikeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            dislikeIb.setBackgroundResource(R.mipmap.icon_action_dislike_normal);
        }
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        webview.setListener(this, this);
        webview.loadUrl(newsListBean.getUrl());
    }

    private void initLoadingView() {
        mWaveDrawable = new WaveDrawable(this, R.mipmap.pic_loading_web);
        mWaveDrawable.setWaveAmplitude(5);//振幅,max=100
        mWaveDrawable.setWaveLength(100);//波长,max=600
        mWaveDrawable.setWaveSpeed(5);//速度,max=50
//        mWaveDrawable.setLevel(4000);//进度,max=10000
//        mWaveDrawable.setIndeterminate(true);//是否自增

        // Use as common drawable
        loadView.setImageDrawable(mWaveDrawable);
    }

    @Override
    public void showLoading() {
        loadingRl.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        progressBar.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (progressBar != null && progressBar.getProgress() < 90) {
                        progressBar.incrementProgressBy(1);
                        mWaveDrawable.setLevel(100 * progressBar.getProgress());
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    public void hideLoading() {
        loadingRl.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webview.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        timer.cancel();
        webview.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webview.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!webview.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLoading();
    }

    @Override
    public void onPageFinished(String url) {
        hideLoading();
        //滑动到顶部
        scrollView.smoothScrollTo(0, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSmoothComment) {//滑动到评论区
                    scrollView.smoothScrollTo(0, webview.getBottom());
                }
            }
        }, 1000);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        hideLoading();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {
        webview.loadUrl(url);
    }

    @OnClick({R.id.share_ll, R.id.comment_ll, R.id.dislike_ll, R.id.like_ll, R.id.bar_back_iv, R.id.bar_comment_tv, R.id.bar_refresh_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_ll:
                actionShare();
                break;
            case R.id.comment_ll:
                actionComment();
                break;
            case R.id.dislike_ll:
                actionDislike();
                break;
            case R.id.like_ll:
                actionLike();
                break;
            case R.id.bar_back_iv:
                killMyself();
                break;
            case R.id.bar_comment_tv:
                showPopComment();
                break;
            case R.id.bar_refresh_iv:
                webview.reload();
                break;
        }
    }

    /**
     * 分享
     */
    private void actionShare() {
        //弹出分享菜单
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_share, null);
        //处理popWindow 显示内容
        handleShareLogic(contentView, newsListBean);
        sharePopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setFocusable(true)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.popwindow_bottom_anim_style)
                .create()
                .showAtLocation(shareIb, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 评论
     */
    private void actionComment() {
        //滑动到评论区
        scrollView.smoothScrollTo(0, webview.getBottom());
    }

    /**
     * 点踩
     */
    private void actionDislike() {
        if (1 == newsListBean.getIsLike()) {
            T.showShort(this, "您已经点过赞了");
        } else if (2 == newsListBean.getIsLike()) {
            T.showShort(this, "您已经点过踩了");
        } else {
            newsListBean.setIsLike(2);
            newsListBean.setDislikeCount(newsListBean.getDislikeCount() + 1);
            dislikeIb.setBackgroundResource(R.mipmap.icon_action_dislike_click);
            dislikeCountTv.setText(String.valueOf(newsListBean.getDislikeCount()));
            dislikeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
            goodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
            goodView.show(dislikeIb);
            //更新adapter列表数据
            Message message = new Message();
            message.what = newsListBean.getCatalog();
            message.obj = newsListBean;
            EventBus.getDefault().post(message, NEWS_ACTION);
            //提交请求...
        }
    }

    /**
     * 点赞
     */
    private void actionLike() {
        if (1 == newsListBean.getIsLike()) {
            T.showShort(this, "您已经点过赞了");
        } else if (2 == newsListBean.getIsLike()) {
            T.showShort(this, "您已经点过踩了");
        } else {
            newsListBean.setIsLike(1);
            newsListBean.setLikeCount(newsListBean.getLikeCount() + 1);
            likeIb.setBackgroundResource(R.mipmap.icon_action_like_click);
            likeCountTv.setText(String.valueOf(newsListBean.getLikeCount()));
            likeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
            goodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
            goodView.show(likeIb);
            //更新adapter列表数据
            Message message = new Message();
            message.what = newsListBean.getCatalog();
            message.obj = newsListBean;
            EventBus.getDefault().post(message, NEWS_ACTION);
            //提交请求...
        }
    }

    /**
     * 弹出评论窗口
     */
    private void showPopComment() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_comment, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        commentPopWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        commentPopWindow.setFocusable(true);

        // 设置点击其他地方就消失
        commentPopWindow.setOutsideTouchable(true);

        // 实例化一个ColorDrawable颜色为半透明0xb0000000
        commentPopWindow.setBackgroundDrawable(new BitmapDrawable());
        setBackgroundAlpha(0.7f);

        //添加pop窗口关闭事件
        commentPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                setBackgroundAlpha(1f);
            }
        });

        // 设置popWindow的显示和消失动画
        commentPopWindow.setAnimationStyle(R.style.popwindow_bottom_anim_style);

        //解决Popupwindow挡住键盘有关问题
        commentPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 在底部显示,第一个参数是parent，不是在其中显示，而是通过子控件找到主窗体，随便一个子控件都行
        commentPopWindow.showAtLocation(commentIb, Gravity.BOTTOM, 0, 0);

        // 处理点击事件
        handleLogic(view);

        KeyBoardUtils.openKeyboard(this);//自动打开软键盘
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleLogic(View contentView) {
        View.OnClickListener listener = v -> {

            switch (v.getId()) {
                case R.id.pop_send_iv:
                    String comment = "";
                    EditText editText = (EditText) contentView.findViewById(R.id.pop_comment_et);
                    comment = editText.getText().toString();

                    //是否已登录
                    if (0 == getDaoInstant().getUserBeanDao().count()) {
                        T.showShort(context,"请先登录");
                        return;
                    } else {
                        L.i("发布评论:"+comment);
                        if(!TextUtils.isEmpty(comment)) {
                            ////提交请求...
                            final String commentTemp=comment;
                            Request request = CommentService.getCommentPub(newsListBean.getId(), comment);
                            ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                                @Override
                                public void onResponse(BaseJson jsonBean, Headers headers) {
                                    L.i("onResponse");
                                    if (commentPopWindow != null) {
                                        commentPopWindow.dismiss();
                                    }
                                    T.showShort(context, "评论成功");
                                    //假数据，预览显示在评论第一条
                                    CommentJson.CommentListBean bean=new CommentJson.CommentListBean();
                                    UserBean userBean=GreenDaoManager.getDaoInstant().getUserBeanDao().loadAll().get(0);
                                    bean.setId(newsListBean.getId());
                                    bean.setPortrait(userBean.getPortrait());
                                    bean.setAuthor(userBean.getName());
                                    bean.setAuthorid(userBean.getUid());
                                    bean.setContent(commentTemp);
                                    bean.setIsLike(0);
                                    bean.setLikeCount(0);
                                    bean.setPubDate("刚刚");
                                    bean.setReplyCount(0);
                                    mAdapter.addFirstItem(bean);
                                }

                                /**
                                 * 可以不重写失败回调
                                 *
                                 * @param call
                                 * @param e
                                 */
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable e) {
                                    L.i("onFailure");
                                    T.showShort(context,"网络异常，请稍后再试");
                                }

                            });
                        }else{
                            T.showShort(this,"评论不能为空");
                        }
                    }
                    break;
            }
        };
        contentView.findViewById(R.id.pop_send_iv).setOnClickListener(listener);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleShareLogic(View contentView, NewsBean bean) {

        //显示
        if (1 == bean.getIsSubscribe()) {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.subscribe_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.subscribe_tv);
            imageView.setImageResource(R.mipmap.icon_action_subscribe_choose);
            textView.setText("取消订阅");
        } else {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.subscribe_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.subscribe_tv);
            imageView.setImageResource(R.mipmap.icon_action_subscribe);
            textView.setText("订阅");
        }

        if (1 == bean.getIsFavor()) {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.favor_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.favor_tv);
            imageView.setImageResource(R.mipmap.icon_action_favor_choose);
            textView.setText("取消收藏");
        } else {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.favor_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.favor_tv);
            imageView.setImageResource(R.mipmap.icon_action_favor);
            textView.setText("收藏");
        }

        //点击事件
        View.OnClickListener listener = v -> {
            if (sharePopWindow != null) {
                sharePopWindow.dissmiss();
            }

            Message message = new Message();
            switch (v.getId()) {
                case R.id.wx_share_ll:
                    message.what = SHARE_WX_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.wxCircle_share_ll:
                    message.what = SHARE_WXCIRCLE_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.qq_share_ll:
                    message.what = SHARE_QQ_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.qZone_share_ll:
                    message.what = SHARE_QZONE_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.wb_share_ll:
                    message.what = SHARE_SINA_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;

                case R.id.tipoff_ll:
                    //提交请求..
                    break;
                case R.id.copyLink_ll:
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(bean.getUrl());
                    T.showShort(context, "已复制");
                    break;
                case R.id.subscribe_ll:
                    //提交请求..
                    break;
                case R.id.favor_ll:
                    if (0 == bean.getIsFavor()) {
                        //更新adapter列表数据
                        bean.setIsFavor(1);
                        message.what = bean.getCatalog();
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorAdd(newsListBean.getId(), 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, Headers headers) {
                                T.showShort(context, "收藏成功！");
                            }
                        });
                    } else {
                        //更新adapter列表数据
                        bean.setIsFavor(0);
                        message.what = bean.getCatalog();
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorRemove(newsListBean.getId(), 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, Headers headers) {
                                T.showShort(context, "取消收藏成功！");
                            }
                        });
                    }
                    break;
                case R.id.cancel:
                    sharePopWindow.dissmiss();
                    break;
            }
        };
        contentView.findViewById(R.id.wx_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.wxCircle_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.qq_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.qZone_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.wb_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.tipoff_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.copyLink_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.subscribe_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.favor_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.cancel).setOnClickListener(listener);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化评论列表
     */
    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        refreshLayout.setPullDownRefreshEnable(false);//禁止下拉刷新
        mAdapter = new CommentRecyclerViewAdapter(this, recylerView);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);
        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        recylerView.addItemDecoration(new Divider(this));

//        recylerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        recylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recylerView.setAdapter(mAdapter);

        //预加载一次，上拉不刷新，上面是webview
        loadFirstComment();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        CommentJson.CommentListBean bean = mAdapter.getItem(position);

        //点赞
        if (childView.getId() == R.id.like_ll) {
            //如果已经点过赞或踩就不可以再操作了
            if (1 == bean.getIsLike()) {
                T.showShort(this, "你已经点过赞了");
            } else if (2 == bean.getIsLike()) {
                T.showShort(this, "你已经点过踩了");
            } else {
                bean.setLikeCount(bean.getLikeCount() + 1);//赞+1
                bean.setIsLike(1);//点赞
                mAdapter.setItem(position, bean);//替换旧数据
                mAdapter.notifyItemChanged(position);//刷新界面
                goodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
                goodView.show(childView);
                //提交请求...
            }
        }

        //用户信息
        else if (childView.getId() == R.id.pic_iv) {

        }

        //查看回复
        else if (childView.getId() == R.id.reply_tv) {
            Intent intent = new Intent(this, CommentReplyListActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("bean", bean);
            mBundle.putBoolean("isShowPop", false);//是否弹出评论编辑
            intent.putExtras(mBundle);
            launchActivity(intent);//跳转到详情页
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(this, CommentReplyListActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("bean", mAdapter.getItem(position));
        mBundle.putBoolean("isShowPop", true);//是否弹出评论编辑
        intent.putExtras(mBundle);
        launchActivity(intent);//跳转到详情页
    }

    private void loadFirstComment() {
        L.i("网络加载");
        new Handler().postDelayed(() -> {
            Request request = CommentService.getCommentList(newsListBean.getId(), 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<CommentJson>() {
                @Override
                public void onResponse(CommentJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    if(refreshLayout==null)return;
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    mAdapter.addNewData(jsonBean.getCommentList());
                }

                /**
                 * 可以不重写失败回调
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable e) {
                    L.i("onFailure");
                    if(refreshLayout==null)return;
                    refreshLayout.endRefreshing();
                }

            });
        }, 1000);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = CommentService.getCommentList(newsListBean.getId(), 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<CommentJson>() {
                @Override
                public void onResponse(CommentJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    if(refreshLayout==null)return;
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getCommentList());
                }

                /**
                 * 可以不重写失败回调
                 *
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable e) {
                    L.i("onFailure");
                    if(refreshLayout==null)return;
                    refreshLayout.endLoadingMore();
                }
            });
        }, 1000);

        return true;
    }

}