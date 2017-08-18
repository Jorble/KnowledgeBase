package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.id;
import static android.R.attr.type;

/**
 * Created by Jorble on 2017/5/31.
 */

public class UserService {

    private static final String LOGIN_URL="action/api/login";
    private static final String LOGOUT_URL="action/api/logout";
    private static final String REGISTER_URL="action/api/register";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getLogin(String account,String pwd){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(LOGIN_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("account",account)
                .putParams("pwd",pwd);

        return request;
    }

    public static Request getLogout(String uid){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(LOGOUT_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("uid",uid);

        return request;
    }

    public static Request getRegister(String account,String pwd){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(REGISTER_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("account",account)
                .putParams("pwd",pwd);

        return request;
    }

}
