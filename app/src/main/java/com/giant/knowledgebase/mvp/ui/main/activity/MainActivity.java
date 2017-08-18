package com.giant.knowledgebase.mvp.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.DoubleClickExitHelper;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.di.component.DaggerMainComponent;
import com.giant.knowledgebase.di.module.MainModule;
import com.giant.knowledgebase.mvp.contract.MainContract;
import com.giant.knowledgebase.mvp.model.entity.NewsBean;
import com.giant.knowledgebase.mvp.presenter.MainPresenter;
import com.giant.knowledgebase.mvp.ui.main.fragment.MainTabFavorFragment;
import com.giant.knowledgebase.mvp.ui.main.fragment.MainTabHomeFragment;
import com.giant.knowledgebase.mvp.ui.main.fragment.MainTabPersonalFragment;
import com.giant.knowledgebase.mvp.ui.main.fragment.MainTabWikiFragment;
import com.giant.knowledgebase.mvp.ui.subscribe.fragment.MainTabSubscribeFragment;
import com.giant.knowledgebase.mvp.ui.widget.NoScrollViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.kevin.tabindicator.TabPageIndicatorEx;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.giant.knowledgebase.app.constant.EventBusTags.HIDE_ERROR;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_WX_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.NETWORK_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_QZONE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WXCIRCLE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WX_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHOW_ERROR;
import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Jorble on 2017/5/25.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {


    @BindView(R.id.tabpage)
    TabPageIndicatorEx tabpage;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.network_error_ll)
    AutoLinearLayout networkErrorLl;

    DoubleClickExitHelper mDoubleClickExitHelper;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this)) //请将MainModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        // 双击返回键退出
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        initTab();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void recreate() {
        try {//避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                fragmentTransaction.remove(mAdapter.getItem(i));
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //监听在栏目编辑时的返回键处理
            Fragment fg;    // fg记录当前的Fragment
            int index = viewpager.getCurrentItem();
            fg = mAdapter.getItem(index);
            if (fg instanceof MainTabHomeFragment) {
                if (((MainTabHomeFragment) fg).onKeyDown(keyCode, event)) {
                    return false;
                } else {
                    return mDoubleClickExitHelper.onKeyDown(keyCode, event);
                }
            }

            // 是否退出应用
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return flag;
    }

    public void initTab() {
        initTabIndicator();
        initViewPager();
        initEvents();
    }

    private void initViewPager() {

        //添加fragment到tabs里面
        mTabs.add(MainTabHomeFragment.newInstance());
        mTabs.add(MainTabFavorFragment.newInstance());
        mTabs.add(MainTabWikiFragment.newInstance());
        mTabs.add(MainTabPersonalFragment.newInstance());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        viewpager.setAdapter(mAdapter);

        viewpager.setOffscreenPageLimit(3);//缓存4页
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        tabpage.setOnTabSelectedListener(index -> viewpager.setCurrentItem(index, false));
    }

    private void initTabIndicator() {

        tabpage.setViewPager(viewpager);
//        tabpage.setIndicateDisplay(2, true);//设置指示红点
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    /**
     * 网络异常
     */
    @Subscriber(tag = NETWORK_ACTION, mode = ThreadMode.MAIN)
    public void onReceiveNetwork(Message message) {
        switch (message.what) {
            case SHOW_ERROR:
                networkErrorLl.setVisibility(View.VISIBLE);
                break;
            case HIDE_ERROR:
                networkErrorLl.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 第三方分享
     */
    @Subscriber(tag = SHARE_ACTION, mode = ThreadMode.MAIN)
    public void onReceiveShare(Message message) {
        switch (message.what) {
            case SHARE_WX_ACTION:
                if (message.obj == null)
                    break;
                shareWeb((NewsBean) message.obj, SHARE_MEDIA.WEIXIN);
                break;
            case SHARE_WXCIRCLE_ACTION:
                if (message.obj == null)
                    break;
                shareWeb((NewsBean) message.obj, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case SHARE_QQ_ACTION:
                if (message.obj == null)
                    break;
                shareWeb((NewsBean) message.obj, SHARE_MEDIA.QQ);
                break;
            case SHARE_QZONE_ACTION:
                if (message.obj == null)
                    break;
                shareWeb((NewsBean) message.obj, SHARE_MEDIA.QZONE);
                break;
            case SHARE_SINA_ACTION:
                if (message.obj == null)
                    break;
                shareWeb((NewsBean) message.obj, SHARE_MEDIA.SINA);
                break;
        }
        //提交请求...
    }

    /**
     * 分享链接
     *
     * @param bean
     * @param platform
     */
    private void shareWeb(NewsBean bean, SHARE_MEDIA platform) {
        //分享链接可以使用UMWeb进行分享,注意在新浪平台，缩略图属于必传参数，否则会报错
        UMWeb web = new UMWeb(bean.getUrl());
        UMImage image = new UMImage(this, bean.getPicUrl());//网络图片
        web.setTitle(bean.getTitle());//标题
        web.setThumb(image);  //缩略图
        web.setDescription(bean.getDesc());//描述

        new ShareAction(this).setPlatform(platform)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }

    /**
     * 分享回调
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.i(platform + " 分享成功了");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            L.i(platform + " 分享失败了");
            if (t != null) {
                L.e("throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            L.i(platform + " 分享取消了");
        }
    };

    /**
     * 第三方登录
     */
    @Subscriber(tag = LOGIN_ACTION, mode = ThreadMode.MAIN)
    public void onReceiveLogin(Message message) {
        switch (message.what) {
            case LOGIN_WX_ACTION:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case LOGIN_QQ_ACTION:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case LOGIN_SINA_ACTION:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
        }
    }

    /**
     * 登录回调
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            L.i("uid=" + data.get("uid"));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

}