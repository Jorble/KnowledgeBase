package com.giant.knowledgebase.mvp.ui.home.hot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.NewsModel;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsRvMultiAdapter;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.Api.CODE_NO_MORE;
import static com.giant.knowledgebase.app.constant.Api.CODE_SUCCESS;
import static com.giant.knowledgebase.app.constant.Api.NEWS_CATALOG_HOT;
import static com.giant.knowledgebase.app.constant.EventBusTags.NEWS_ACTION;
import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * Use the {@link HotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotFragment extends Fragment implements  BGARefreshLayout.BGARefreshLayoutDelegate{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int catalog = NEWS_CATALOG_HOT;

    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    Unbinder unbinder;

    private NewsRvMultiAdapter mAdapter;

    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    public HotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);

        //初始化列表
        initRefreshLayout();

        return view;
    }


    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);

        initAdapter();//初始化adapter

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);

        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        recylerView.addItemDecoration(new Divider(getContext()));

//        recylerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (GreenDaoManager.getDaoInstant().getNewsBeanDao().count() > 0) {
//            if(false){
            L.i("db load");
            L.i("loadHistory recommend size="+NewsModel.loadHistory(catalog).size());
            mAdapter.setData(NewsModel.loadHistory(catalog));
        }else{
            refreshLayout.beginRefreshing();//网络加载
        }
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
                intent.putExtras(mBundle);
                checkNotNull(intent);
                UiUtils.startActivity(intent);//跳转到详情页
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
            Request request = NewsService.getNewsList(catalog, 20);
            ItheimaHttp.send(request, new HttpResponseListener<NewsJson>() {
                @Override
                public void onResponse(NewsJson jsonBean, okhttp3.Headers headers) {
                    L.i("network load");
//                    L.i(jsonBean);
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;

                    switch (jsonBean.getErrCode()){
                        case CODE_SUCCESS:
                            mAdapter.addNewData(jsonBean.getNewsList());
                            recylerView.scrollToPosition(0);//滑动到最顶条
                            break;
                        case CODE_NO_MORE:
                            T.showShort(getContext(),R.string.already_last_data);
                            break;
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
                    refreshLayout.endRefreshing();
                    T.showShort(getContext(), R.string.request_failed);
                }
            });
        }, 1000);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = NewsService.getNewsList(catalog, 20);
            ItheimaHttp.send(request, new HttpResponseListener<NewsJson>() {
                @Override
                public void onResponse(NewsJson jsonBean, okhttp3.Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;

                    switch (jsonBean.getErrCode()){
                        case CODE_SUCCESS:
                            mAdapter.addMoreData(jsonBean.getNewsList());
                            break;
                        case CODE_NO_MORE:
                            T.showShort(getContext(),R.string.no_more_data);
                            break;
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
                    refreshLayout.endLoadingMore();
                    T.showShort(getContext(), R.string.request_failed);
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
            case catalog://在详情页点赞和点踩可以更新数据到首页列表
                for (NewsBean bean : mAdapter.getData()) {
                    if (bean.getId().equals(obj.getId())) {
                        mAdapter.setItem(bean, obj);
                    }
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存请求过的数据
        NewsModel.saveNewsHistory(catalog,mAdapter.getData());
        L.i("db news num:" + GreenDaoManager.getDaoInstant().getNewsBeanDao().count());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }
}
