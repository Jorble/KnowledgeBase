package com.giant.knowledgebase.mvp.model.entity;

import java.util.List;

/**
 * Created by Jorble on 2017/7/3.
 */

public class SearchJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * newsList : [{"id":26754,"author":"test33","authorId":253469,"type":4,"pubDate":"2017-05-31 09:20","title":"解读一带一路是什么意思 为什么提出一带一路战略","desc":"\u201c一带一路\u201d是\u201c丝绸之路经济带\u201d和\u201c21世纪海上丝绸之路\u201d的简称。","url":"http://mini.eastday.com/mobile/170531092040260.html","picUrl":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3648935290,148471663&fm=173&s=74AB97554E6559110E170D6B0300F03B&w=500&h=212&img.JPEG","picUrl2":"http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg","picUrl3":"http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg","likeCount":2230,"dislikeCount":410,"shareCount":210,"commentCount":20,"isLike":0}]
     * subscribeList : [{"name":"中国新闻网","id":9688,"desc":"发布中国最新热点发布中国最新热点发布中国最新热点发布中国最新热点","count":12321,"picUrl":"http://oschina.org/code/snippet324.jpg"}]
     * wordList : [{"word":"航母","id":9688,"desc":"航空母舰，简称\u201c航母\u201d，是一种以舰载机为主要武器的大型水面舰艇，可以供舰载机起飞和降落。它通常拥有巨大的飞行甲板和舰岛，舰岛大多坐落于右舷。航空母舰是世界上最庞大、最复杂、威力最强的武器之一，是一个国家综合国力的象征。","url":"http://baike.baidu.com/link?url=z4auCdTXXbB0Y_8W04fPQqxPbLKZrlkpHgVmewX9KDo34jNykWvqkDDQwQQRQ-LTCDATjB2t6kkdWGEM2A-rNe-6GZNuwWejh-Tdc9lgDhbNy5ZkQFRCkOYohScobBQLkjbrOUZrAKR9f3PHB1Ioh4INW62XJhue7gaoUVumRkZUlY37vzOivEnjh7rxtA62","picUrl":"http://oschina.org/code/snippet324.jpg"}]
     */

    private int errCode;
    private String msg;
    private List<NewsBean> newsList;
    private List<SubscribeBean> subscribeList;
    private List<WordBean> wordList;

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

    public List<NewsBean> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsBean> newsList) {
        this.newsList = newsList;
    }

    public List<SubscribeBean> getSubscribeList() {
        return subscribeList;
    }

    public void setSubscribeList(List<SubscribeBean> subscribeList) {
        this.subscribeList = subscribeList;
    }

    public List<WordBean> getWordList() {
        return wordList;
    }

    public void setWordList(List<WordBean> wordList) {
        this.wordList = wordList;
    }

}
