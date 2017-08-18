package com.giant.knowledgebase.mvp.ui.news.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.app.utils.KeyBoardUtils;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerCommentReplyListComponent;
import com.giant.knowledgebase.di.module.CommentReplyListModule;
import com.giant.knowledgebase.mvp.contract.CommentReplyListContract;
import com.giant.knowledgebase.mvp.model.api.service.CommentService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.CommentJson;
import com.giant.knowledgebase.mvp.model.entity.CommentReplyJson;
import com.giant.knowledgebase.mvp.model.entity.UserBean;
import com.giant.knowledgebase.mvp.presenter.CommentReplyListPresenter;
import com.giant.knowledgebase.mvp.ui.news.adapter.ReplyRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.klinker.android.link_builder.LinkConsumableTextView;
import com.wx.goodview.GoodView;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.mvp.model.db.GreenDaoManager.getDaoInstant;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CommentReplyListActivity extends BaseActivity<CommentReplyListPresenter> implements CommentReplyListContract.View, BGAOnRVItemClickListener, BGAOnItemChildClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.pic_iv)
    ImageView picIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.reply_tv)
    TextView replyTv;
    @BindView(R.id.like_ib)
    ImageView likeIb;
    @BindView(R.id.likeCount_tv)
    TextView likeCountTv;
    @BindView(R.id.like_ll)
    AutoLinearLayout likeLl;
    @BindView(R.id.divider_tv)
    TextView dividerTv;
    @BindView(R.id.divider_ll)
    AutoLinearLayout dividerLl;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;
    @BindView(R.id.content_tv)
    LinkConsumableTextView contentTv;


    GoodView goodView;
    CommentJson.CommentListBean baseCommentBean;
    ReplyRecyclerViewAdapter mAdapter;
    boolean isShowPop;
    PopupWindow commentPopWindow;
    Context context;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCommentReplyListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .commentReplyListModule(new CommentReplyListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_comment_reply_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        context=this;
        //获取传递对象
        baseCommentBean = (CommentJson.CommentListBean) getIntent().getSerializableExtra("bean");
        isShowPop = (boolean) getIntent().getBooleanExtra("isShowPop", false);

        barTitleTv.setText("评论详情");
        dividerTv.setText("全部评论");
        goodView = new GoodView(this);
        initBaseComment();

        //初始化评论
        initRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示评论编辑
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShowPop){
                    showPopComment(baseCommentBean.getAuthor());
                }
            }
        },500);
    }

    /**
     * 初始化被回复评论
     */
    private void initBaseComment() {
        likeCountTv.setText(String.valueOf(baseCommentBean.getLikeCount()));
        timeTv.setText(baseCommentBean.getPubDate());
        contentTv.setText(baseCommentBean.getContent());
        nameTv.setText(baseCommentBean.getAuthor());

        //0-无 1-赞 2-踩
        if (1 == baseCommentBean.getIsLike()) {
            likeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
            likeIb.setImageResource(R.mipmap.icon_action_like_click);
        } else if (2 == baseCommentBean.getIsLike()) {
            likeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            likeIb.setImageResource(R.mipmap.icon_action_like_normal);
        } else {
            likeCountTv.setTextColor(getResources().getColor(R.color.text_black3));
            likeIb.setImageResource(R.mipmap.icon_action_like_normal);
        }

        Glide
                .with(this) //上下文
                .load(baseCommentBean.getPortrait()) //图片地址
                .transform(new GlideCircleTransform(this))
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
//                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into(picIv); //显示在哪个控件中

        //隐藏查看回复
        replyTv.setVisibility(View.INVISIBLE);

    }

    /**
     * 初始化评论列表
     */
    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        refreshLayout.setPullDownRefreshEnable(false);//禁止下拉刷新
        mAdapter = new ReplyRecyclerViewAdapter(this, recylerView);
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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


    @OnClick({R.id.pic_iv, R.id.like_ib,R.id.item_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_iv:
                break;
            case R.id.item_ll:
                showPopComment(baseCommentBean.getAuthor());
                break;
            case R.id.like_ib:
                likeCountTv.setTextColor(getResources().getColor(R.color.theme_color));
                likeIb.setImageResource(R.mipmap.icon_action_like_click);
                goodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
                goodView.show(likeIb);
                break;
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        CommentReplyJson.ReplyListBean bean = mAdapter.getItem(position);

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

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        showPopComment(mAdapter.getItem(position).getAuthor());//回复评论
    }

    private void loadFirstComment() {
        L.i("网络加载");
        refreshLayout.beginRefreshing();
        new Handler().postDelayed(() -> {
            Request request = CommentService.getReplyList("id123","id123", 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<CommentReplyJson>() {
                @Override
                public void onResponse(CommentReplyJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    mAdapter.addNewData(jsonBean.getReplyList());
                }

                /**
                 * 可以不重写失败回调
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable e) {
                    L.i("onFailure");
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
            Request request = CommentService.getReplyList("id123","id123", 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<CommentReplyJson>() {
                @Override
                public void onResponse(CommentReplyJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getReplyList());
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
                    refreshLayout.endLoadingMore();
                    T.showShort(context, "请求数据失败请重试");
                }
            });
        }, 1000);

        return true;
    }

    /**
     * 弹出评论窗口
     */
    private void showPopComment(String replyName) {
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
        commentPopWindow.showAtLocation(contentTv, Gravity.BOTTOM, 0, 0);

        // 处理点击事件
        handleLogic(view, replyName);

        KeyBoardUtils.openKeyboard(this);//自动打开软键盘
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleLogic(View contentView, String replyName) {
        EditText editText = (EditText) contentView.findViewById(R.id.pop_comment_et);
        editText.setHint("回复" + replyName);

        View.OnClickListener listener = v -> {
            if (commentPopWindow != null) {
                commentPopWindow.dismiss();
            }

            switch (v.getId()) {
                case R.id.pop_send_iv:
                    String comment = "";
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
                            final String replyNameTemp=replyName.equals(baseCommentBean.getAuthor())?"":replyName;//如果是回复楼主，则设为空
                            Request request = CommentService.getReplyPub(baseCommentBean.getId(),replyNameTemp, comment);
                            ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                                @Override
                                public void onResponse(BaseJson jsonBean, Headers headers) {
                                    L.i("onResponse");
                                    if (commentPopWindow != null) {
                                        commentPopWindow.dismiss();
                                    }
                                    T.showShort(context, "评论成功");
                                    //假数据，预览显示在评论第一条
                                    CommentReplyJson.ReplyListBean bean=new CommentReplyJson.ReplyListBean();
                                    UserBean userBean= getDaoInstant().getUserBeanDao().loadAll().get(0);
                                    bean.setId("id312");
                                    bean.setPortrait(userBean.getPortrait());
                                    bean.setAuthor(userBean.getName());
                                    bean.setAuthorid(userBean.getUid());
                                    bean.setContent(commentTemp);
                                    bean.setIsLike(0);
                                    bean.setLikeCount(0);
                                    bean.setPubDate("刚刚");
                                    bean.setReplyName(replyNameTemp);
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
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
