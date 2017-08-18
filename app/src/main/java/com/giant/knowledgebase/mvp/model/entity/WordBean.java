package com.giant.knowledgebase.mvp.model.entity;

/**
 * Created by Jorble on 2017/7/3.
 */

public class WordBean {
    /**
     * word : 航母
     * id : 9688
     * desc : 航空母舰，简称“航母”，是一种以舰载机为主要武器的大型水面舰艇，可以供舰载机起飞和降落。它通常拥有巨大的飞行甲板和舰岛，舰岛大多坐落于右舷。航空母舰是世界上最庞大、最复杂、威力最强的武器之一，是一个国家综合国力的象征。
     * url : http://baike.baidu.com/link?url=z4auCdTXXbB0Y_8W04fPQqxPbLKZrlkpHgVmewX9KDo34jNykWvqkDDQwQQRQ-LTCDATjB2t6kkdWGEM2A-rNe-6GZNuwWejh-Tdc9lgDhbNy5ZkQFRCkOYohScobBQLkjbrOUZrAKR9f3PHB1Ioh4INW62XJhue7gaoUVumRkZUlY37vzOivEnjh7rxtA62
     * picUrl : http://oschina.org/code/snippet324.jpg
     */

    private String word;
    private String id;
    private String desc;
    private String url;
    private String picUrl;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
