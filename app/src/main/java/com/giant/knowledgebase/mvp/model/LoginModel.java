package com.giant.knowledgebase.mvp.model;

import android.app.Application;

import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.UserBean;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.giant.knowledgebase.mvp.contract.LoginContract;


@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
     * 是否已登录
     * @return
     */
    public static boolean isLogin(){
        return GreenDaoManager.getDaoInstant().getUserBeanDao().count()>0;
    }

    /**
     * 获取用户
     * @return
     */
    public static UserBean getUser(){
        if(isLogin()) {
            return GreenDaoManager.getDaoInstant().getUserBeanDao().loadAll().get(0);
        }else {
            return null;
        }
    }
}