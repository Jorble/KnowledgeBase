package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/8/3.
 */

public class CommentHistoryJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * commentHistoryList : [{"title":"helloworld","objid":9688,"comment":"这篇文章写得很好","url":"http://oschina.org/code/snippet_253472_9688","picUrl":"http://oschina.org/code/snippet324.jpg"},{"title":"JetBrains 开发工具商业授权全面六折！","objid":26745,"comment":"这篇文章写得很好！","url":"http://oschina.org/news/26745","picUrl":"http://oschina.org/code/snippet324.jpg"}]
     */

    private int errCode;
    private String msg;
    private List<CommentHistoryListBean> commentHistoryList;

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

    public List<CommentHistoryListBean> getCommentHistoryList() {
        return commentHistoryList;
    }

    public void setCommentHistoryList(List<CommentHistoryListBean> commentHistoryList) {
        this.commentHistoryList = commentHistoryList;
    }

    public static class CommentHistoryListBean {
        /**
         * title : helloworld
         * objid : 9688
         * comment : 这篇文章写得很好
         * url : http://oschina.org/code/snippet_253472_9688
         * picUrl : http://oschina.org/code/snippet324.jpg
         */

        private String title;
        private String objid;
        private String comment;
        private String url;
        private String picUrl;

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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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
