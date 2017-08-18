package com.giant.knowledgebase.mvp.ui.news.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerNewsComponent;
import com.giant.knowledgebase.di.module.NewsModule;
import com.giant.knowledgebase.mvp.contract.NewsContract;
import com.giant.knowledgebase.mvp.model.api.service.FavorService;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;
import com.giant.knowledgebase.mvp.presenter.NewsPresenter;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wx.goodview.GoodView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.EventBusTags.NEWS_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.REPLACE;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QZONE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WXCIRCLE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WX_ACTION;
import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Jorble on 2017/5/31.
 */

public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View, BGAOnRVItemClickListener, BGAOnItemChildClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private NewsRecyclerViewAdapter mAdapter;
    GoodView mGoodView;
    CustomPopWindow sharePopWindow;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    private RxPermissions mRxPermissions;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerNewsComponent
                .builder()
                .appComponent(appComponent)
                .newsModule(new NewsModule(this))//请将NewsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //点赞动画
        mGoodView = new GoodView(getContext());
        //初始化列表
        initRefreshLayout();
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new NewsRecyclerViewAdapter(getContext(), recylerView);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);

        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        recylerView.addItemDecoration(new Divider(getContext()));

//        recylerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recylerView.setAdapter(mAdapter);

        refreshLayout.beginRefreshing();//预加载一次
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {

        new Handler().postDelayed(() -> {
            //先从本地读取已有数据，再网络请求数据
//        mAdapter.setData(mPresenter.getNews2());

            if (GreenDaoManager.getDaoInstant().getNewsBeanDao().count() > 0) {
                L.i("1数据库加载");
                refreshLayout.endRefreshing();
                mAdapter.setData(GreenDaoManager.getDaoInstant().getNewsBeanDao().loadAll());
            }else {
                Request request = NewsService.getNewsList(1, 20);
                ItheimaHttp.send(request, new HttpResponseListener<NewsJson>() {
                    @Override
                    public void onResponse(NewsJson jsonBean, okhttp3.Headers headers) {
                        L.i("2网络加载");
                        refreshLayout.endRefreshing();
                        if (jsonBean == null) return;
                        mAdapter.addNewData(jsonBean.getNewsList());
                        //保存最新请求的数据到数据库
                        GreenDaoManager.getDaoInstant().getNewsBeanDao().deleteAll();
                        GreenDaoManager.getDaoInstant().getNewsBeanDao().insertInTx(jsonBean.getNewsList());
                        L.i("数据库资讯数:" + GreenDaoManager.getDaoInstant().getNewsBeanDao().count());
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
                        refreshLayout.endRefreshing();
                        T.showShort(getContext(), "请求数据失败请重试");
                    }
                });
            }
        }, 1000);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = NewsService.getNewsList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<NewsJson>() {
                @Override
                public void onResponse(NewsJson jsonBean, okhttp3.Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getNewsList());
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
                    T.showShort(getContext(), "请求数据失败请重试");
                }
            });
        }, 1000);

        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        NewsBean newsListBean = mAdapter.getItem(position);

        //点赞
        if (childView.getId() == R.id.like_ll) {
            //如果已经点过赞或踩就不可以再操作了
            if (1 == newsListBean.getIsLike()) {
                T.showShort(getContext(), "你已经点过赞了");
            } else if (2 == newsListBean.getIsLike()) {
                T.showShort(getContext(), "你已经点过踩了");
            } else {
                newsListBean.setLikeCount(newsListBean.getLikeCount() + 1);//赞+1
                newsListBean.setIsLike(1);//点赞
                mAdapter.setItem(position, newsListBean);//替换旧数据
                mAdapter.notifyItemChanged(position);//刷新界面
                mGoodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
                mGoodView.show(childView);
                //提交请求...
            }
        }

        //点踩
        else if (childView.getId() == R.id.dislike_ll) {
            //如果已经点过赞或踩就不可以再操作了
            if (1 == newsListBean.getIsLike()) {
                T.showShort(getContext(), "你已经点过赞了");
            } else if (2 == newsListBean.getIsLike()) {
                T.showShort(getContext(), "你已经点过踩了");
            } else {
                newsListBean.setDislikeCount(newsListBean.getDislikeCount() + 1);//踩+1
                newsListBean.setIsLike(2);//点踩
                mAdapter.setItem(position, newsListBean);//替换旧数据
                mAdapter.notifyDataSetChanged();//刷新界面
                mGoodView.setTextInfo("+1", getResources().getColor(R.color.theme_color), 12);//+1动画
                mGoodView.show(childView);
                //提交请求...
            }
        }

        //评论
        else if (childView.getId() == R.id.comment_ll) {

        }

        //转发
        else if (childView.getId() == R.id.share_ll) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_share, null);
            //处理popWindow 显示内容
            handleShareLogic(contentView, newsListBean);
            sharePopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                    .setView(contentView)
                    .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                    .setBgDarkAlpha(0.7f) // 控制亮度
                    .setAnimationStyle(R.style.popwindow_bottom_anim_style)
                    .create()
                    .showAtLocation(childView, Gravity.BOTTOM, 0, 0);
        }

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
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(bean.getUrl());
                    T.showShort(getContext(), "已复制");
                    break;
                case R.id.subscribe_ll:
                    //提交请求..
                    break;
                case R.id.favor_ll:
                    if (0 == bean.getIsFavor()) {

                        //更新adapter列表数据
                        bean.setIsFavor(1);
                        message.what = REPLACE;
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorAdd("id312", 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, okhttp3.Headers headers) {
                                T.showShort(getContext(), "收藏成功！");
                            }
                        });
                    } else {
                        //更新adapter列表数据
                        bean.setIsFavor(0);
                        message.what = REPLACE;
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorRemove("id312", 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, okhttp3.Headers headers) {
                                T.showShort(getContext(), "取消收藏成功！");
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


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("bean", mAdapter.getItem(position));
        intent.putExtras(mBundle);
        launchActivity(intent);//跳转到详情页
    }

//    /**
//     * 通过eventbus post事件,远程遥控执行对应方法
//     */
//    @Subscriber(tag = NEWS_ACTION, mode = ThreadMode.MAIN)
//    public void onReceive(Message message) {
//        switch (message.what) {
//            case REPLACE:
//                if (message.obj == null)
//                    break;
//
//                NewsBean obj = (NewsBean) message.obj;
//                for (NewsBean bean : mAdapter.getData()) {
//                    if (bean.getId() == obj.getId()) {
//                        mAdapter.setItem(bean, obj);
//                    }
//                }
//                break;
//        }
//    }
}