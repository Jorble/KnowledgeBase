package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.id;
import static android.R.attr.type;


/**
 * 存放通用的一些API
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public class CommentService {

    private static final String COMMENT_LIST_URL="action/api/news_comment_list";
    private static final String COMMENT_PUB_URL="action/api/news_comment_pub";

    private static final String REPLY_LIST_URL="action/api/comment_reply_list";
    private static final String REPLY_PUB_URL="action/api/comment_reply_pub";

    /**
     * RetrofitUtil请求实例
     */
    public static Request getCommentList(String id, int page,int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(COMMENT_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id)
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    public static Request getCommentPub(String id, String content){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(COMMENT_PUB_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("id",id)
                .putParams("content",content);

        return request;
    }

    public static Request getReplyList(String newsId,String commentId, int page,int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(REPLY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("newsId",newsId)
                .putParams("commentId",commentId)
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    public static Request getReplyPub(String commentId,String replyName, String content){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(REPLY_PUB_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("commentId",commentId)
                .putParams("replyName",replyName)
                .putParams("content",content);

        return request;
    }
}
