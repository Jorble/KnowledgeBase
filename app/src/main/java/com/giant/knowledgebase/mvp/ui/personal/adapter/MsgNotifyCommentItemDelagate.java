package com.giant.knowledgebase.mvp.ui.personal.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.mvp.model.entity.MsgNotifyJson;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsRvMultiAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/6/22.
 */
public class MsgNotifyCommentItemDelagate implements ItemViewDelegate<MsgNotifyJson.MsgNotifyListBean> {

    private Context context;

    public MsgNotifyCommentItemDelagate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_notify_comment;
    }

    @Override
    public boolean isForViewType(MsgNotifyJson.MsgNotifyListBean item, int position) {
        // [0-点赞|1-评论]
        return item.getType() == 1;
    }

    @Override
    public void convert(ViewHolder holder, MsgNotifyJson.MsgNotifyListBean model, int position) {
        holder
                .setText(R.id.name_tv, model.getAuthor())
                .setText(R.id.pubDate_tv,model.getPubDate())
                .setText(R.id.hisComment_tv, model.getHisComment())
                .setText(R.id.myComment, model.getMyComment());

        Glide
                .with(context) //上下文
                .load(model.getAvatar()) //图片地址
                .transform(new GlideCircleTransform(context))//转换成圆形图片
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
//                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into((ImageView)holder.getView(R.id.pic_iv)); //显示在哪个控件中
    }
}
