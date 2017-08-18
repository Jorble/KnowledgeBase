package com.giant.knowledgebase.mvp.presenter;

import android.app.Application;

import com.giant.knowledgebase.mvp.contract.NewsContract;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Jorble on 2017/5/31.
 */

@ActivityScope
public class NewsPresenter extends BasePresenter<NewsContract.Model, NewsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    public static int num=0;

    @Inject
    public NewsPresenter(NewsContract.Model model, NewsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 模拟后台返回数据
     * @return
     */
    public List<NewsBean> getNews(){
        List<NewsBean> newsListBeanList=new ArrayList<>();
        for(int i=0;i<10;i++){
            NewsBean bean=new NewsBean();
            bean.setId(""+num*10+i);
            bean.setAuthor("test33");
            bean.setAuthorId("id312");
            bean.setCommentCount(20);
            bean.setDesc("中国为什么需要两栖攻击舰？");
            bean.setDislikeCount(410);
            bean.setLikeCount(2230);
            bean.setPicUrl("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589556306144_620_1000.JPEG");
            bean.setPicUrl2("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589580573190_620_1000.JPEG");
            bean.setPicUrl3("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589623655396_620_1000.JPEG");
            bean.setPubDate("2017-05-31 09:20");
            bean.setShareCount(210);
            bean.setTitle("中国又一艘“航母”即将下水？俄罗斯想买，美表示不服");
            bean.setType(1);
            bean.setUrl("http://kb.kkyuyin.com/item/2064e733120792594e513dfc1468f53c.html?from=smsc&uc_param_str=dnntnwvepffrgibijbpr");
            bean.setIsLike(0);
            newsListBeanList.add(bean);
        }
        return newsListBeanList;
    }

    /**
     * 模拟后台返回数据（铸造）
     * @return
     */
    public List<NewsBean> getNews2(){
        List<NewsBean> newsListBeanList=new ArrayList<>();
        for(int i=0;i<5;i++){
            NewsBean bean=new NewsBean();
            bean.setId(""+num*10+i);
            bean.setAuthor("test33");
            bean.setAuthorId("id312");
            bean.setCommentCount(20);
            bean.setDesc("根据我国外热风冲天炉的应用特点，在以往取得成果的基础上，为低质炉料的应用，研发了顶置燃烧室外热风冲天炉");
            bean.setDislikeCount(410);
            bean.setLikeCount(2230);
            bean.setPicUrl("http://fs01.bokee.net/userfilespace/2014/11/11/hbxlhb21105713.jpg");
            bean.setPicUrl2("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589580573190_620_1000.JPEG");
            bean.setPicUrl3("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589623655396_620_1000.JPEG");
            bean.setPubDate("2017-05-31 09:20");
            bean.setShareCount(210);
            bean.setTitle("根据国家三部委推荐外热风冲天炉的公告");
            bean.setType(1);
            bean.setUrl("https://wapbaike.baidu.com/item/%e5%86%b2%e5%a4%a9%e7%82%89/2371084?adapt=1&fr=aladdin&bk_tashuoStyle=topLeft&bk_share=shoubai&bk_sharefr=lemma");
            bean.setIsLike(0);
            newsListBeanList.add(bean);

            NewsBean bean2=new NewsBean();
            bean2.setId(""+num*10+i);
            bean2.setAuthor("test33");
            bean2.setAuthorId("id312");
            bean2.setCommentCount(640);
            bean2.setDesc("铸造行业会逐渐的淘汰原始煤烧炉而采用电炉");
            bean2.setDislikeCount(40);
            bean2.setLikeCount(223);
            bean2.setPicUrl("http://img2.imgtn.bdimg.com/it/u=4203089358,2419990616&fm=214&gp=0.jpg");
            bean2.setPicUrl2("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589580573190_620_1000.JPEG");
            bean2.setPicUrl3("http://n1.itc.cn/img7/adapt/wb/sohulife/2017/06/03/149642589623655396_620_1000.JPEG");
            bean2.setPubDate("2017-05-31 09:20");
            bean2.setShareCount(67);
            bean2.setTitle("铸造是现代制造工业的基础工艺之一");
            bean2.setType(1);
            bean2.setUrl("https://wapbaike.baidu.com/item/%e9%93%b8%e9%80%a0%e7%94%b5%e7%82%89/7426931?adapt=1&fr=aladdin&bk_tashuoStyle=topLeft&bk_share=shoubai&bk_sharefr=lemma");
            bean2.setIsLike(0);
            newsListBeanList.add(bean2);

        }
        return newsListBeanList;
    }
}