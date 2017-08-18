package com.giant.knowledgebase.mvp.ui.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.constant.Api;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.SearchService;
import com.giant.knowledgebase.mvp.model.api.service.WikiService;
import com.giant.knowledgebase.mvp.model.entity.AdviceJson;
import com.giant.knowledgebase.mvp.model.entity.AdviceSuggestion;
import com.giant.knowledgebase.mvp.model.entity.WikiWordsJson;
import com.giant.knowledgebase.mvp.ui.search.fragment.SearchResultFragment;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.giant.knowledgebase.mvp.ui.wiki.activity.WikiWordActivity;
import com.giant.knowledgebase.mvp.ui.wiki.adapter.WikiWordsRecyclerViewAdapter;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.Api.CODE_EMPTY;
import static com.giant.knowledgebase.app.constant.Api.CODE_SUCCESS;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Jorble on 2017/5/27.
 */

public class MainTabWikiFragment extends Fragment implements BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.empty_iv)
    ImageView emptyIv;
    @BindView(R.id.empty_rl)
    AutoRelativeLayout emptyRl;
    @BindView(R.id.error_iv)
    ImageView errorIv;
    @BindView(R.id.error_rl)
    AutoRelativeLayout errorRl;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private WikiWordsRecyclerViewAdapter mAdapter;

    public static MainTabWikiFragment newInstance() {
        MainTabWikiFragment fragment = new MainTabWikiFragment();
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
        View view = inflater.inflate(R.layout.fragment_main_tab_wiki, container, false);
        ButterKnife.bind(this, view);

        //搜索框
        setupFloatingSearch();
        //初始化列表
        initRefreshLayout();

        return view;
    }

    /**
     * 初始化搜索框
     */
    private void setupFloatingSearch() {
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                L.i("onSuggestionClicked()");
            }

            @Override
            public void onSearchAction(String query) {
                L.i("onSearchAction()");
                //转到百科
                jumpToWiki(query);
            }
        });
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new WikiWordsRecyclerViewAdapter(getContext(), recylerView);
        mAdapter.setOnRVItemClickListener(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);
        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        recylerView.addItemDecoration(new Divider(getContext()));

//        recylerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recylerView.setAdapter(mAdapter);

        //预加载一次
        refreshLayout.beginRefreshing();

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {

            if(mAdapter.getData().size()>0){
                refreshLayout.endRefreshing();
                T.showShort(getContext(),R.string.already_last_data);
                return;//如果已经有数据，则不再刷新
            }

            showContent();
            Request request = WikiService.getWordsList(10);
            ItheimaHttp.send(request, new HttpResponseListener<WikiWordsJson>() {
                @Override
                public void onResponse(WikiWordsJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;

                    if (CODE_SUCCESS == jsonBean.getErrCode()) {
                        mAdapter.setData(jsonBean.getWordsList());
                        showContent();
                    } else if (CODE_EMPTY == jsonBean.getErrCode()) {
                        showEmpty();
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
                    showError();
                }
            });
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //进入百科
        String word=mAdapter.getItem(position).getWord();
        jumpToWiki(word);
    }

    /**
     * 跳转到百科
     */
    private void jumpToWiki(String word){
        if(TextUtils.isEmpty(word)){
            T.showShort(getContext(),R.string.search_empty);
            return;
        }
        Intent intent=new Intent(getActivity(), WikiWordActivity.class);
        intent.putExtra("word", word);
        UiUtils.startActivity(intent);
    }

    /**
     * 显示内容
     */
    private void showContent() {
        emptyRl.setVisibility(View.GONE);
        errorRl.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示空布局
     */
    private void showEmpty() {
        emptyRl.setVisibility(View.VISIBLE);
        errorRl.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    /**
     * 显示错误布局
     */
    private void showError() {
        emptyRl.setVisibility(View.GONE);
        errorRl.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.empty_rl, R.id.error_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.empty_rl:
                refreshLayout.beginRefreshing();
                break;
            case R.id.error_rl:
                refreshLayout.beginRefreshing();
                break;
        }
    }
}