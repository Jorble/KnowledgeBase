package com.giant.knowledgebase.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.giant.knowledgebase.di.module.CommentReplyListModule;

import com.giant.knowledgebase.mvp.ui.news.activity.CommentReplyListActivity;

@ActivityScope
@Component(modules = CommentReplyListModule.class, dependencies = AppComponent.class)
public interface CommentReplyListComponent {
    void inject(CommentReplyListActivity activity);
}