package com.giant.knowledgebase.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.giant.knowledgebase.di.module.NewsMultiModule;

import com.giant.knowledgebase.mvp.ui.news.fragment.NewsMultiFragment;

@ActivityScope
@Component(modules = NewsMultiModule.class, dependencies = AppComponent.class)
public interface NewsMultiComponent {
    void inject(NewsMultiFragment fragment);
}