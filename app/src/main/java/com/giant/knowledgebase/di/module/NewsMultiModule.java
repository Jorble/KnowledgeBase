package com.giant.knowledgebase.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.giant.knowledgebase.mvp.contract.NewsMultiContract;
import com.giant.knowledgebase.mvp.model.NewsMultiModel;


@Module
public class NewsMultiModule {
    private NewsMultiContract.View view;

    /**
     * 构建NewsMultiModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public NewsMultiModule(NewsMultiContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewsMultiContract.View provideNewsMultiView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewsMultiContract.Model provideNewsMultiModel(NewsMultiModel model) {
        return model;
    }
}