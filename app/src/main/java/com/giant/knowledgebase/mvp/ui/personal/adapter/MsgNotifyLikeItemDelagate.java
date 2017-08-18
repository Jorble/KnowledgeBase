package com.giant.knowledgebase.mvp.ui.personal.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.FavorService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.MsgNotifyJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
import com.giant.knowledgebase.mvp.ui.news.adapter.NewsRvMultiAdapter;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.wx.goodview.GoodView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.EventBus;

import static com.giant.knowledgebase.app.constant.EventBusTags.NEWS_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.REPLACE;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QZONE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WXCIRCLE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WX_ACTION;

/**
 * Created by zhy on 16/6/22.
 */
public class MsgNotifyLikeItemDelagate implements ItemViewDelegate<MsgNotifyJson.MsgNotifyListBean> {

    private Context context;

    public MsgNotifyLikeItemDelagate(Context mcontex) {
        this.context = mcontex;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_notify_like;
    }

    @Override
    public boolean isForViewType(MsgNotifyJson.MsgNotifyListBean item, int position) {
        // [0-点赞|1-评论]
        return item.getType() == 0;
    }

    @Override
    public void convert(ViewHolder holder, MsgNotifyJson.MsgNotifyListBean model, int position) {
        holder
                .setText(R.id.name_tv, model.getAuthor())
                .setText(R.id.pubDate_tv, String.valueOf(model.getPubDate()))
                .setText(R.id.myComment, String.valueOf(model.getMyComment()));

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
                .into((ImageView) holder.getView(R.id.pic_iv)); //显示在哪个控件中
    }
}
