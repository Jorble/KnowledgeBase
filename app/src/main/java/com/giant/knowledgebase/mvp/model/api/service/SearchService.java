package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.type;


/**
 * 存放通用的一些API
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public class SearchService {

    private static final String ADVICE_URL="action/api/advice";
    private static final String SEARCH_URL="action/api/search";
    private static final String RECOMMEND_URL="action/api/recommend";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getAdvice(String q){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(ADVICE_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("q",q);

        return request;
    }

    public static Request getRecommend( ){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(RECOMMEND_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        return request;
    }

    public static Request getSearch(String q,int page,int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SEARCH_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("q",q)
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }


}
