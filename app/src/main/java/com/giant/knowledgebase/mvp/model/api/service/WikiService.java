package com.giant.knowledgebase.mvp.model.api.service;

import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;

/**
 * Created by Jorble on 2017/8/4.
 */

public class WikiService {

    private static final String WIKI_WORDS_LIST_URL="action/api/wiki_words_list";

    /**
     * 热门词条
     */
    public static Request getWordsList(int size){

        //ItheimaHttp.newPostRequest(apiUrl)
        Request request = ItheimaHttp.newGetRequest(WIKI_WORDS_LIST_URL);//apiUrl格式："xxx/xxxxx"
        //添加请求参数
        request
                .putParams("size",size);
        return request;
    }

}
