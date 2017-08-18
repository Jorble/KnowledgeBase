package com.giant.knowledgebase.mvp.ui.news.adapter;

import android.content.Context;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.widget.BGARvMultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Jorble on 2017/6/2.
 */

public class NewsRvMultiAdapter extends BGARvMultiItemTypeAdapter<NewsBean>
{
public NewsRvMultiAdapter(Context context) {
        super(context);

        addItemViewDelegate(new NewsSimpleTextItemDelagate(context,this));//纯文本
        addItemViewDelegate(new NewsSinglePicItemDelagate(context,this));//单图
        addItemViewDelegate(new NewsThreePicItemDelagate(context,this));//三图
        addItemViewDelegate(new NewsLeftPicItemDelagate(context,this));//左图
        }
}