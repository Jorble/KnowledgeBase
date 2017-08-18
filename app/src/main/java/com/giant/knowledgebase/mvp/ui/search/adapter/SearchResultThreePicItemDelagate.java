package com.giant.knowledgebase.mvp.ui.search.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/6/22.
 */
public class SearchResultThreePicItemDelagate implements ItemViewDelegate<NewsBean>
{

    private Context context;

    public SearchResultThreePicItemDelagate(Context context){
        this.context=context;
    }

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_search_result_threepic;
    }

    @Override
    public boolean isForViewType(NewsBean item, int position)
    {
        // [0-纯文本|1-单图文|2-三图文|3-左图]
        return item.getType()==2;
    }

    @Override
    public void convert(ViewHolder holder, NewsBean model, int position)
    {
        holder
                .setText(R.id.title_tv, model.getTitle());

        Glide
                .with(context) //上下文
                .load(model.getPicUrl()) //图片地址
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into((ImageView) holder.getView(R.id.pic1_iv)); //显示在哪个控件中

        Glide
                .with(context) //上下文
                .load(model.getPicUrl2()) //图片地址
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into((ImageView) holder.getView(R.id.pic2_iv)); //显示在哪个控件中

        Glide
                .with(context) //上下文
                .load(model.getPicUrl3()) //图片地址
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into((ImageView) holder.getView(R.id.pic3_iv)); //显示在哪个控件中

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
