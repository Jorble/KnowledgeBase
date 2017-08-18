package com.giant.knowledgebase.mvp.ui.subscribe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.SubscribeService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.SubscribeJson;
import com.giant.knowledgebase.mvp.ui.subscribe.activity.SubscribeAddActivity;
import com.giant.knowledgebase.mvp.ui.subscribe.adapter.SubscribeRecyclerViewAdapter;
import com.giant.knowledgebase.mvp.ui.widget.Divider;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

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

public class MainTabSubscribeFragment extends Fragment implements BGAOnRVItemClickListener, BGAOnItemChildClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.subscribe_add_bt)
    Button subscribeAddBt;
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

    private SubscribeRecyclerViewAdapter mAdapter;

    public static MainTabSubscribeFragment newInstance() {
        MainTabSubscribeFragment fragment = new MainTabSubscribeFragment();
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
        View view = inflater.inflate(R.layout.fragment_main_tab_subscribe, container, false);
        ButterKnife.bind(this, view);

        //初始化列表
        initRefreshLayout();

        return view;
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        mAdapter = new SubscribeRecyclerViewAdapter(getContext(), recylerView);
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

        //预加载一次
        refreshLayout.beginRefreshing();

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        showContent();
        new Handler().postDelayed(() -> {
            Request request = SubscribeService.getSubscribeList(1, 20);
            ItheimaHttp.send(request, new HttpResponseListener<SubscribeJson>() {
                @Override
                public void onResponse(SubscribeJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    refreshLayout.endRefreshing();
                    if (jsonBean == null) return;

                    if (CODE_SUCCESS == jsonBean.getErrCode()) {
                        mAdapter.setData(jsonBean.getSubscribeList());
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
            Request request = SubscribeService.getSubscribeList(1, 20);
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
                    T.showShort(getContext(), "加载失败");
                }

            });
        }, 1000);

        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //进入订阅号详情

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.remove_bt) {
            //提交请求，取消订阅
            Request request = SubscribeService.getSubscribeRemove(mAdapter.getItem(position).getId());
            ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                @Override
                public void onResponse(BaseJson jsonBean, Headers headers) {
                    L.i("onResponse");
                    if (jsonBean == null) return;
                    if (jsonBean.isSuccess()) {
                        //列表中移除
                        mAdapter.removeItem(position);
                        T.showShort(getContext(), "取消成功");
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
                    T.showShort(getContext(), "取消失败");
                }

            });
        }
    }

    @OnClick({R.id.subscribe_add_bt, R.id.empty_rl,R.id.error_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.subscribe_add_bt:
                UiUtils.startActivity(SubscribeAddActivity.class);
                break;
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

}