package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;


/**
 * Created by Jorble on 2017/5/31.
 */

public class NewsService {

    private static final String NEWS_LIST_URL="action/api/newsList";
    private static final String NEWS_DETAIL_URL="action/api/news_detail";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getNewsList(int catalog, int size){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(NEWS_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("catalog",catalog)
                .putParams("size",size);

        return request;
    }

    public static Request getNewsDetail(String id){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(NEWS_DETAIL_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id);

        return request;
    }

}
