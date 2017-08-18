package com.giant.knowledgebase.mvp.ui.wiki.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.constant.Api;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.mvp.contract.NewsDetailsContract;
import com.race604.drawable.wave.WaveDrawable;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.delight.android.webview.AdvancedWebView;

public class WikiWordActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.loadView)
    ImageView loadView;
    @BindView(R.id.webview)
    AdvancedWebView webview;
    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_refresh_iv)
    ImageView barRefreshIv;

    Timer timer;
    WaveDrawable mWaveDrawable;

    String word="";
    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_word);
        ButterKnife.bind(this);

        //得到词条
        word=getIntent().getCharSequenceExtra("word").toString();
        //拼接url
        url=Api.WIKI_URL+word;
        L.i(url);

        timer = new Timer();

        //初始化加载动画
        initLoadingView();

        //初始化webview
        initWebView();

    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        webview.setListener(this, this);
        webview.loadUrl(Api.WIKI_URL+word);//拼接url
    }

    private void initLoadingView() {
        mWaveDrawable = new WaveDrawable(this, R.mipmap.pic_loading_web);
        mWaveDrawable.setWaveAmplitude(5);//振幅,max=100
        mWaveDrawable.setWaveLength(100);//波长,max=600
        mWaveDrawable.setWaveSpeed(5);//速度,max=50
//        mWaveDrawable.setLevel(4000);//进度,max=10000
//        mWaveDrawable.setIndeterminate(true);//是否自增

        // Use as common drawable
        loadView.setImageDrawable(mWaveDrawable);
    }

    public void showLoading() {
        loadView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);

        progressBar.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (progressBar != null && progressBar.getProgress() < 90) {
                        progressBar.incrementProgressBy(1);
                        mWaveDrawable.setLevel(100 * progressBar.getProgress());
                    }
                });
            }
        }, 1000, 100);
    }

    public void hideLoading() {
        loadView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        timer.cancel();
        webview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (!webview.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLoading();
    }

    @Override
    public void onPageFinished(String url) {
        hideLoading();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        hideLoading();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {
        webview.loadUrl(url);
    }

    @OnClick({R.id.bar_back_iv, R.id.bar_refresh_iv,R.id.bar_close_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_back_iv:
                if (!webview.onBackPressed()) {
                    return;
                }
                super.onBackPressed();
                break;
            case R.id.bar_close_iv:
                finish();
                break;
            case R.id.bar_refresh_iv:
                webview.reload();
                break;
        }
    }
}
