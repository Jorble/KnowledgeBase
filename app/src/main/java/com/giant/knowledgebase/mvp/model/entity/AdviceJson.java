package com.giant.knowledgebase.mvp.model.entity;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.List;

/**
 * Created by Jorble on 2017/6/12.
 */

public class AdviceJson  {

    /**
     * errCode : 200
     * msg : 请求成功
     * adviceList : ["一带一路的含义","一带一路峰会","一带一路高峰论坛","一带一路战略","一带一路战略思考","一带一路的必然性"]
     */

    private int errCode;
    private String msg;
    private List<String> adviceList;

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

    public List<String> getAdviceList() {
        return adviceList;
    }

    public void setAdviceList(List<String> adviceList) {
        this.adviceList = adviceList;
    }

}
