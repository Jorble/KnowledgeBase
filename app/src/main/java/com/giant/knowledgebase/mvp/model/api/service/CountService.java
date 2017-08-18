package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * 存放通用的一些API
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public class CountService {

    private static final String COUNT_URL="action/api/count";

    /**
     * type [0-点赞|1-点踩|2-评论|3-转发]
     */
    private static Request getCount(String newsId,int type){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(COUNT_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("newsId",newsId)
                .putParams("type",type);

        return request;
    }

    /**
     * 点赞计数
     * @param newsId
     */
    public static void sendLike(String newsId){
        ItheimaHttp.send(getCount(newsId,0),null);
    }

    /**
     * 点踩计数
     * @param newsId
     */
    public static void sendDislike(String newsId){
        ItheimaHttp.send(getCount(newsId,1),null);
    }

    /**
     * 评论计数
     * @param newsId
     */
    public static void sendComment(String newsId){
        ItheimaHttp.send(getCount(newsId,2),null);
    }

    /**
     * 转发计数
     * @param newsId
     */
    public static void sendShare(String newsId){
        ItheimaHttp.send(getCount(newsId,3),null);
    }
}
