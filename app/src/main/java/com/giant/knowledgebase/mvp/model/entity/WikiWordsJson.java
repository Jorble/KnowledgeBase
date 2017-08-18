package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/8/14.
 */

public class WikiWordsJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * wordsList : [{"id":26754,"word":"丝绸之路","desc":"古代连接中西方的商道","url":"https://baike.baidu.com/item/丝绸之路","pic":"https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=33cfb34ddb1373f0e13267cdc566209e/a8773912b31bb0511a95981e3c7adab44aede057.jpg"}]
     */

    private int errCode;
    private String msg;
    private List<WordsListBean> wordsList;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<WordsListBean> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<WordsListBean> wordsList) {
        this.wordsList = wordsList;
    }

    public static class WordsListBean {
        /**
         * id : 26754
         * word : 丝绸之路
         * desc : 古代连接中西方的商道
         * url : https://baike.baidu.com/item/丝绸之路
         * pic : https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=33cfb34ddb1373f0e13267cdc566209e/a8773912b31bb0511a95981e3c7adab44aede057.jpg
         */

        private String id;
        private String word;
        private String desc;
        private String url;
        private String pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
