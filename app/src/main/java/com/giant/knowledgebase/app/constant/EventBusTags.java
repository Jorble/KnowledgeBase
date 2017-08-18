package com.giant.knowledgebase.app.constant;

import static com.giant.knowledgebase.app.constant.Api.NEWS_CATALOG_RECOMMEND;

/**
 * Created by jess on 8/30/16 16:39
 * Contact with jess.yan.effort@gmail.com
 */
public class EventBusTags {

    public static final String NEWS_ACTION="NEWS_ACTION";//新闻操作
    public static final int REPLACE = 0;//使用api的catalog常量


    public static final String SHARE_ACTION="SHARE_ACTION";//分享
    public static final int SHARE_WX_ACTION = 1;
    public static final int SHARE_WXCIRCLE_ACTION = 2;
    public static final int SHARE_QQ_ACTION = 3;
    public static final int SHARE_QZONE_ACTION = 4;
    public static final int SHARE_SINA_ACTION = 5;

    public static final String LOGIN_ACTION="LOGIN_ACTION";//登录
    public static final int LOGIN_WX_ACTION = 1;
    public static final int LOGIN_QQ_ACTION = 2;
    public static final int LOGIN_SINA_ACTION = 3;

    public static final String PERSONAL_FRAGMENT="PERSONAL_FRAGMENT";//个人中心
    public static final int USER_LOAD = 1;

    public static final String SEARCH_RESULT_FRAGMENT="SEARCH_RESULT_FRAGMENT";//搜索
    public static final int SEARCH_RESULT_REFRESH = 1;

    public static final String NETWORK_ACTION="NETWORK_ACTION";//网络
    public static final int SHOW_ERROR = 1;
    public static final int HIDE_ERROR = 2;

}
