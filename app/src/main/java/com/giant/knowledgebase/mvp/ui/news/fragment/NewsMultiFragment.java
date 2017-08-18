package com.giant.knowledgebase.mvp.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerNewsMultiComponent;
import com.giant.knowledgebase.di.module.NewsMultiModule;
import com.giant.knowledgebase.mvp.contract.NewsMultiContract;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;
import com.giant.knowledgebase.mvp.presenter.NewsMultiPresenter;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsRvMultiAdapter;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.EventBusTags.NEWS_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.REPLACE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class NewsMultiFragment extends BaseFragment<NewsMultiPresenter> implements NewsMultiContract.View, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private NewsRvMultiAdapter mAdapter;

    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    public static NewsMultiFragment newInstance() {
        NewsMultiFragment fragment = new NewsMultiFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerNewsMultiComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .newsMultiModule(new NewsMultiModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_multi, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);

        initAdapter();

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);

        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        recylerView.addItemDecoration(new Divider(getContext()));

//        recylerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (GreenDaoManager.getDaoInstant().getNewsBeanDao().count() > 0) {
//            if(false){
            L.i("1数据库加载");
            refreshLayout.endRefreshing();
            mAdapter.setData(GreenDaoManager.getDaoInstant().getNewsBeanDao().loadAll());
        }

        refreshLayout.beginRefreshing();//预加载一次
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter = new NewsRvMultiAdapter(getContext());
        //item点击
        mAdapter.setOnItemClickListener(new BGARvMultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("bean", mAdapter.getItem(position));
                mBundle.putBoolean("isSmoothComment",false);//是否滚动评论区
                intent.putExtras(mBundle);
                launchActivity(intent);//跳转到详情页
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recylerView.setAdapter(mAdapter);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            //先从本地读取已有数据，再网络请求数据
//        mAdapter.setData(mPresenter.getNews2());

            Request request = NewsService.getNewsList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<NewsJson>() {
                @Override
                public void onResponse(NewsJson jsonBean, okhttp3.Headers headers) {
                    L.i("2网络加载");
//                    L.i(jsonBean);
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    else L.i("request size=" + jsonBean.getNewsList().size());
                    mAdapter.addNewData(jsonBean.getNewsList());
                    //保存最新请求的数据到数据库
                    GreenDaoManager.getDaoInstant().getNewsBeanDao().deleteAll();
                    GreenDaoManager.getDaoInstant().getNewsBeanDao().insertOrReplaceInTx(jsonBean.getNewsList());
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
                    T.showShort(getContext(), "请求数据失败");
                }

            });
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

    /**
     * 通过eventbus post事件,远程遥控执行对应方法
     */
    @Subscriber(tag = NEWS_ACTION, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {

        if (message.obj == null)
            return;
        NewsBean obj = (NewsBean) message.obj;

        switch (message.what) {
            case REPLACE:
                for (NewsBean bean : mAdapter.getData()) {
                    if (bean.getId()==obj.getId()) {
                        mAdapter.setItem(bean, obj);
                    }
                }
                break;
        }
    }

}