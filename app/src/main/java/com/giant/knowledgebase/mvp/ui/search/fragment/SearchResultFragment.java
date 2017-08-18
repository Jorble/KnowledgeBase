package com.giant.knowledgebase.mvp.ui.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.giant.knowledgebase.R;
import com.giant.knowledgebase.mvp.ui.main.adapter.SlidingTabPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultFragment extends Fragment {

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    Unbinder unbinder;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private SlidingTabPagerAdapter mAdapter;
    private String[] mTitles={"资讯","订阅","百科"};

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        unbinder = ButterKnife.bind(this, view);

        initTab();

        return view;
    }

    /**
     * 初始化指示器标签
     */
    public void initTab() {

        //添加fragment
        mFragments.clear();

        mFragments.add(SearchResultNewsFragment.newInstance());
        mFragments.add(SearchResultSubscribeFragment.newInstance());
        mFragments.add(SearchResultWordFragment.newInstance());

        //设置适配器
        mAdapter = new SlidingTabPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        vp.setAdapter(mAdapter);

        vp.setOffscreenPageLimit(mFragments.size() - 1);
        //初始化
        slidingTabLayout.setViewPager(vp);
        vp.setCurrentItem(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {// 不在最前端界面显示
//
//        } else {// 重新显示到最前端中
//            initTab();
//        }
//    }
}
