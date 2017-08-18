package com.giant.knowledgebase.mvp.model;

import android.app.Application;

import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.SearchHistoryBean;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.giant.knowledgebase.mvp.contract.SearchContract;

import java.util.List;

import static com.giant.knowledgebase.mvp.model.db.GreenDaoManager.getDaoInstant;


@ActivityScope
public class SearchModel extends BaseModel implements SearchContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SearchModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     * 清空数据
     */
    public static void clearHistory(){
        getDaoInstant().getSearchHistoryBeanDao().deleteAll();
    }

    /**
     * 保存到历史记录到数据库
     * @param mLastQuery
     */
    public static void saveSearchHistory(String mLastQuery){
        //限制最多保存20条历史数据，超出则清除最早一条
        if(getDaoInstant().getSearchHistoryBeanDao().count()>=20){
            List<SearchHistoryBean> list= getDaoInstant().getSearchHistoryBeanDao().loadAll();
            getDaoInstant().getSearchHistoryBeanDao().delete(list.get(0));
        }

        SearchHistoryBean historyBean=new SearchHistoryBean();
        historyBean.setHistory(mLastQuery);
        getDaoInstant().getSearchHistoryBeanDao().insert(historyBean);
    }

}