package com.giant.knowledgebase.mvp.ui.search.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.di.component.DaggerSearchComponent;
import com.giant.knowledgebase.di.module.SearchModule;
import com.giant.knowledgebase.mvp.contract.SearchContract;
import com.giant.knowledgebase.mvp.model.SearchModel;
import com.giant.knowledgebase.mvp.model.api.service.SearchService;
import com.giant.knowledgebase.mvp.model.entity.AdviceJson;
import com.giant.knowledgebase.mvp.model.entity.AdviceSuggestion;
import com.giant.knowledgebase.mvp.presenter.SearchPresenter;
import com.giant.knowledgebase.mvp.ui.search.fragment.SearchHistoryFragment;
import com.giant.knowledgebase.mvp.ui.search.fragment.SearchResultFragment;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoFrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {


    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    FragmentManager fm;
    Fragment mCurrentFragment;
    String SEARCH_FR_TAG = "SearchHistoryFragment";
    String RESULT_FR_TAG = "SearchResultFragment";
    @BindView(R.id.contain_fl)
    AutoFrameLayout containFl;

    public static String mLastQuery = "";
    //搜索防抖
    public static final int MIN_CLICK_DELAY_TIME = 1500;
    private long lastClickTime = 0;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setupFloatingSearch();
        setDefaultFragment();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SearchActivity.mLastQuery="";//清空搜索词
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SearchHistoryFragment searchHistoryFragment = new SearchHistoryFragment();
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        /**
         * 在这里进行添加页面，设置fragment的TAG值
         */
        ft
                .add(R.id.contain_fl, searchHistoryFragment, SEARCH_FR_TAG)
                .add(R.id.contain_fl, searchResultFragment, RESULT_FR_TAG).hide(searchResultFragment)
                .commit();

    }

    /**
     * 主activity进行控制不同的fragment
     *
     * @param toTag
     */
    public void switchFragment(String fromTag,String toTag) {
        Fragment from = fm.findFragmentByTag(fromTag);
        Fragment to = fm.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {//判断是否被添加到了Activity里面去了
                transaction.hide(from).add(R.id.contain_fl, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }

    }

    /**
     * 初始化搜索框
     */
    private void setupFloatingSearch() {

        //默认打开热点搜索
        floatingSearchView.setSearchFocused(true);

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //防抖处理
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;

                    //搜索处理
                    if (!oldQuery.equals("") && newQuery.equals("")) {
                        floatingSearchView.clearSuggestions();
                    } else {

                        //this shows the top left circular progress
                        //you can call it where ever you want, but
                        //it makes sense to do it when loading something in
                        //the background.
                        floatingSearchView.showProgress();

//                    //模拟数据
//                    floatingSearchView.swapSuggestions(mPresenter.getAdviceSuggestionList());

                        //simulates a query call to a data source
                        //with a new query.
                        Request request = SearchService.getAdvice(newQuery);
                        ItheimaHttp.send(request, new HttpResponseListener<AdviceJson>() {
                            @Override
                            public void onResponse(AdviceJson jsonBean, Headers headers) {
                                L.i("onSearchTextChanged onResponse");
                                if (floatingSearchView == null) return;
                                floatingSearchView.hideProgress();
                                if (jsonBean == null) return;
                                //转换成AdviceSuggestion
                                List<AdviceSuggestion> adviceSuggestionList = new ArrayList<>();
                                for (String s : jsonBean.getAdviceList()) {
                                    adviceSuggestionList.add(new AdviceSuggestion(s));
                                }
                                //切换数据
                                floatingSearchView.swapSuggestions(adviceSuggestionList);
                            }

                            /**
                             * 可以不重写失败回调
                             * @param call
                             * @param e
                             */
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable e) {
                                L.i("onSearchTextChanged onFailure");
                                if (floatingSearchView == null) return;
                                floatingSearchView.hideProgress();
                            }

                        });

                    }
                }

            }
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                L.i("onSuggestionClicked()");

                floatingSearchView.setSearchFocused(false);//取消焦点，不再触发textchange请求

                doSearch(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String query) {
                L.i("onSearchAction()");

                doSearch(query);
            }
        });

        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                L.i("onFocus");
            }

            @Override
            public void onFocusCleared() {
                L.i("onFocusCleared");
            }
        });

        floatingSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                L.i("onClearSearchClicked");
//                switchFragment(RESULT_FR_TAG,SEARCH_FR_TAG);//切换到搜索
            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                L.i("onHomeClicked");
                if(mCurrentFragment instanceof SearchResultFragment){
                    switchFragment(RESULT_FR_TAG,SEARCH_FR_TAG);
                }else {
                    killMyself();
                }
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {

                SearchSuggestion suggestion = (SearchSuggestion) item;

                String textColor = "#787878";
                String textLight = "#FF4500";

                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_history_black_24dp, null));
                Util.setIconColor(leftIcon, Color.parseColor(textColor));
                leftIcon.setAlpha(.36f);

                textView.setTextColor(Color.parseColor(textColor));
                String text = suggestion.getBody()
                        .replaceFirst(floatingSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + floatingSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        floatingSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                containFl.setTranslationY(newHeight);
            }
        });
    }



    /**
     * 执行搜索
     * @param mLastQuery
     */
    public void doSearch(String mLastQuery){
        this.mLastQuery=mLastQuery;

        floatingSearchView.setSearchText(mLastQuery);//设置搜索框中的搜索文本
//        floatingSearchView.clearQuery();
        floatingSearchView.clearSuggestions();

        //保存历史
        SearchModel.saveSearchHistory(mLastQuery);

        //切换到结果
        switchFragment(SEARCH_FR_TAG,RESULT_FR_TAG);

        //刷新数据
        if(mCurrentFragment instanceof SearchHistoryFragment){
            ((SearchHistoryFragment) mCurrentFragment).initHistoryGv();
        }else if(mCurrentFragment instanceof SearchResultFragment){
            ((SearchResultFragment) mCurrentFragment).initTab();
        }

    }

    public FloatingSearchView getFloatingSearchView(){
        return floatingSearchView;
    }
}