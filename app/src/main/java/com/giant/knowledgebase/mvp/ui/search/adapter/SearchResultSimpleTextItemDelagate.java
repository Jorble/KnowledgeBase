package com.giant.knowledgebase.mvp.ui.search.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.wx.goodview.GoodView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/6/22.
 */
public class SearchResultSimpleTextItemDelagate implements ItemViewDelegate<NewsBean>
{

    private Context context;

    public SearchResultSimpleTextItemDelagate(Context context){
        this.context=context;
    }

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_search_result_simpletext;
    }

    @Override
    public boolean isForViewType(NewsBean item, int position)
    {
        // [0-纯文本|1-单图文|2-三图文|3-左图]
        return item.getType()==0;
    }

    @Override
    public void convert(ViewHolder holder, NewsBean model, int position)
    {
        holder
                .setText(R.id.title_tv, model.getTitle())
                .setText(R.id.desc_tv, model.getDesc());

        // not underlined
        Link link = new Link(SearchActivity.mLastQuery);
        link.setUnderlined(false);
        link.setTextColor(Color.parseColor("#FF0000"));

        // Add the links and make the links clickable
        LinkBuilder.on(holder.getView(R.id.title_tv))
                .addLink(link)
                .build();
    }
}
