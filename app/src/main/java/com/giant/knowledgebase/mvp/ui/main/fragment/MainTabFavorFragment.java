package com.giant.knowledgebase.mvp.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.di.component.DaggerMainTabFavorComponent;
import com.giant.knowledgebase.di.module.MainTabFavorModule;
import com.giant.knowledgebase.mvp.contract.MainTabFavorContract;
import com.giant.knowledgebase.mvp.presenter.MainTabFavorPresenter;
import com.giant.knowledgebase.mvp.ui.favor.fragment.FavorWebFragment;
import com.giant.knowledgebase.mvp.ui.main.adapter.SlidingTabPagerAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.giant.knowledgebase.R.id.viewPager;
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
 * Created by Jorble on 2017/5/27.
 */

public class MainTabFavorFragment extends BaseFragment<MainTabFavorPresenter> implements MainTabFavorContract.View {


    @BindView(R.id.segmentTabLayout)
    SegmentTabLayout segmentTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "资讯", "文档"
    };
    private SlidingTabPagerAdapter mAdapter;

    public static MainTabFavorFragment newInstance() {
        MainTabFavorFragment fragment = new MainTabFavorFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTabFavorComponent
                .builder()
                .appComponent(appComponent)
                .mainTabFavorModule(new MainTabFavorModule(this))//请将MainTabFavorModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_favor, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
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
        //添加fragment
        for (String title : mTitles) {
            mFragments.add(FavorWebFragment.newInstance());
        }

        //设置适配器
        mAdapter = new SlidingTabPagerAdapter(getActivity().getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);

        //初始化
        segmentTabLayout.setTabData(mTitles);

        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                segmentTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

    }

}