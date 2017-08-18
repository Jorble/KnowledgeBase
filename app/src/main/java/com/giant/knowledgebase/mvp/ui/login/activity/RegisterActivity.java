package com.giant.knowledgebase.mvp.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.di.component.DaggerRegisterComponent;
import com.giant.knowledgebase.di.module.RegisterModule;
import com.giant.knowledgebase.mvp.contract.RegisterContract;
import com.giant.knowledgebase.mvp.model.api.service.UserService;
import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.LoginJson;
import com.giant.knowledgebase.mvp.presenter.RegisterPresenter;
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

import static com.giant.knowledgebase.app.constant.EventBusTags.PERSONAL_FRAGMENT;
import static com.giant.knowledgebase.app.constant.EventBusTags.USER_LOAD;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {


    @BindView(R.id.usernameEt)
    EditTextWithClear usernameEt;
    @BindView(R.id.pwdEt)
    PasswordEditText pwdEt;
    @BindView(R.id.pwdEt_confirm)
    PasswordEditText pwdEtConfirm;
    @BindView(R.id.register_bt)
    Button registerBt;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        barTitleTv.setText("注册账号");
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

    @OnClick(R.id.register_bt)
    public void onViewClicked() {
        register();
    }

    /**
     * 注册
     */
    private void register(){

        String username=usernameEt.getText().toString();
        String pwd=pwdEt.getText().toString();
        String pwdConfirm=pwdEtConfirm.getText().toString();
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
        if(TextUtils.isEmpty(pwdConfirm)){
            pwdEtConfirm.setShakeAnimation();
            T.showShort(this,"确认密码不能为空");
            return;
        }
        if(!pwd.equals(pwdConfirm)){
            pwdEtConfirm.setShakeAnimation();
            T.showShort(this,"两次密码输入不一致");
            return;
        }
        //注册
        Request request = UserService.getRegister(username, pwd);
        ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
            @Override
            public void onResponse(BaseJson jsonBean, okhttp3.Headers headers) {
                L.i("onResponse");
                if (jsonBean == null) return;

                Intent intent =new Intent();
                intent.putExtra("username", "username");
                intent.putExtra("pwd", "pwd");
                RegisterActivity.this.setResult(RESULT_OK, intent);
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
                T.showShort(getApplicationContext(), "注册失败");
            }
        });
    }
}
