package com.giant.knowledgebase.mvp.ui.news.adapter;

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

import com.example.zhouwei.library.CustomPopWindow;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.FavorService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.ui.news.activity.NewsDetailsActivity;
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
public class NewsSimpleTextItemDelagate implements ItemViewDelegate<NewsBean> {
    private Context context;
    private NewsRvMultiAdapter mAdapter;
    private CustomPopWindow sharePopWindow;

    public NewsSimpleTextItemDelagate(Context context, NewsRvMultiAdapter mAdapter) {
        this.context = context;
        this.mAdapter = mAdapter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_news_simpletext;
    }

    @Override
    public boolean isForViewType(NewsBean item, int position) {
        // [0-纯文本|1-单图文|2-三图文|3-左图]
        return item.getType() == 0;
    }

    @Override
    public void convert(ViewHolder holder, NewsBean model, int position) {
        holder
                .setText(R.id.title_tv, model.getTitle())
                .setText(R.id.desc_tv, model.getDesc())
                .setText(R.id.shareCount_tv, String.valueOf(model.getShareCount()))
                .setText(R.id.commentCount_tv, String.valueOf(model.getCommentCount()))
                .setText(R.id.likeCount_tv, String.valueOf(model.getLikeCount()))
                .setText(R.id.dislikeCount_tv, String.valueOf(model.getDislikeCount()));

        //0-无 1-赞 2-踩
        if (1 == model.getIsLike()) {
            holder
                    .setTextColorRes(R.id.likeCount_tv, R.color.theme_color)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_click)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_normal);
        } else if (2 == model.getIsLike()) {
            holder
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.theme_color)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_click);
        } else {
            holder
                    .setTextColorRes(R.id.likeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.like_ib, R.mipmap.icon_action_like_normal)
                    .setTextColorRes(R.id.dislikeCount_tv, R.color.text_black3)
                    .setBackgroundRes(R.id.dislike_ib, R.mipmap.icon_action_dislike_normal);
        }

        holder
                .setOnClickListener(R.id.like_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞
                        if (1 == model.getIsLike()) {
                            T.showShort(context, "你已经点过赞了");
                        } else if (2 == model.getIsLike()) {
                            T.showShort(context, "你已经点过踩了");
                        } else {
                            model.setLikeCount(model.getLikeCount() + 1);//赞+1
                            model.setIsLike(1);//点赞
                            //点赞动画
                            GoodView mGoodView = new GoodView(context);
                            mGoodView.setTextInfo("+1", context.getResources().getColor(R.color.theme_color), 12);//+1动画
                            mGoodView.show(v);
                            //更新adapter列表数据
                            mAdapter.setItem(position, model);//替换旧数据
                            mAdapter.notifyItemChanged(position);//刷新界面
                        }
                    }
                })
                .setOnClickListener(R.id.dislike_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点踩
                        if (1 == model.getIsLike()) {
                            T.showShort(context, "你已经点过赞了");
                        } else if (2 == model.getIsLike()) {
                            T.showShort(context, "你已经点过踩了");
                        } else {
                            model.setDislikeCount(model.getDislikeCount() + 1);//踩+1
                            model.setIsLike(2);//点踩
                            //点赞动画
                            GoodView mGoodView = new GoodView(context);
                            mGoodView.setTextInfo("+1", context.getResources().getColor(R.color.theme_color), 12);//+1动画
                            mGoodView.show(v);
                            //更新adapter列表数据
                            mAdapter.setItem(position, model);//替换旧数据
                            mAdapter.notifyItemChanged(position);//刷新界面
                        }
                    }
                })
                .setOnClickListener(R.id.comment_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //评论
                        Intent intent = new Intent(context, NewsDetailsActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("bean", model);
                        mBundle.putBoolean("isSmoothComment",true);//是否滚动评论区
                        intent.putExtras(mBundle);
                        context.startActivity(intent);//跳转到详情页
                    }
                })
                .setOnClickListener(R.id.share_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享
                        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
                        //处理popWindow 显示内容
                        handleShareLogic(contentView, model);
                        sharePopWindow = new CustomPopWindow.PopupWindowBuilder(context)
                                .setView(contentView)
                                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                                .setFocusable(true)
                                .setOutsideTouchable(true)
                                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                                .setBgDarkAlpha(0.7f) // 控制亮度
                                .setAnimationStyle(R.style.popwindow_bottom_anim_style)
                                .create()
                                .showAtLocation(holder.getConvertView(), Gravity.BOTTOM, 0, 0);
                    }
                });
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleShareLogic(View contentView, NewsBean bean) {

        //显示
        if (1 == bean.getIsSubscribe()) {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.subscribe_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.subscribe_tv);
            imageView.setImageResource(R.mipmap.icon_action_subscribe_choose);
            textView.setText("取消订阅");
        } else {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.subscribe_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.subscribe_tv);
            imageView.setImageResource(R.mipmap.icon_action_subscribe);
            textView.setText("订阅");
        }

        if (1 == bean.getIsFavor()) {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.favor_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.favor_tv);
            imageView.setImageResource(R.mipmap.icon_action_favor_choose);
            textView.setText("取消收藏");
        } else {
            ImageView imageView = (ImageView) contentView.findViewById(R.id.favor_iv);
            TextView textView = (TextView) contentView.findViewById(R.id.favor_tv);
            imageView.setImageResource(R.mipmap.icon_action_favor);
            textView.setText("收藏");
        }

        //点击事件
        View.OnClickListener listener = v -> {
            if (sharePopWindow != null) {
                sharePopWindow.dissmiss();
            }

            Message message = new Message();
            switch (v.getId()) {
                case R.id.wx_share_ll:
                    message.what = SHARE_WX_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.wxCircle_share_ll:
                    message.what = SHARE_WXCIRCLE_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.qq_share_ll:
                    message.what = SHARE_QQ_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.qZone_share_ll:
                    message.what = SHARE_QZONE_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;
                case R.id.wb_share_ll:
                    message.what = SHARE_SINA_ACTION;
                    message.obj = bean;
                    EventBus.getDefault().post(message, SHARE_ACTION);
                    break;

                case R.id.tipoff_ll:
                    //提交请求..
                    break;
                case R.id.copyLink_ll:
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(bean.getUrl());
                    T.showShort(context, "已复制");
                    break;
                case R.id.subscribe_ll:
                    //提交请求..
                    break;
                case R.id.favor_ll:
                    if (0 == bean.getIsFavor()) {

                        //更新adapter列表数据
                        bean.setIsFavor(1);
                        message.what = REPLACE;
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorAdd(bean.getId(), 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, okhttp3.Headers headers) {
                                T.showShort(context, "收藏成功！");
                            }
                        });
                    } else {
                        //更新adapter列表数据
                        bean.setIsFavor(0);
                        message.what = REPLACE;
                        message.obj = bean;
                        EventBus.getDefault().post(message, NEWS_ACTION);

                        Request request = FavorService.getFavorRemove(bean.getId(), 1);
                        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                            @Override
                            public void onResponse(BaseJson json, okhttp3.Headers headers) {
                                T.showShort(context, "取消成功！");
                            }
                        });
                    }
                    break;
                case R.id.cancel:
                    sharePopWindow.dissmiss();
                    break;
            }
        };
        contentView.findViewById(R.id.wx_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.wxCircle_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.qq_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.qZone_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.wb_share_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.tipoff_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.copyLink_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.subscribe_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.favor_ll).setOnClickListener(listener);
        contentView.findViewById(R.id.cancel).setOnClickListener(listener);
    }
}
