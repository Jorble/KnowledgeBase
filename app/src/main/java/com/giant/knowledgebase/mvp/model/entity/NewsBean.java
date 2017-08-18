package com.giant.knowledgebase.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Jorble on 2017/6/15.
 */

@Entity
public class NewsBean implements Serializable {
    @Transient
    private static final long serialVersionUID = -8437508806205056770L;
    /**
     * id : 26754
     * author : test33
     * authorId : 253469
     * type : 4
     * pubDate : 2017-05-31 09:20
     * title : 伊拉克首都巴格达连遭炸弹攻击 至少27人丧生
     * desc : 据法国国际广播电台5月30日援引法新社报道，伊拉克首都巴格达发生2起爆炸案，安全官员与医疗人员于当地时间5月30日表示，至少有27人在攻击中丧生，另有上百人受伤。
     * url : http://mini.eastday.com/mobile/170531092040260.html
     * picUrl : https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3648935290,148471663&fm=173&s=74AB97554E6559110E170D6B0300F03B&w=500&h=212&img.JPEG
     * picUrl2 : http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg
     * picUrl3 : http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg
     * likeCount : 2230
     * dislikeCount : 410
     * shareCount : 210
     * commentCount : 20
     * isLike : 0
     * isSubscribe : 0
     * isFavor : 0
     */

    //数据库ID不能用int
    @Id
    private Long dbid;
    @Unique
    private String id;
    private String author;
    private String authorId;
    private int type;
    private int catalog;
    private String pubDate;
    private String title;
    private String desc;
    private String url;
    private String picUrl;
    private String picUrl2;
    private String picUrl3;
    private int likeCount;
    private int dislikeCount;
    private int shareCount;
    private int commentCount;
    private int isLike;
    private int isSubscribe;
    private int isFavor;
    @Generated(hash = 1059444267)
    public NewsBean(Long dbid, String id, String author, String authorId, int type, int catalog, String pubDate, String title, String desc, String url,
            String picUrl, String picUrl2, String picUrl3, int likeCount, int dislikeCount, int shareCount, int commentCount, int isLike, int isSubscribe,
            int isFavor) {
        this.dbid = dbid;
        this.id = id;
        this.author = author;
        this.authorId = authorId;
        this.type = type;
        this.catalog = catalog;
        this.pubDate = pubDate;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.picUrl = picUrl;
        this.picUrl2 = picUrl2;
        this.picUrl3 = picUrl3;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.isLike = isLike;
        this.isSubscribe = isSubscribe;
        this.isFavor = isFavor;
    }
    @Generated(hash = 1662878226)
    public NewsBean() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getCatalog() {
        return this.catalog;
    }
    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }
    public String getPubDate() {
        return this.pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getPicUrl2() {
        return this.picUrl2;
    }
    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }
    public String getPicUrl3() {
        return this.picUrl3;
    }
    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
    }
    public int getLikeCount() {
        return this.likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public int getDislikeCount() {
        return this.dislikeCount;
    }
    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
    public int getShareCount() {
        return this.shareCount;
    }
    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
    public int getCommentCount() {
        return this.commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getIsLike() {
        return this.isLike;
    }
    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
    public int getIsSubscribe() {
        return this.isSubscribe;
    }
    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
    public int getIsFavor() {
        return this.isFavor;
    }
    public void setIsFavor(int isFavor) {
        this.isFavor = isFavor;
    }
}
