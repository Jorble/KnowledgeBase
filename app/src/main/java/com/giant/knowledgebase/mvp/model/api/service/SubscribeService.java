package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.type;

/**
 * Created by Jorble on 2017/5/31.
 */

public class SubscribeService {

    private static final String SUBSCRIBE_LIST_URL="action/api/subscribe_list";
    private static final String SUBSCRIBE_ADVICE_LIST_URL="action/api/subscribe_advice_list";
    private static final String SUBSCRIBE_ADD_URL="action/api/subscribe_add";
    private static final String SUBSCRIBE_REMOVE_URL="action/api/subscribe_remove";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getSubscribeList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SUBSCRIBE_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    public static Request getSubscribeAdviceList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SUBSCRIBE_ADVICE_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    public static Request getSubscribeAdd(String id){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SUBSCRIBE_ADD_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id);

        return request;
    }

    public static Request getSubscribeRemove(String id){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SUBSCRIBE_REMOVE_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id);

        return request;
    }

}
