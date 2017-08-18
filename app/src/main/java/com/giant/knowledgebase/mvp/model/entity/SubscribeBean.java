package com.giant.knowledgebase.mvp.model.entity;

/**
 * Created by Jorble on 2017/7/3.
 */

public class SubscribeBean {
    /**
     * name : 中国新闻网
     * id : 9688
     * desc : 发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点
     * count : 12321
     * picUrl : http://oschina.org/code/snippet324.jpg
     */

    private String name;
    private String id;
    private String desc;
    private int count;
    private String picUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
