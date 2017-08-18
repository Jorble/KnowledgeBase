package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/8/7.
 */

public class MsgNotifyJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * msgNotifyList : [{"objid":9688,"type":0,"author":"test33","authorId":1341,"pubDate":"05-31 09:20","avatar":"http://oschina.org/code/snippet324.jpg","myComment":"这篇文章写得太好了","hisComment":"这个评论真赞","url":"http://oschina.org/code/snippet_253472_9688"},{"objid":9688,"type":0,"author":"test33","authorId":1341,"pubDate":"05-31 09:20","avatar":"http://oschina.org/code/snippet324.jpg","myComment":"这篇文章写得太好了","hisComment":"这个评论真赞","url":"http://oschina.org/code/snippet_253472_9688"}]
     */

    private int errCode;
    private String msg;
    private List<MsgNotifyListBean> msgNotifyList;

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

    public List<MsgNotifyListBean> getMsgNotifyList() {
        return msgNotifyList;
    }

    public void setMsgNotifyList(List<MsgNotifyListBean> msgNotifyList) {
        this.msgNotifyList = msgNotifyList;
    }

    public static class MsgNotifyListBean {
        /**
         * objid : 9688
         * type : 0
         * author : test33
         * authorId : 1341
         * pubDate : 05-31 09:20
         * avatar : http://oschina.org/code/snippet324.jpg
         * myComment : 这篇文章写得太好了
         * hisComment : 这个评论真赞
         * newsId : 4543
         */


        private String objid;
        private int type;
        private String author;
        private String authorId;
        private String pubDate;
        private String avatar;
        private String myComment;
        private String hisComment;
        private String newsId;

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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMyComment() {
            return myComment;
        }

        public void setMyComment(String myComment) {
            this.myComment = myComment;
        }

        public String getHisComment() {
            return hisComment;
        }

        public void setHisComment(String hisComment) {
            this.hisComment = hisComment;
        }

        public String getNewsId() {
            return newsId;
        }

        public void setNewsId(String newsId) {
            this.newsId = newsId;
        }
    }
}
