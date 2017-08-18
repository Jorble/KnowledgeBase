package com.giant.knowledgebase.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.giant.knowledgebase.di.module.NewsDetailsModule;

import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;

@ActivityScope
@Component(modules = NewsDetailsModule.class, dependencies = AppComponent.class)
public interface NewsDetailsComponent {
    void inject(NewsDetailsActivity activity);
}