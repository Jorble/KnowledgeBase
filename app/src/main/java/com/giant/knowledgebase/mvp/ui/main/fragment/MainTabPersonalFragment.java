package com.giant.knowledgebase.mvp.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.GlobalConfiguration;
import com.giant.knowledgebase.app.constant.Api;
import com.giant.knowledgebase.app.utils.AnimatorUtil;
import com.giant.knowledgebase.app.utils.GlideCircleTransform;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerMainTabPersonalComponent;
import com.giant.knowledgebase.di.module.MainTabPersonalModule;
import com.giant.knowledgebase.mvp.contract.MainTabPersonalContract;
import com.giant.knowledgebase.mvp.model.LoginModel;
import com.giant.knowledgebase.mvp.model.NewsModel;
import com.giant.knowledgebase.mvp.model.SearchModel;
import com.giant.knowledgebase.mvp.model.api.service.UserService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.UserBean;
import com.giant.knowledgebase.mvp.presenter.MainTabPersonalPresenter;
import com.giant.knowledgebase.mvp.ui.login.activity.LoginActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.AboutActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.CommentHistoryActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.FeedBackActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.LikeHistoryActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.MsgNotifyActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.RecentHistoryActivity;
import com.giant.knowledgebase.mvp.ui.personal.activity.ShareHistoryActivity;
import com.giant.knowledgebase.mvp.ui.widget.NotificationDownloadListener;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;
import ezy.boost.update.UpdateUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.app.constant.Api.APP_DOMAIN;
import static com.giant.knowledgebase.app.constant.Api.APP_DOMAIN_KEY;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_QQ_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_SINA_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.LOGIN_WX_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.PERSONAL_FRAGMENT;
import static com.giant.knowledgebase.app.constant.EventBusTags.USER_LOAD;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static java.util.ResourceBundle.clearCache;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Jorble on 2017/5/27.
 */

