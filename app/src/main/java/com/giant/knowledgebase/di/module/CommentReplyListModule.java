package com.giant.knowledgebase.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.giant.knowledgebase.mvp.contract.CommentReplyListContract;
import com.giant.knowledgebase.mvp.model.CommentReplyListModel;


@Module
public class CommentReplyListModule {
    private CommentReplyListContract.View view;

    /**
     * 构建CommentReplyListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CommentReplyListModule(CommentReplyListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CommentReplyListContract.View provideCommentReplyListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CommentReplyListContract.Model provideCommentReplyListModel(CommentReplyListModel model) {
        return model;
    }
}