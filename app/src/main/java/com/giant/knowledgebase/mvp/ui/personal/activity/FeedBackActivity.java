package com.giant.knowledgebase.mvp.ui.personal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.KeyBoardUtils;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.app.utils.T;
import com.giant.knowledgebase.mvp.model.api.service.AppService;
import com.giant.knowledgebase.mvp.model.api.service.HistoryService;
import com.giant.knowledgebase.mvp.model.entity.BaseJson;
import com.giant.knowledgebase.mvp.model.entity.LikeHistoryJson;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.giant.knowledgebase.R.id.refreshLayout;

public class FeedBackActivity extends AppCompatActivity {

    @BindView(R.id.content_tv)
    EditText contentTv;
    @BindView(R.id.commit_cb)
    CircularProgressButton commitCb;
    @BindView(R.id.main_ll)
    AutoLinearLayout mainLl;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        barTitleTv.setText("用户反馈");
        commitCb.setIndeterminateProgressMode(true);//循环进度

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        KeyBoardUtils.openKeyboard(FeedBackActivity.this);//打开软键盘
        contentTv.requestFocus();//获取焦点
    }

    @OnClick({R.id.commit_cb, R.id.bar_back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit_cb:
                //获取反馈内容
                String content=contentTv.getText().toString();
                if(TextUtils.isEmpty(content)){
                    T.showShort(this,"反馈内容不能为空");
                    return;
                }
                startCommitAmin();
                KeyBoardUtils.closeKeyboard(FeedBackActivity.this);
                //提交请求
                new Handler().postDelayed(() -> {
                    Request request = AppService.getFeedBack(content);
                    ItheimaHttp.send(request, new HttpResponseListener<BaseJson>() {
                        @Override
                        public void onResponse(BaseJson jsonBean, Headers headers) {
                            contentTv.setText("");//清空输入
                            commitCb.setProgress(100);
                            T.showShort(FeedBackActivity.this,"提交成功，感谢您的支持");
                            new Handler().postDelayed(()->{
                                finish();
                            },2000);
                        }

                        /**
                         * 可以不重写失败回调
                         *
                         * @param call
                         * @param e
                         */
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable e) {
                            commitCb.setProgress(-1);
                            T.showShort(FeedBackActivity.this,"提交失败，请重试");
                            new Handler().postDelayed(()->{
                                commitCb.setProgress(0);
                            },1000);
                        }

                    });
                }, 1000);

                break;
            case R.id.bar_back_iv:
                finish();
                break;
        }
    }

    /**
     * 提交动画
     */
    private void startCommitAmin(){
        if (commitCb.getProgress() == 0) {
            commitCb.setProgress(50);
        } else if (commitCb.getProgress() == 100) {
            commitCb.setProgress(0);
        } else {
            commitCb.setProgress(100);
        }
    }
}
