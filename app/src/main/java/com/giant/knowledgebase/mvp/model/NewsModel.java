package com.giant.knowledgebase.mvp.model;

import android.app.Application;

import com.giant.knowledgebase.mvp.contract.NewsContract;
import com.giant.knowledgebase.mvp.model.db.gen.NewsBeanDao;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.model.entity.SearchHistoryBean;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import static android.R.id.list;
import static com.giant.knowledgebase.mvp.model.db.GreenDaoManager.getDaoInstant;
import static com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity.mLastQuery;
import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import retrofit2.http.PUT;

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
public class NewsModel extends BaseModel implements NewsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public NewsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     * 加载数据
     * @param catalog
     * @return
     */
    public static List<NewsBean> loadHistory(int catalog){
        //获取该类别的所有数据
        return getDaoInstant().getNewsBeanDao().queryBuilder()
                .where(NewsBeanDao.Properties.Catalog.eq(catalog))
                .list();
    }

    /**
     * 清空数据
     */
    public static void clearHistory(){
        getDaoInstant().getNewsBeanDao().deleteAll();
    }

    /**
     * 保存到历史记录到数据库
     * 每个类别都是先清除原有的再添加，如果大于20条只保留前20条
     * @param catalog
     */
    public static void saveNewsHistory(int catalog,List <NewsBean> addItems){

        int max=20;

        //获取该类别的所有数据
        List <NewsBean> items = getDaoInstant().getNewsBeanDao().queryBuilder()
                .where(NewsBeanDao.Properties.Catalog.eq(catalog))
                .list();

        //清除该类别的所有数据
        if(items!=null && items.size()>0) {
            getDaoInstant().getNewsBeanDao().deleteInTx(items);
        }

        //保存要添加的
        if(addItems.size()<=max) {
            getDaoInstant().getNewsBeanDao().insertOrReplaceInTx(addItems);
        }else {
            List<NewsBean> newlist = new ArrayList<>();
            for(int i = 0; i < max; i++){
                newlist.add(addItems.get(i));
            }
            getDaoInstant().getNewsBeanDao().insertOrReplaceInTx(newlist);
        }
    }

}