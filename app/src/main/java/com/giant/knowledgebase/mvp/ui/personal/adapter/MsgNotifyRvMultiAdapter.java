package com.giant.knowledgebase.mvp.ui.personal.adapter;

import android.content.Context;

import com.giant.knowledgebase.mvp.model.entity.MsgNotifyJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsLeftPicItemDelagate;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsSimpleTextItemDelagate;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsSinglePicItemDelagate;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsThreePicItemDelagate;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;

/**
 * Created by Jorble on 2017/6/2.
 */

public class MsgNotifyRvMultiAdapter extends BGARvMultiItemTypeAdapter<MsgNotifyJson.MsgNotifyListBean>
{
public MsgNotifyRvMultiAdapter(Context context) {
        super(context);

        addItemViewDelegate(new MsgNotifyLikeItemDelagate(context));//点赞
        addItemViewDelegate(new MsgNotifyCommentItemDelagate(context));//评论
        }
}