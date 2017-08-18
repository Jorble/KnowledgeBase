package com.giant.knowledgebase.mvp.ui.subscribe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerSubscribeAddComponent;
import com.giant.knowledgebase.di.module.SubscribeAddModule;
import com.giant.knowledgebase.mvp.contract.SubscribeAddContract;
import com.giant.knowledgebase.mvp.model.api.service.SubscribeService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.SubscribeJson;
import com.giant.knowledgebase.mvp.presenter.SubscribeAddPresenter;
import com.giant.knowledgebase.mvp.ui.subscribe.adapter.SubscribeAddRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
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

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SubscribeAddActivity extends BaseActivity<SubscribeAddPresenter> implements SubscribeAddContract.View, BGAOnRVItemClickListener, BGAOnItemChildClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


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
    @BindView(R.id.divider_tv)
    TextView dividerTv;
    @BindView(R.id.divider_ll)
    AutoLinearLayout dividerLl;

    private SubscribeAddRecyclerViewAdapter mAdapter;
    Context context;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSubscribeAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .subscribeAddModule(new SubscribeAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_subscribe_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        context = this;
        barTitleTv.setText("添加订阅");
        dividerTv.setText("您可能感兴趣的");

        initRefreshLayout();
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


    @OnClick(R.id.bar_back_iv)
    public void onViewClicked() {
        killMyself();
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new SubscribeAddRecyclerViewAdapter(this, recylerView);
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

        //预加载一次
        refreshLayout.beginRefreshing();

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.subscribe_bt) {
            //提交请求，取消订阅
            Request request = SubscribeService.getSubscribeAdd(mAdapter.getItem(position).getId());
            ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                @Override
                public void onResponse(BaseJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    if (jsonBean == null) return;
                    if (jsonBean.isSuccess()) {
                        //列表中移除
                        mAdapter.removeItem(position);
                        T.showShort(context, "订阅成功");
                    }
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
                    T.showShort(context, "取消失败");
                }

            });
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //进入订阅号详情
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = SubscribeService.getSubscribeAdviceList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<SubscribeJson>() {
                @Override
                public void onResponse(SubscribeJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    mAdapter.setData(jsonBean.getSubscribeList());
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
                    T.showShort(context, "请求数据失败请重试");
                }

            });
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = SubscribeService.getSubscribeAdviceList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<SubscribeJson>() {
                @Override
                public void onResponse(SubscribeJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getSubscribeList());
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

}