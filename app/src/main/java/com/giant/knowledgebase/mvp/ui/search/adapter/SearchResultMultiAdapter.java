package com.giant.knowledgebase.mvp.ui.search.adapter;

import android.content.Context;

import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;

/**
 * Created by Jorble on 2017/6/2.
 */

public class SearchResultMultiAdapter extends BGARvMultiItemTypeAdapter<NewsBean>
{
public SearchResultMultiAdapter(Context context) {
        super(context);

        addItemViewDelegate(new SearchResultSimpleTextItemDelagate(context));//纯文本
        addItemViewDelegate(new SearchResultSinglePicItemDelagate(context));//单图
        addItemViewDelegate(new SearchResultThreePicItemDelagate(context));//三图
        addItemViewDelegate(new SearchResultLeftPicItemDelagate(context));//左图
        }
}