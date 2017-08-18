package com.giant.knowledgebase.mvp.ui.news.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

import static com.bumptech.glide.Glide.with;

/**
 * 作者:Jorble 邮件:lijiebu@gzjtit.com
 * 创建时间:17/7/3 13:43
 * 描述:
 */
public class NewsRecyclerViewAdapter extends BGARecyclerViewAdapter<NewsBean> {
    Context context;

    public NewsRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_news_singlepic);
        this.context=context;
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
        viewHolderHelper.setItemChildClickListener(R.id.share_ll);
        viewHolderHelper.setItemChildClickListener(R.id.comment_ll);
        viewHolderHelper.setItemChildClickListener(R.id.like_ll);
        viewHolderHelper.setItemChildClickListener(R.id.dislike_ll);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, NewsBean model) {
        viewHolderHelper
                .setText(R.id.title_tv, model.getTitle())
                .setText(R.id.shareCount_tv, String.valueOf(model.getShareCount()))
                .setText(R.id.commentCount_tv, String.valueOf(model.getCommentCount()))
                .setText(R.id.likeCount_tv, String.valueOf(model.getLikeCount()))
                .setText(R.id.dislikeCount_tv, String.valueOf(model.getDislikeCount()));

        //0-无 1-赞 2-踩
        if(1==model.getIsLike()) {
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.theme_color)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_click)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_normal);
        }else if(2==model.getIsLike()){
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.theme_color)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_click);
        }else {
            viewHolderHelper
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_normal);
        }

//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, mContext.getResources().getDisplayMetrics());
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, mContext.getResources().getDisplayMetrics());

        Glide
                .with(mContext) //上下文
                .load(model.getPicUrl()) //图片地址
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into(viewHolderHelper.getImageView(R.id.pic_iv)); //显示在哪个控件中

    }
}