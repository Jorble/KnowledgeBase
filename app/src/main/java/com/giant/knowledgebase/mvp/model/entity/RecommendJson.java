package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/6/12.
 */

public class RecommendJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * hotRecommentList : ["5年就业最好专业","习近平会见美国国务卿","账户凭空多巨款"]
     * userRecommendList : ["李明哲被逮捕","一带一路","英国曼切斯特爆炸"]
     */

    private int errCode;
    private String msg;
    private List<String> hotRecommentList;

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

    public List<String> getHotRecommentList() {
        return hotRecommentList;
    }

    public void setHotRecommentList(List<String> hotRecommentList) {
        this.hotRecommentList = hotRecommentList;
    }

}
