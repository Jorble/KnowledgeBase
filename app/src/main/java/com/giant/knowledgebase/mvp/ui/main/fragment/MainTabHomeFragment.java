package com.giant.knowledgebase.mvp.ui.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.constant.Api;
import com.giant.knowledgebase.app.utils.ACache;
import com.giant.knowledgebase.di.component.DaggerMainTabHomeComponent;
import com.giant.knowledgebase.di.module.MainTabHomeModule;
import com.giant.knowledgebase.mvp.contract.MainTabHomeContract;
import com.giant.knowledgebase.mvp.model.TipDataModel;
import com.giant.knowledgebase.mvp.presenter.MainTabHomePresenter;
import com.giant.knowledgebase.mvp.ui.home.hot.HotFragment;
import com.giant.knowledgebase.mvp.ui.home.last.LastFragment;
import com.giant.knowledgebase.mvp.ui.home.recommend.RecommendFragment;
import com.giant.knowledgebase.mvp.ui.main.activity.MainActivity;
import com.giant.knowledgebase.mvp.ui.main.adapter.SlidingTabPagerAdapter;
import com.giant.knowledgebase.mvp.ui.search.activity.SearchActivity;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.EasyTipDragView;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.bean.SimpleTitleTip;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.bean.Tip;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.widget.TipItemView;
import com.itheima.retrofitutils.ItheimaHttp;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.data;
import static android.R.id.list;
import static com.giant.knowledgebase.app.constant.Api.APP_DOMAIN;
import static com.giant.knowledgebase.app.constant.Api.NEWS_CATALOG_HOT;
import static com.giant.knowledgebase.app.constant.Api.NEWS_CATALOG_RECOMMEND;
import static com.giant.knowledgebase.app.constant.Catalog.HOT;
import static com.giant.knowledgebase.app.constant.Catalog.LAST;
import static com.giant.knowledgebase.app.constant.Catalog.MILITARY;
import static com.giant.knowledgebase.app.constant.Catalog.RECOMMEND;
import static com.giant.knowledgebase.app.constant.Catalog.SOCIAL;
import static com.giant.knowledgebase.app.constant.Catalog.TECH;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.umeng.socialize.utils.DeviceConfig.context;

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

public class MainTabHomeFragment extends BaseFragment<MainTabHomePresenter> implements MainTabHomeContract.View {


    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.head_app_iv)
    ImageView headAppIv;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.head_search_iv)
    ImageView headSearchIv;
    @BindView(R.id.easy_tip_drag_view)
    EasyTipDragView easyTipDragView;
    @BindView(R.id.down_iv)
    ImageView downIv;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private SlidingTabPagerAdapter mAdapter;
    private String[] mTitles;
    private Context context;

    public static MainTabHomeFragment newInstance() {
        MainTabHomeFragment fragment = new MainTabHomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTabHomeComponent
                .builder()
                .appComponent(appComponent)
                .mainTabHomeModule(new MainTabHomeModule(this))//请将MainTabHomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        context=getContext();
        initTab();
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
//        unbinder.unbind();//有bug，解除绑定后会奔溃
    }

    /**
     * 初始化指示器标签
     */
    private void initTab() {
        mTitles=TipDataModel.getDragTipsTitle();

        //添加fragment
        mFragments.clear();
        //只添加能匹配到fragment的标题，如果没有对应的fragment则不添加
        List<String> tipsList=new ArrayList<>();
        for (String title : mTitles) {
            switch (title){
                case RECOMMEND:
                    mFragments.add(RecommendFragment.newInstance());
                    tipsList.add(title);
                    break;
                case HOT:
                    mFragments.add(HotFragment.newInstance());
                    tipsList.add(title);
                    break;
                case LAST:
                    mFragments.add(LastFragment.newInstance());
                    tipsList.add(title);
                    break;
                case TECH:
                    mFragments.add(HotFragment.newInstance());
                    tipsList.add(title);
                    break;
                case MILITARY:
                    mFragments.add(HotFragment.newInstance());
                    tipsList.add(title);
                    break;
                case SOCIAL:
                    mFragments.add(HotFragment.newInstance());
                    tipsList.add(title);
                    break;
                default://测试用（没有该标题的fragment可以顶替用着先）
                    mFragments.add(HotFragment.newInstance());
                    tipsList.add(title);
                    break;
            }
        }

        //设置适配器
        mAdapter = new SlidingTabPagerAdapter(getChildFragmentManager()
                , mFragments, tipsList.toArray(new String[tipsList.size()]));
        vp.setAdapter(mAdapter);

        vp.setOffscreenPageLimit(mFragments.size() - 1);
        //初始化
        slidingTabLayout.setViewPager(vp);
        vp.setCurrentItem(0);
    }


    @OnClick({R.id.head_app_iv, R.id.head_search_iv, R.id.down_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_app_iv:
                showEditAppDialog();
                break;
            case R.id.head_search_iv:
                UiUtils.startActivity(SearchActivity.class);
                break;
            case R.id.down_iv:
                showChannelWindow();
                break;
        }
    }

    /**
     * 弹出修改app_domain值窗口
     */
    private void showEditAppDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("后台地址");
        builder.setIcon(android.R.drawable.btn_star);
        final EditText editText = new EditText(getContext());
        String app_url=ACache.get(context).getAsString(Api.APP_DOMAIN_KEY);
        if(TextUtils.isEmpty(app_url)){
            editText.setText(APP_DOMAIN);
        }else {
            editText.setText(app_url);
        }
        builder.setView(editText);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url=editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(url)){
                            //必须以“/”结尾，如果没有则自动拼接上去
                            if(!url.endsWith("/"))url=url+"/";
                            ACache.get(context).put(Api.APP_DOMAIN_KEY,url);
                            ItheimaHttp.init(context,url);
                            Toast.makeText(context,"后台地址:" + url,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    /**
     * 打开栏目编辑
     */
    private void showChannelWindow(){
        //设置已包含的标签数据
        easyTipDragView.setAddData(TipDataModel.getAddTips());
        //设置可以添加的标签数据
        easyTipDragView.setDragData(TipDataModel.getDragTips());
        //在easyTipDragView处于非编辑模式下点击item的回调（编辑模式下点击item作用为删除item）
        easyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
            @Override
            public void onTileSelected(Tip entity, int position, View view) {
                easyTipDragView.close();
                vp.setCurrentItem(position);
            }
        });
        //设置每次数据改变后的回调（例如每次拖拽排序了标签或者增删了标签都会回调）
        easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<Tip> tips) {

            }
        });
        //设置点击“确定”按钮后最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                List<SimpleTitleTip> newDragTips = new ArrayList<>();
                for(Tip tip:tips){
//                    L.i(((SimpleTitleTip)tip).toString());
                    newDragTips.add((SimpleTitleTip)tip);
                }
                TipDataModel.refreshTips(newDragTips);//更新数据库
                initTab();//更新栏目
            }
        });
        easyTipDragView.open();
    }

    // 返回键按下时会被调用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // TODO
            //判断easyTipDragView是否已经显示出来
            if(easyTipDragView.isOpen()){
                if(!easyTipDragView.onKeyBackDown()){
                    easyTipDragView.close();
                }
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}