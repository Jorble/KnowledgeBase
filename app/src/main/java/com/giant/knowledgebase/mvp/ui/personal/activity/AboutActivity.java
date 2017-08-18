package com.giant.knowledgebase.mvp.ui.personal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.giant.knowledgebase.R;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.bar_back_iv)
    ImageView barBackIv;
    @BindView(R.id.bar_title_tv)
    TextView barTitleTv;
    @BindView(R.id.bar_title_ll)
    AutoLinearLayout barTitleLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        barTitleTv.setText("关于我们");
    }

    @OnClick(R.id.bar_back_iv)
    public void onViewClicked() {
        finish();
    }
}
