package com.giant.knowledgebase.mvp.model.entity;

import static android.R.attr.id;

/**
 * Created by Jorble on 2017/6/9.
 */

public class BaseJson {

    /**
     * errCode : 200
     * msg : 请求成功
     */

    private int errCode;
    private String msg;

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

    public boolean isSuccess(){
        return 200==errCode;
    }
}
