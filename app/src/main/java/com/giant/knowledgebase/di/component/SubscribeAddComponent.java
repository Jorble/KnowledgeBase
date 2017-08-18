package com.giant.knowledgebase.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.giant.knowledgebase.di.module.SubscribeAddModule;

import com.giant.knowledgebase.mvp.ui.subscribe.activity.SubscribeAddActivity;

@ActivityScope
@Component(modules = SubscribeAddModule.class, dependencies = AppComponent.class)
public interface SubscribeAddComponent {
    void inject(SubscribeAddActivity activity);
}