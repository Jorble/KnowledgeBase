package com.giant.knowledgebase.mvp.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerLoginComponent;
import com.giant.knowledgebase.di.module.LoginModule;
import com.giant.knowledgebase.mvp.contract.LoginContract;
import com.giant.knowledgebase.mvp.model.LoginModel;
import com.giant.knowledgebase.mvp.model.api.service.NewsService;
import com.giant.knowledgebase.mvp.model.api.service.UserService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.LoginJson;
import com.giant.knowledgebase.mvp.model.entity.NewsJson;
import com.giant.knowledgebase.mvp.presenter.LoginPresenter;
import com.giant.knowledgebase.mvp.ui.main.activity.MainActivity;
import com.giant.knowledgebase.mvp.ui.widget.EditTextWithClear;
import com.giant.knowledgebase.mvp.ui.widget.PasswordEditText;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.R.id.refreshLayout;
import static com.giant.knowledgebase.app.constant.EventBusTags.PERSONAL_FRAGMENT;
import static com.giant.knowledgebase.app.constant.EventBusTags.USER_LOAD;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.usernameEt)
    EditTextWithClear usernameEt;
    @BindView(R.id.pwdEt)
    PasswordEditText pwdEt;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.forget_tv)
    TextView forgetTv;
    @BindView(R.id.phone_login_ll)
    AutoLinearLayout phoneLoginLl;
    @BindView(R.id.sina_login_ll)
    AutoLinearLayout sinaLoginLl;
    @BindView(R.id.qq_login_ll)
    AutoLinearLayout qqLoginLl;
    @BindView(R.id.wx_login_ll)
    AutoLinearLayout wxLoginLl;
    @BindView(R.id.third_login_ll)
    AutoLinearLayout thirdLoginLl;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        barTitleTv.setText("登录账号");
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
        finish();
    }


    @OnClick({R.id.login_bt, R.id.register_tv, R.id.forget_tv, R.id.phone_login_ll, R.id.sina_login_ll, R.id.qq_login_ll, R.id.wx_login_ll, R.id.bar_back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_bt:
                login();
                break;
            case R.id.register_tv:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
                break;
            case R.id.forget_tv:
                T.showShort(this, "此功能暂未开放");
                break;
            case R.id.phone_login_ll:
                break;
            case R.id.sina_login_ll:
                break;
            case R.id.qq_login_ll:
                break;
            case R.id.wx_login_ll:
                break;
            case R.id.bar_back_iv:
                killMyself();
                break;
        }
    }

    /**
     * 登录
     */
    private void login(){
        String username=usernameEt.getText().toString();
        String pwd=pwdEt.getText().toString();
        if(TextUtils.isEmpty(username)){
            usernameEt.setShakeAnimation();
            T.showShort(this,"账号不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            pwdEt.setShakeAnimation();
            T.showShort(this,"密码不能为空");
            return;
        }
        //登录
        Request request = UserService.getLogin(username, pwd);
        ItheimaHttp.send(request, new HttpResponseListener<LoginJson>() {
            @Override
            public void onResponse(LoginJson jsonBean, okhttp3.Headers headers) {
                L.i("onResponse");
                if (jsonBean == null) return;

                //刷新个人中心
                Message message=new Message();
                message.what=USER_LOAD;
                message.obj=jsonBean.getUser();
                EventBus.getDefault().post(message,PERSONAL_FRAGMENT);

                //替换数据库的user和token
                GreenDaoManager.getDaoInstant().getUserBeanDao().deleteAll();
                GreenDaoManager.getDaoInstant().getUserBeanDao().insert(jsonBean.getUser());

                T.showShort(getApplicationContext(), "登录成功");
                //返回
                killMyself();
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
                T.showShort(getApplicationContext(), "登录失败");
            }
        });
    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中重写onActivityResult方法
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && data!=null){
            String username = data.getExtras().getString("username");
            String pwd = data.getExtras().getString("pwd");
            usernameEt.setText(username);
            pwdEt.setText(pwd);
            login();
        }
    }
}