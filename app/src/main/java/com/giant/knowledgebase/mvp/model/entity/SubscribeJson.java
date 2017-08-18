package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/6/12.
 */

public class SubscribeJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * subscribeList : [{"name":"中国新闻网","id":9688,"desc":"发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点","count":12321,"picUrl":"http://oschina.org/code/snippet324.jpg"},{"name":"人民网","id":9688,"desc":"发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点","count":12321,"picUrl":"http://oschina.org/code/snippet324.jpg"}]
     */

    private int errCode;
    private String msg;
    private List<SubscribeBean> subscribeList;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SubscribeBean> getSubscribeList() {
        return subscribeList;
    }

    public void setSubscribeList(List<SubscribeBean> subscribeList) {
        this.subscribeList = subscribeList;
    }

}
