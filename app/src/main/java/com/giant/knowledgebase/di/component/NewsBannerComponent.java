package com.giant.knowledgebase.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.giant.knowledgebase.di.module.NewsBannerModule;

import com.giant.knowledgebase.mvp.ui.news.fragment.NewsBannerFragment;

@ActivityScope
@Component(modules = NewsBannerModule.class, dependencies = AppComponent.class)
public interface NewsBannerComponent {
    void inject(NewsBannerFragment fragment);
}