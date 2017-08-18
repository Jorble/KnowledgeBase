package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.type;

/**
 * Created by Jorble on 2017/5/31.
 */

public class FavorService {

    private static final String FAVOR_LIST_URL="action/api/favor_list";
    private static final String FAVOR_ADD_URL="action/api/favor_add";
    private static final String FAVOR_REMOVE_URL="action/api/favor_remove";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getFavorList(int type,int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(FAVOR_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("type",type)
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    public static Request getFavorAdd(String id,int type){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(FAVOR_ADD_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id)
                .putParams("type",type);

        return request;
    }

    public static Request getFavorRemove(String id,int type){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(FAVOR_REMOVE_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id)
                .putParams("type",type);

        return request;
    }

}
