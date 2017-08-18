package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

/**
 * Created by Jorble on 2017/8/4.
 */

public class AppService {

    private static final String MSG_NOTIFY_LIST_URL="action/api/msg_notify_list";
    private static final String FEED_BACK_URL="action/api/user_feedback";

    /**
     * 消息通知
     */
    public static Request getMsgNotifyList(int page, int pageSize){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(MSG_NOTIFY_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("page",page)
                .putParams("pageSize",pageSize);

        return request;
    }

    /**
     * 用户反馈
     */
    public static Request getFeedBack(String content){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(FEED_BACK_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("content",content);

        return request;
    }

}
