package com.giant.knowledgebase.mvp.model.entity;

/**
 * Created by Jorble on 2017/6/16.
 */

public class LoginJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * user : {"uid":"899**","email":"****@gmail.com","name":"彭博","gender":"男","portrait":"http://www.oschina.net/uploads/user/****","location":"广东 广州","phone":"10899132493","state":1}
     */

    private int errCode;
    private String msg;
    private UserBean user;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

}
