package com.giant.knowledgebase.mvp.ui.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.mvp.model.entity.CommentJson;
import com.giant.knowledgebase.mvp.model.entity.FavorJson;
import com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * 作者:Jorble 邮件:lijiebu@gzjtit.com
 * 创建时间:17/7/3 13:43
 * 描述:
 */
public class CommentRecyclerViewAdapter extends BGARecyclerViewAdapter<CommentJson.CommentListBean> {
    Context context;

    public CommentRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_comment_list);
        this.context=context;
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
        viewHolderHelper.setItemChildClickListener(R.id.pic_iv);
        viewHolderHelper.setItemChildClickListener(R.id.like_ll);
        viewHolderHelper.setItemChildClickListener(R.id.reply_tv);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, CommentJson.CommentListBean model) {
        viewHolderHelper
                .setText(R.id.likeCount_tv,String.valueOf(model.getLikeCount()))
                .setText(R.id.time_tv,model.getPubDate())
                .setText(R.id.reply_tv,"查看"+model.getReplyCount()+"条回复>>")
                .setText(R.id.content_tv,model.getContent())
                .setText(R.id.name_tv, model.getAuthor());

        //0-无 1-赞 2-踩
        if(1==model.getIsLike()) {
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.theme_color)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_click);
        }else if(2==model.getIsLike()){
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal);
        }else {
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal);
        }

        Glide
                .with(mContext) //上下文
                .load(model.getPortrait()) //图片地址
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
//                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into(viewHolderHelper.getImageView(R.id.pic_iv)); //显示在哪个控件中

        //如果有回复则显示
        if(model.getReplyCount()>0){
            viewHolderHelper.getView(R.id.reply_tv).setVisibility(View.VISIBLE);
        }

    }
}