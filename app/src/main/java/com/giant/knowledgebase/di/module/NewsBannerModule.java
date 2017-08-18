package com.giant.knowledgebase.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.giant.knowledgebase.mvp.contract.NewsBannerContract;
import com.giant.knowledgebase.mvp.model.NewsBannerModel;


@Module
public class NewsBannerModule {
    private NewsBannerContract.View view;

    /**
     * 构建NewsBannerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public NewsBannerModule(NewsBannerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewsBannerContract.View provideNewsBannerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewsBannerContract.Model provideNewsBannerModel(NewsBannerModel model) {
        return model;
    }
}