package com.giant.knowledgebase.mvp.ui.search.fragment;

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
import com.giant.knowledgebase.mvp.model.api.service.SearchService;
import com.giant.knowledgebase.mvp.model.entity.SearchJson;
import com.giant.knowledgebase.mvp.model.entity.SearchJson;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.search.adapter.SearchResultMultiAdapter;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.EventBusTags.SEARCH_RESULT_FRAGMENT;
import static com.giant.knowledgebase.app.constant.EventBusTags.SEARCH_RESULT_REFRESH;
import static com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity.mLastQuery;

/**
 * Use the {@link SearchResultNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultNewsFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    Unbinder unbinder;

    private SearchResultMultiAdapter mAdapter;

    public SearchResultNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultNewsFragment newInstance() {
        SearchResultNewsFragment fragment = new SearchResultNewsFragment();
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
        mAdapter = new SearchResultMultiAdapter(getContext());
        //item点击
        mAdapter.setOnItemClickListener(new BGARvMultiItemTypeAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder,  int position)
            {
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("bean", mAdapter.getItem(position));
                intent.putExtras(mBundle);
                UiUtils.startActivity(intent);//跳转到详情页
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                return false;
            }
        });
        recylerView.setAdapter(mAdapter);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            L.i("mLastQuery="+mLastQuery);
            Request request = SearchService.getSearch(mLastQuery,1,20);
            ItheimaHttp.send(request, new HttpResponseListener<SearchJson>() {
                @Override
                public void onResponse(SearchJson jsonBean, okhttp3.Headers headers) {
                    L.i("网络加载");
                    if (refreshLayout == null) return;//访问请求过程中销毁了fragment
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;
                    else L.i("request size="+jsonBean.getNewsList().size());
                    mAdapter.setData(jsonBean.getNewsList());
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
                    if (refreshLayout == null) return;
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
                public void onResponse(SearchJson jsonBean, okhttp3.Headers headers) {
                    L.i("onResponse");
                    if (refreshLayout == null) return;
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
                    if (refreshLayout == null) return;
                    refreshLayout.endLoadingMore();
                }
            });
        }, 1000);

        return true;
    }

    /**
     * 通过eventbus post事件,远程遥控执行对应方法
     */
    @Subscriber(tag = SEARCH_RESULT_FRAGMENT, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case SEARCH_RESULT_REFRESH:
                L.i("SEARCH_RESULT_REFRESH");
                if (message.obj == null)
                    break;
                else
                    refreshLayout.beginRefreshing();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