public class MainTabPersonalFragment extends BaseFragment<MainTabPersonalPresenter> implements MainTabPersonalContract.View, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.phone_login_ll)
    AutoLinearLayout phoneLoginLl;
    @BindView(R.id.sina_login_ll)
    AutoLinearLayout sinaLoginLl;
    @BindView(R.id.qq_login_ll)
    AutoLinearLayout qqLoginLl;
    @BindView(R.id.wx_login_ll)
    AutoLinearLayout wxLoginLl;
    @BindView(R.id.login_ll)
    AutoLinearLayout loginLl;
    @BindView(R.id.comment_menu_ll)
    AutoLinearLayout commentMenuLl;
    @BindView(R.id.share_menu_ll)
    AutoLinearLayout shareMenuLl;
    @BindView(R.id.menu_ll)
    AutoLinearLayout menuLl;
    @BindView(R.id.msg_setting_ll)
    AutoLinearLayout msgSettingLl;
    @BindView(R.id.feedback_setting_ll)
    AutoLinearLayout feedbackSettingLl;
    @BindView(R.id.about_setting_ll)
    AutoLinearLayout aboutSettingLl;
    @BindView(R.id.exit_setting_ll)
    AutoLinearLayout exitSettingLl;
    @BindView(R.id.other_ll)
    AutoLinearLayout otherLl;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.userImg_iv)
    ImageView userImgIv;
    @BindView(R.id.userName_tv)
    TextView userNameTv;
    @BindView(R.id.third_login_ll)
    AutoLinearLayout thirdLoginLl;
    @BindView(R.id.update_setting_ll)
    AutoLinearLayout updateSettingLl;

    public static MainTabPersonalFragment newInstance() {
        MainTabPersonalFragment fragment = new MainTabPersonalFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTabPersonalComponent
                .builder()
                .appComponent(appComponent)
                .mainTabPersonalModule(new MainTabPersonalModule(this))//请将MainTabPersonalModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_personal, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRefreshLayout();
        refreshLayout.beginRefreshing();
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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
    public void killMyself() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void initRefreshLayout() {
        refreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_water);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.theme_color);
        refreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
    }

    @OnClick({R.id.userImg_iv, R.id.phone_login_ll, R.id.sina_login_ll, R.id.qq_login_ll
            , R.id.wx_login_ll, R.id.comment_menu_ll, R.id.share_menu_ll, R.id.recent_menu_ll
            , R.id.like_menu_ll, R.id.msg_setting_ll, R.id.feedback_setting_ll, R.id.clear_setting_ll
            , R.id.about_setting_ll, R.id.exit_setting_ll, R.id.update_setting_ll})
    public void onViewClicked(View view) {
        Message message = new Message();
        switch (view.getId()) {
            case R.id.userImg_iv:
                if(LoginModel.isLogin()){//若已登录，跳转个人信息

                }else {//若未登录，跳转登录
                    UiUtils.startActivity(LoginActivity.class);
                }
                break;
            case R.id.phone_login_ll:

                break;
            case R.id.sina_login_ll:
                message.what = LOGIN_SINA_ACTION;
                EventBus.getDefault().post(message, LOGIN_ACTION);
                break;
            case R.id.qq_login_ll:
                message.what = LOGIN_QQ_ACTION;
                EventBus.getDefault().post(message, LOGIN_ACTION);
                break;
            case R.id.wx_login_ll:
                message.what = LOGIN_WX_ACTION;
                EventBus.getDefault().post(message, LOGIN_ACTION);
                break;
            case R.id.comment_menu_ll:
                launchActivity(new Intent(getActivity(), CommentHistoryActivity.class));
                break;
            case R.id.share_menu_ll:
                launchActivity(new Intent(getActivity(), ShareHistoryActivity.class));
                break;
            case R.id.like_menu_ll:
                launchActivity(new Intent(getActivity(), LikeHistoryActivity.class));
                break;
            case R.id.recent_menu_ll:
                launchActivity(new Intent(getActivity(), RecentHistoryActivity.class));
                break;
            case R.id.msg_setting_ll:
                launchActivity(new Intent(getActivity(), MsgNotifyActivity.class));
                break;
            case R.id.feedback_setting_ll:
                launchActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.update_setting_ll:
                checkUpdate();
                break;
            case R.id.clear_setting_ll:
                clearAppCache();
                break;
            case R.id.about_setting_ll:
                launchActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.exit_setting_ll:
                exitUser();
                break;
        }
    }

    /**
     * 清理缓存
     */
    private void clearAppCache(){
        //清空本地数据库的数据
        NewsModel.clearHistory();
        SearchModel.clearHistory();
        T.showShort(getContext(),"清理完成");
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        UpdateUtil.clean(getContext());//清除sp的忽略标志和apk文件缓存
        UpdateManager.setDebuggable(true);
        UpdateManager.setWifiOnly(false);
        check();
    }

    void check() {
        UpdateManager.create(getContext())
                .setUrl(GlobalConfiguration.getCacheString(APP_DOMAIN_KEY,APP_DOMAIN)+Api.CHECK_UPDATE_URL)
                .setManual(true)
                .setParser(new IUpdateParser() {
                    @Override
                    public UpdateInfo parse(String source) throws Exception {
                        L.i("source=" + source);//这里的source接收的是上面的agent.setInfo的值
                        //定制解析过程
                        UpdateInfo info = UpdateInfo.parse(source);
                        L.i("url=" + info.url);
                        return info;
                    }
                })
                .setOnNotificationDownloadListener(new NotificationDownloadListener(getContext(), 998))
                .check();
    }

    /**
     * 通过eventbus post事件,远程遥控执行对应方法
     */
    @Subscriber(tag = PERSONAL_FRAGMENT, mode = ThreadMode.MAIN)
    public void onReceiveShare(Message message) {
        switch (message.what) {
            case USER_LOAD:
                if (message.obj == null)
                    break;
                loadUser((UserBean) message.obj);
                break;
        }
    }

    /**
     * 加载用户信息
     */
    private void loadUser(UserBean bean) {
        thirdLoginLl.setVisibility(View.GONE);
        exitSettingLl.setVisibility(View.VISIBLE);
        AnimatorUtil.showUp(exitSettingLl);

        Glide
                .with(getContext()) //上下文
                .load(bean.getPortrait()) //图片地址
                .transform(new GlideCircleTransform(getContext()))//转换成圆形图片
                .placeholder(R.mipmap.pic_user_default)//占位图
                .error(R.mipmap.pic_user_default) //出错的占位图
//                .override(800,400) //图片显示的分辨率 ，像素值 可以转化为DP再设置
//                .animate(R.anim.my_alpha)//显示动画
//                .centerCrop()//CenterCrop()会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .fitCenter()//图片会被完整显示，可能不能完全填充整个ImageView。
                .into(userImgIv); //显示在哪个控件中

        userNameTv.setText(bean.getName());
    }

    /**
     * 退出登录
     */
    private void exitUser() {
        if(!LoginModel.isLogin())return;//若未登录，直接返回，无需注销

        new Handler().postDelayed(() -> {
            String uid=String.valueOf(LoginModel.getUser().getUid());
            Request request = UserService.getLogout(uid);
            ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                @Override
                public void onResponse(BaseJson jsonBean, okhttp3.Headers headers) {
                    L.i("onResponse");
                    if (jsonBean == null) return;

                    thirdLoginLl.setVisibility(View.VISIBLE);
                    exitSettingLl.setVisibility(View.GONE);
                    userImgIv.setImageResource(R.mipmap.pic_user_default);
                    userNameTv.setText("未登录");
                    AnimatorUtil.showUp(thirdLoginLl);

                    GreenDaoManager.getDaoInstant().getUserBeanDao().deleteAll();
                }

                /**
                 * 可以不重写失败回调
                 *
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable e) {
                    L.i("onFailure");
                    T.showShort(getContext(), "注销失败");
                }

            });
        }, 1000);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout mrefreshLayout) {
        //刷新用户状态
        new Handler().postDelayed(() -> {
            refreshLayout.endRefreshing();
            if (LoginModel.isLogin()) {
                loadUser(LoginModel.getUser());
            } else {
                thirdLoginLl.setVisibility(View.VISIBLE);
                exitSettingLl.setVisibility(View.GONE);
                userImgIv.setImageResource(R.mipmap.pic_user_default);
                userNameTv.setText("未登录");
                AnimatorUtil.showUp(thirdLoginLl);
            }
        }, 500);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

}