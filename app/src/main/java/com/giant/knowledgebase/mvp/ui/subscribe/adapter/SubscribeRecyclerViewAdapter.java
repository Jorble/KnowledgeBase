package com.giant.knowledgebase.mvp.ui.subscribe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.mvp.model.entity.FavorJson;
import com.giant.knowledgebase.mvp.model.entity.SubscribeBean;
import com.giant.knowledgebase.mvp.model.entity.SubscribeJson;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * 作者:Jorble 邮件:lijiebu@gzjtit.com
 * 创建时间:17/7/3 13:43
 * 描述:
 */
public class SubscribeRecyclerViewAdapter extends BGARecyclerViewAdapter<SubscribeBean> {
    Context context;

    public SubscribeRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_subscribe_list);
        this.context=context;
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
        viewHolderHelper.setItemChildClickListener(R.id.remove_bt);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, SubscribeBean model) {
        viewHolderHelper
                .setText(R.id.remove_bt, "取消订阅")
                .setText(R.id.count_tv, String.valueOf(model.getCount())+"人订阅")
                .setText(R.id.name_tv, model.getName());

        Glide
                .with(mContext) //上下文
                .load(model.getPicUrl()) //图片地址
                .transform(new GlideCircleTransform(mContext))//转换成圆形图片
                .placeholder(R.mipmap.pic_placeholder)//占位图
//                .error(R.mipmap.ic_launcher) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
//                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into(viewHolderHelper.getImageView(R.id.pic_iv)); //显示在哪个控件中

    }
}