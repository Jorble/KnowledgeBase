package com.giant.knowledgebase.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jorble on 2017/6/14.
 */

public class CommentJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * commentList : [{"content":"alksdjf","id":276346713,"pubDate":"2013-10-14 14:53:56","author":"彭博","authorid":89964,"likeCount":321,"replyCount":321,"portrait":"http://www.oschina.net/uploads/user/44/89964_50.jpg?t=1376365607000"}]
     */

    private int errCode;
    private String msg;
    private List<CommentListBean> commentList;

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

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean implements Serializable{
        /**
         * content : alksdjf
         * id : 276346713
         * pubDate : 2013-10-14 14:53:56
         * author : 彭博
         * authorid : 89964
         * likeCount : 321
         * replyCount : 321
         * portrait : http://www.oschina.net/uploads/user/44/89964_50.jpg?t=1376365607000
         */

        private String content;
        private String id;
        private String pubDate;
        private String author;
        private String authorid;
        private int likeCount;
        private int replyCount;
        private String portrait;
        private int isLike;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorid() {
            return authorid;
        }

        public void setAuthorid(String authorid) {
            this.authorid = authorid;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }
    }
}
