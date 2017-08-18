package com.giant.knowledgebase.mvp.ui.search.fragment;

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
import com.giant.knowledgebase.mvp.model.api.service.SearchService;
import com.giant.knowledgebase.mvp.model.api.service.SubscribeService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.SearchJson;
import com.giant.knowledgebase.mvp.ui.search.adapter.SearchResultWordAdapter;
import com.giant.knowledgebase.mvp.ui.subscribe.adapter.SubscribeAddRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.EventBusTags.SEARCH_RESULT_FRAGMENT;
import static com.giant.knowledgebase.app.constant.EventBusTags.SEARCH_RESULT_REFRESH;
import static com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity.mLastQuery;

/**
 * Use the {@link SearchResultWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultWordFragment extends Fragment implements BGAOnRVItemClickListener,BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    Unbinder unbinder;

    private SearchResultWordAdapter mAdapter;

    public SearchResultWordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultWordFragment newInstance() {
        SearchResultWordFragment fragment = new SearchResultWordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result_news, container, false);
        unbinder = ButterKnife.bind(this, view);

        initRefreshLayout();

        return view;
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

        refreshLayout.beginRefreshing();//预加载一次
    }

    /**
     * 初始化适配器
     */
    private void initAdapter(){
        mAdapter = new SearchResultWordAdapter(getContext(),recylerView);
        //item点击
        mAdapter.setOnRVItemClickListener(this);

        recylerView.setAdapter(mAdapter);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            L.i("mLastQuery="+mLastQuery);
            Request request = SearchService.getSearch(mLastQuery,1,20);
            ItheimaHttp.send(request, new HttpResponseListener<SearchJson>() {
                @Override
                public void onResponse(SearchJson jsonBean, Headers headers) {
                    L.i("网络加载");
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    else L.i("request size="+jsonBean.getNewsList().size());
                    mAdapter.setData(jsonBean.getWordList());
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
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.endRefreshing();
                    T.showShort(getContext(), "请求数据失败");
                }

            });
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = SearchService.getSearch(mLastQuery,1,20);
            ItheimaHttp.send(request, new HttpResponseListener<SearchJson>() {
                @Override
                public void onResponse(SearchJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getWordList());
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
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.endLoadingMore();
                }
            });
        }, 1000);

        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //进入词语详情
        T.showShort(getContext(),"进入词语详情");
    }

    /**
     * 通过eventbus post事件,远程遥控执行对应方法
     */
    @Subscriber(tag = SEARCH_RESULT_FRAGMENT, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case SEARCH_RESULT_REFRESH:
                L.i("SEARCH_RESULT_REFRESH");
                if (message.obj == null) {
                    break;
                }else {
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.beginRefreshing();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
