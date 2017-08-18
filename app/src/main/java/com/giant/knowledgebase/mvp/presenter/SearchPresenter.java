package com.giant.knowledgebase.mvp.presenter;

import android.app.Application;

import com.giant.knowledgebase.mvp.model.entity.AdviceSuggestion;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.giant.knowledgebase.mvp.contract.SearchContract;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class SearchPresenter extends BasePresenter<SearchContract.Model, SearchContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SearchPresenter(SearchContract.Model model, SearchContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 返回搜索建议列表
     * @return
     */
    public List<AdviceSuggestion> getAdviceSuggestionList(){
        List<AdviceSuggestion> adviceSuggestionList=new ArrayList<>();
        for (int i=0;i<6;i++){
            adviceSuggestionList.add(new AdviceSuggestion("advice "+i));
        }
        return adviceSuggestionList;
    }
}