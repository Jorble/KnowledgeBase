package com.giant.knowledgebase.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.giant.knowledgebase.BuildConfig;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.constant.Api;
import com.giant.knowledgebase.app.utils.ACache;
import com.giant.knowledgebase.app.utils.KeyBoardUtils;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.mvp.model.TipDataModel;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.itheima.retrofitutils.ItheimaHttp;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.App;
import com.jess.arms.base.delegate.AppDelegate;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;
import com.jess.arms.integration.IRepositoryManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import timber.log.Timber;

/**
 * app的全局配置信息在此配置,需要将此实现类声明到AndroidManifest中
 * ConfigModule 用来给框架配置各种自定义属性和功能,配合 GlobalConfigModule 使用非常强大
 * Created by jess on 12/04/2017 17:25
 * Contact with jess.yan.effort@gmail.com
 */


public class GlobalConfiguration implements ConfigModule {

    private static ACache mAcache;

    /**
     * 友盟分享:
     * 这里只配置了微信、QQ/Qzone、新浪的三方appkey，如果使用其他平台，在这里增加对应平台key配置
     */
    {
        Config.DEBUG = true;
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setWeixin("wxa8bc4e26302f64fb", "73f6c8870fdef47d9b304387a2b907a6");
        PlatformConfig.setSinaWeibo("1464590837", "eba2e964aadf27c33b1a155e63bc3a0c", "http://sns.whalecloud.com");
    }

    /**
     * 获取本地缓存文件
     * @return
     */
    public static ACache getAcache() {
        return mAcache;
    }

    /**
     * 获取缓存key-value，如果有值则返回，如果没有则返回默认值并保存
     * @param key
     * @param index 默认值
     * @return
     */
    public static String getCacheString(String key,String index){
        String value=mAcache.getAsString(key);
        if(TextUtils.isEmpty(value)){
            mAcache.put(key,index);
            return index;
        }
        return value;
    }

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {

    }

    /**
     *   ● 使用 RepositoryManager 之前,必须在 ConfigModule 中注册上面声明的组件
     */
    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {
        //使用repositoryManager可以注入一些服务
//        repositoryManager.injectRetrofitService(CountService.class);//Retrofit需要的Service
//        repositoryManager.injectCacheService(CommonCache.class);//RxCache需要的Service
    }

    /**
     * //向Application的生命周期中注入一些自定义逻辑
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppDelegate.Lifecycle() {

            @Override
            public void onCreate(Application application) {

                if (BuildConfig.LOG_DEBUG) {//Timber日志打印
                    Timber.plant(new Timber.DebugTree());
                }
                //leakCanary内存泄露检查
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);

                //缓存
                mAcache=ACache.get(context);

                //初始化 retrofitUtils初始化需要二个参数Context、baseUrl
                //baseUrl格式："http://xxxxxx/xxxxx/"
                String domain=getCacheString(Api.APP_DOMAIN_KEY,Api.APP_DOMAIN);
                L.i("domain="+domain);
                ItheimaHttp.init(context, domain);


                //设置是否缓存http响应数据（默认支持缓存）
                ItheimaHttp.setHttpCache(false);//false不缓存，true缓存

                //友盟分享
                UMShareAPI.get(context);

                //Green Dao
                GreenDaoManager.setupDatabase(context,"knowledgebase.db");
                //初始化栏目标签
                TipDataModel.initTips();
            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

    /**
     * //向Activity的生命周期中注入一些自定义逻辑
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里

                //状态栏
                StatusBarUtil.setColor(activity, activity.getResources().getColor(R.color.theme_bg),255);

                //返回
                if (activity.findViewById(R.id.bar_back_iv) != null) {
                    activity.findViewById(R.id.bar_back_iv).setOnClickListener(v -> {
                        activity.onBackPressed();
                    });
                }


            }


            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
                //关闭软键盘
                KeyBoardUtils.closeKeyboard(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //关闭软键盘
                KeyBoardUtils.closeKeyboard(activity);
            }
        });
    }

    /**
     * //向Fragment的生命周期中注入一些自定义逻辑
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
            lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

                @Override
                public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                    // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                    // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
//                    f.setRetainInstance(true);
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    ((RefWatcher)((App) f.getActivity().getApplication()).getAppComponent().extras().get(RefWatcher.class.getName())).watch(this);
                }
            });
    }

}
