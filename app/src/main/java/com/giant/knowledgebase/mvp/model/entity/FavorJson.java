package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/6/9.
 */

public class FavorJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * favoriteList : [{"title":"helloworld","objid":9688,"type":1,"url":"http://oschina.org/code/snippet_253472_9688","picUrl":"http://oschina.org/code/snippet324.jpg"},{"title":"JetBrains 开发工具商业授权全面六折！","objid":26745,"type":1,"url":"http://oschina.org/news/26745","picUrl":"http://oschina.org/code/snippet324.jpg"}]
     */

    private int errCode;
    private String msg;
    private List<FavoriteListBean> favoriteList;

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

    public List<FavoriteListBean> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<FavoriteListBean> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public static class FavoriteListBean {
        /**
         * title : helloworld
         * objid : 9688
         * type : 1
         * url : http://oschina.org/code/snippet_253472_9688
         * picUrl : http://oschina.org/code/snippet324.jpg
         */

        private String title;
        private String objid;
        private int type;
        private String url;
        private String picUrl;
        private String author;
        private String pubDate;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getObjid() {
            return objid;
        }

        public void setObjid(String objid) {
            this.objid = objid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
