package com.giant.knowledgebase.app.utils;

/**
 * Created by Jorble on 2017/6/29.
 */

import android.view.View;

import com.github.florent37.viewanimator.ViewAnimator;

/*
 * @Title:  AnimatorUtil
 * @Copyright:  GuangZhou F.R.O Electronic Technology Co.,Ltd. Copyright 2006-2016,  All rights reserved
 * @Description:  ${TODO}<控件动画显示和消失>
 * @author:  Jorble
 * @data:  2017/3/28 10:24
 * @version:  V1.0
 */
public class AnimatorUtil {

    /**
     * 从下往上显示
     * @param view
     */
    public static void showUp(View view){
        ViewAnimator
                .animate(view)
                .translationY(view.getY(), 0)
                .duration(500)
                .start();
    }
}
