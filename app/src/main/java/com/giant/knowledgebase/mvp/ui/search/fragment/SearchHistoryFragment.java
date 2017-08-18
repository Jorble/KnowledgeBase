package com.giant.knowledgebase.mvp.ui.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.mvp.model.api.service.SearchService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.RecommendJson;
import com.giant.knowledgebase.mvp.model.entity.SearchHistoryBean;
import com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SearchHistoryFragment extends Fragment {

    @BindView(R.id.clearHistory_iv)
    ImageView clearHistoryIv;
    @BindView(R.id.history_gv)
    GridView historyGv;
    @BindView(R.id.history_ll)
    AutoLinearLayout historyLl;
    @BindView(R.id.showOrHideHot_iv)
    ImageView showOrHideHotIv;
    @BindView(R.id.hot_gv)
    GridView hotGv;
    @BindView(R.id.hot_ll)
    AutoLinearLayout hotLl;
    Unbinder unbinder;

    boolean isShowHotGv=true;//是否显示热点推荐

    public SearchHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);

        initHistoryGv();

        initHotGv();

        return view;
    }

    /**
     * 历史搜索
     */
    public void initHistoryGv(){
        if(0==GreenDaoManager.getDaoInstant().getSearchHistoryBeanDao().count()){
            historyLl.setVisibility(View.GONE);
            return;
        }

        List<SearchHistoryBean> mDatas=GreenDaoManager.getDaoInstant().getSearchHistoryBeanDao().loadAll();
        Collections.reverse(mDatas); // 倒序排列
        historyGv.setAdapter(new CommonAdapter<SearchHistoryBean>(getContext(), R.layout.item_search_history, mDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, SearchHistoryBean item, int position) {
                viewHolder.setText(R.id.history_tv,item.getHistory());

                viewHolder.setOnClickListener(R.id.item_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        L.i("item="+item.getHistory());
                        //执行SearchActivity的搜索方法
                        if(getActivity() instanceof SearchActivity){
                            ((SearchActivity) getActivity()).getFloatingSearchView().setSearchFocused(false);
                            ((SearchActivity) getActivity()).doSearch(item.getHistory());
                        }
                    }
                });
            }
            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView)
            {
                super.onViewHolderCreated(holder, itemView);
                AutoUtils.autoSize(itemView);
            }
        });
    }

    /**
     * 热点推荐搜索
     */
    private void initHotGv(){
        Request request = SearchService.getRecommend();
        ItheimaHttp.send(request, new HttpResponseListener<RecommendJson>() {
            @Override
            public void onResponse(RecommendJson jsonBean, okhttp3.Headers headers) {
                L.i("onResponse");
                if (jsonBean == null) return;

                hotGv.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_search_history, jsonBean.getHotRecommentList()) {
                    @Override
                    protected void convert(ViewHolder viewHolder, String item, int position) {
                        viewHolder.setText(R.id.history_tv,item);

                        viewHolder.setOnClickListener(R.id.item_ll, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                L.i("item="+item);
                                //执行SearchActivity的搜索方法
                                if(getActivity() instanceof SearchActivity){
                                    ((SearchActivity) getActivity()).getFloatingSearchView().setSearchFocused(false);
                                    ((SearchActivity) getActivity()).doSearch(item);
                                }
                            }
                        });
                    }
                    @Override
                    public void onViewHolderCreated(ViewHolder holder, View itemView)
                    {
                        super.onViewHolderCreated(holder, itemView);
                        AutoUtils.autoSize(itemView);
                    }
                });
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
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {// 不在最前端界面显示
//
//        } else {// 重新显示到最前端中
//            initHistoryGv();
//        }
//    }

    @OnClick({R.id.clearHistory_iv, R.id.showOrHideHot_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clearHistory_iv:
                GreenDaoManager.getDaoInstant().getSearchHistoryBeanDao().deleteAll();
                initHistoryGv();
                break;
            case R.id.showOrHideHot_iv:
                isShowHotGv=!isShowHotGv;
                if(isShowHotGv){
                    showOrHideHotIv.setImageResource(R.mipmap.icon_visible);
                    hotGv.setVisibility(View.GONE);
                }else {
                    showOrHideHotIv.setImageResource(R.mipmap.icon_invisible);
                    hotGv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

}
