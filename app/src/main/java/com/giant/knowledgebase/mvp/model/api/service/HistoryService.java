package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

import static android.R.attr.type;

/**
 * Created by Jorble on 2017/8/4.
 */

public class HistoryService {

    private static final String SHARE_HISTORY_LIST_URL="action/api/share_history_list";
    private static final String COMMENT_HISTORY_LIST_URL="action/api/comment_history_list";
    private static final String RECENT_HISTORY_LIST_URL="action/api/recent_history_list";
    private static final String LIKE_HISTORY_LIST_URL="action/api/like_history_list";

    /**
     * 分享历史
     */
    public static Request getShareHistoryList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(SHARE_HISTORY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    /**
     * 评论历史
     */
    public static Request getCommentHistoryList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(COMMENT_HISTORY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    /**
     * 点赞历史
     */
    public static Request getLikeHistoryList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(LIKE_HISTORY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    /**
     * 最近历史
     */
    public static Request getRecentHistoryList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(RECENT_HISTORY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }
}
