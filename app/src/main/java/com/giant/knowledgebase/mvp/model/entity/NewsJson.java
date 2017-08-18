package com.giant.knowledgebase.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jorble on 2017/5/31.
 */

public class NewsJson {

    /**
     * errCode : 200
     * msg : 请求成功
     * newsList : [{"id":26754,"author":"test33","authorId":253469,"type":4,"pubDate":"2017-05-31 09:20","title":"伊拉克首都巴格达连遭炸弹攻击 至少27人丧生","desc":"据法国国际广播电台5月30日援引法新社报道，伊拉克首都巴格达发生2起爆炸案，安全官员与医疗人员于当地时间5月30日表示，至少有27人在攻击中丧生，另有上百人受伤。","url":"http://mini.eastday.com/mobile/170531092040260.html","picUrl":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3648935290,148471663&fm=173&s=74AB97554E6559110E170D6B0300F03B&w=500&h=212&img.JPEG","picUrl2":"http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg","picUrl3":"http://04.imgmini.eastday.com/mobile/20170531/20170531091258_077108c859f8d842569ac39836d33b44_2_mwpm_03200403.jpeg","likeCount":2230,"dislikeCount":410,"shareCount":210,"commentCount":20,"isLike":0,"isSubscribe":0,"isFavor":0}]
     */

    private int errCode;
    private String msg;
    private List<NewsBean> newsList;

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

}
