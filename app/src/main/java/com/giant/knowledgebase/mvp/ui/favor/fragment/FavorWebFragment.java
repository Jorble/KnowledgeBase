package com.giant.knowledgebase.mvp.ui.favor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zhouwei.library.CustomPopWindow;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.PopupWindowUtil;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.FavorService;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.FavorJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.favor.adapter.FavorWebRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.Api.CODE_EMPTY;
import static com.giant.knowledgebase.app.constant.Api.CODE_SUCCESS;


public class FavorWebFragment extends Fragment implements BGAOnRVItemLongClickListener, BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.empty_iv)
    ImageView emptyIv;
    @BindView(R.id.empty_rl)
    AutoRelativeLayout emptyRl;
    @BindView(R.id.error_iv)
    ImageView errorIv;
    @BindView(R.id.error_rl)
    AutoRelativeLayout errorRl;

    private FavorWebRecyclerViewAdapter mAdapter;
    CustomPopWindow favorMenuPopWindow;

    public static FavorWebFragment newInstance() {
        FavorWebFragment fragment = new FavorWebFragment();
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
        View view = inflater.inflate(R.layout.fragment_favor_news_list, container, false);
        ButterKnife.bind(this, view);

        //初始化列表
        initRefreshLayout();

        return view;
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new FavorWebRecyclerViewAdapter(getContext(), recylerView);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);

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
        showContent();
        new Handler().postDelayed(() -> {
            Request request = FavorService.getFavorList(1, 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<FavorJson>() {
                @Override
                public void onResponse(FavorJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;

                    if (CODE_SUCCESS == jsonBean.getErrCode()) {
                        mAdapter.setData(jsonBean.getFavoriteList());
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
            Request request = FavorService.getFavorList(1, 1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<FavorJson>() {
                @Override
                public void onResponse(FavorJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endLoadingMore();
                    if (jsonBean == null) return;
                    mAdapter.addMoreData(jsonBean.getFavoriteList());
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
                    T.showShort(getContext(), "加载失败");
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

                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
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
                T.showShort(getContext(), "获取详情失败");
            }

        });

    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_favor, null);
        //处理popWindow 显示内容
        handleFavorMenuView(contentView, mAdapter.getItem(position));
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(itemView, contentView);
        int xOff = 60; // 可以自己调整偏移(离屏幕右边距离)
        windowPos[0] -= xOff;
        favorMenuPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwindow_alpha_anim_style)
                .create()
                .showAtLocation(itemView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        return true;
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleFavorMenuView(View contentView, FavorJson.FavoriteListBean bean) {
        View.OnClickListener listener = v -> {
            if (favorMenuPopWindow != null) {
                favorMenuPopWindow.dissmiss();
            }
            switch (v.getId()) {
                case R.id.favor_remove_menu_tv:
                    mAdapter.removeItem(bean);

                    Request request = FavorService.getFavorRemove(bean.getObjid(), 1);
                    ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                        @Override
                        public void onResponse(BaseJson json, Headers headers) {
                            T.showShort(getContext(), "取消成功！");
                        }
                    });

                    break;
                case R.id.favor_top_menu_tv:
                    mAdapter.removeItem(bean);
                    mAdapter.addFirstItem(bean);
                    break;
            }
        };
        contentView.findViewById(R.id.favor_remove_menu_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.favor_top_menu_tv).setOnClickListener(listener);
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