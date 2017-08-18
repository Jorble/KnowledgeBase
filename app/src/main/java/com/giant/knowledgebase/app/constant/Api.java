package com.giant.knowledgebase.app.constant;

import com.giant.knowledgebase.app.GlobalConfiguration;
import com.giant.knowledgebase.app.utils.ACache;

import static com.umeng.a.g;

/**
 * Created by jess on 8/5/16 11:25
 * contact with jess.yan.effort@gmail.com
 * <p>
 * 在interface里面的变量都是public static final 的。所以你可以这样写：
 * public static final int i=10;
 * 或则
 * int i=10；（可以省略掉一部分）
 * 注意在声明的时候要给变量赋予初值
 */
public interface Api {

    //主域名
    String APP_DOMAIN_KEY = "APP_DOMAIN_KEY";
    String APP_DOMAIN = "http://192.168.100.173:8080/KnowledgeBase/";//192.168.100.187

    //检查更新
    String CHECK_UPDATE_URL = "action/api/update_check";

    //百科
    String WIKI_URL = "https://baike.baidu.com/item/";

    /**
     * 请求参数
     */
    int NEWS_CATALOG_RECOMMEND = 1;
    int NEWS_CATALOG_HOT = 2;
    int NEWS_CATALOG_LAST = 3;
    int NEWS_CATALOG_TECH = 4;
    int NEWS_CATALOG_MILITARY = 5;
    int NEWS_CATALOG_SOCIAL = 6;
    int NEWS_CATALOG_RECOMMEND_TOP = 100;

    int CODE_SUCCESS = 200;
    int CODE_EMPTY = 500;
    int CODE_NO_MORE = 501;
}
