package com.giant.knowledgebase.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.giant.knowledgebase.mvp.contract.SubscribeAddContract;
import com.giant.knowledgebase.mvp.model.SubscribeAddModel;


@Module
public class SubscribeAddModule {
    private SubscribeAddContract.View view;

    /**
     * 构建SubscribeAddModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SubscribeAddModule(SubscribeAddContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SubscribeAddContract.View provideSubscribeAddView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SubscribeAddContract.Model provideSubscribeAddModel(SubscribeAddModel model) {
        return model;
    }
}