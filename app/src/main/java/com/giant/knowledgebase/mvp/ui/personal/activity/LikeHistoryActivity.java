package com.giant.knowledgebase.mvp.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.HistoryService;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.entity.LikeHistoryJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.LikeHistoryJson;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.personal.adapter.LikeHistoryRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.personal.adapter.ShareHistoryRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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

public class LikeHistoryActivity extends AppCompatActivity implements  BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate{

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
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;

    private LikeHistoryRecyclerViewAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_history);
        ButterKnife.bind(this);
        barTitleTv.setText("点赞历史");

        //初始化列表
        initRefreshLayout();
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new LikeHistoryRecyclerViewAdapter(this, recylerView);
        mAdapter.setOnRVItemClickListener(this);

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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        showContent();
        new Handler().postDelayed(() -> {
            Request request = HistoryService.getLikeHistoryList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<LikeHistoryJson>() {
                @Override
                public void onResponse(LikeHistoryJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;

                    if (CODE_SUCCESS == jsonBean.getErrCode()) {
                        mAdapter.setData(jsonBean.getLikeHistoryList());
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
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout mrefreshLayout) {
        new Handler().postDelayed(() -> {
            Request request = HistoryService.getLikeHistoryList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<LikeHistoryJson>() {
                @Override
                public void onResponse(LikeHistoryJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getLikeHistoryList());
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
                    T.showShort(LikeHistoryActivity.this, "加载失败");
                }

            });
        }, 1000);

        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        L.i("onRVItemClick");
        //根据id获取具体信息
        Request request = NewsService.getNewsDetail(mAdapter.getItem(position).getObjid());
        ItheimaHttp.send(request, new HttpResponseListener<NewsBean>() {
            @Override
            public void onResponse(NewsBean jsonBean, Headers headers) {
                L.i("onResponse");
                if (jsonBean == null) return;

                Intent intent = new Intent(LikeHistoryActivity.this, NewsDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("bean", jsonBean);
                intent.putExtras(mBundle);
                UiUtils.startActivity(intent);//跳转到详情页
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
                T.showShort(LikeHistoryActivity.this, "获取详情失败");
            }

        });

    }

    @OnClick({R.id.bar_back_iv,R.id.empty_rl, R.id.error_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_back_iv:
                finish();
            case R.id.empty_rl:
                refreshLayout.beginRefreshing();
                break;
            case R.id.error_rl:
                refreshLayout.beginRefreshing();
                break;
        }
    }

    /**
     * 显示内容
     */
    private void showContent(){
        emptyRl.setVisibility(View.GONE);
        errorRl.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示空布局
     */
    private void showEmpty(){
        emptyRl.setVisibility(View.VISIBLE);
        errorRl.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    /**
     * 显示错误布局
     */
    private void showError(){
        emptyRl.setVisibility(View.GONE);
        errorRl.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }
}
