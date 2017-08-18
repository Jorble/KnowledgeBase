package com.giant.knowledgebase.mvp.model;

import com.giant.knowledgebase.mvp.model.db.GreenDaoManager;
import com.giant.knowledgebase.mvp.model.db.gen.SimpleTitleTipDao;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.bean.SimpleTitleTip;
import com.giant.knowledgebase.mvp.ui.widget.easytagdragview.bean.Tip;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static com.giant.knowledgebase.app.constant.Catalog.HOT;
import static com.giant.knowledgebase.app.constant.Catalog.LAST;
import static com.giant.knowledgebase.app.constant.Catalog.MILITARY;
import static com.giant.knowledgebase.app.constant.Catalog.RECOMMEND;
import static com.giant.knowledgebase.app.constant.Catalog.SOCIAL;
import static com.giant.knowledgebase.app.constant.Catalog.TECH;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class TipDataModel {
    //已添加的（默认）
    private static String[] dragTips ={ RECOMMEND,HOT, LAST,TECH,MILITARY,SOCIAL};
    //未添加的
    private static String[] addTips ={"数码","移动互联","云课堂","家居","旅游","健康","读书","跑步","情感","政务","艺术","博客","轻松一刻","军事","历史","游戏","时尚","NBA"
            ,"漫画","段子","中国足球","手机"};

    /**
     * 获取已添加栏目
     * @return
     */
    public static List<Tip>  getDragTips(){
        List<Tip> result = new ArrayList<>();
        List<SimpleTitleTip> daoList = GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().queryBuilder()
                .where(SimpleTitleTipDao.Properties.IsAdd.eq(1))
                .orderAsc(SimpleTitleTipDao.Properties.Id)
                .list();
        result.addAll(daoList);
        return result;
    }

    /**
     * 获取已添加栏目名称
     * @return
     */
    public static String[] getDragTipsTitle(){
        List<String> result = new ArrayList<>();
        List<SimpleTitleTip> daoList = GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().queryBuilder()
                .where(SimpleTitleTipDao.Properties.IsAdd.eq(1))
                .orderAsc(SimpleTitleTipDao.Properties.Id)
                .list();
        for(SimpleTitleTip tip:daoList){
            result.add(tip.getTip());
        }

        String[] strings = new String[result.size()];
        result.toArray(strings);

        return strings;
    }

    /**
     * 获取未添加栏目
     * @return
     */
    public static List<Tip> getAddTips(){
        List<Tip> result = new ArrayList<>();
        List<SimpleTitleTip> daoList = GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().queryBuilder()
                .where(SimpleTitleTipDao.Properties.IsAdd.eq(0))
                .orderAsc(SimpleTitleTipDao.Properties.Id)
                .list();
        result.addAll(daoList);
        return result;
    }

    /**
     * 初始化栏目标签到数据库
     */
    public static void initTips(){
        //如果数据库为空才初始化数据
        if(0==GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().count()) {
            List<SimpleTitleTip> result = new ArrayList<>();
            //添加已添加的，id必须唯一
            for (int i = 0; i < dragTips.length; i++) {
                String temp = dragTips[i];
                SimpleTitleTip tip = new SimpleTitleTip();
                tip.setTip(temp);
                tip.setId(i);
                tip.setIsAdd(1);
                result.add(tip);
            }
            //添加未添加的，id必须唯一
            for (int i = 0; i < addTips.length; i++) {
                String temp = addTips[i];
                SimpleTitleTip tip = new SimpleTitleTip();
                tip.setTip(temp);
                tip.setId(i + dragTips.length);
                tip.setIsAdd(0);
                result.add(tip);
            }

            GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().deleteAll();
            GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().insertOrReplaceInTx(result);
        }
    }

    /**
     * 更新栏目标签到数据库
     */
    public static void refreshTips(List<SimpleTitleTip> newDragTips){
            List<SimpleTitleTip> result = new ArrayList<>();
            //添加已添加的，id必须唯一
            for (int i = 0; i < newDragTips.size(); i++) {
                SimpleTitleTip tip = new SimpleTitleTip();
                tip.setTip(newDragTips.get(i).getTip());
                tip.setId(i);
                tip.setIsAdd(1);
                result.add(tip);
            }

            //添加未添加的，id必须唯一
            String[] allTips=concat(dragTips,addTips);
            for (int i = 0; i < allTips.length; i++) {
                boolean isContain=false;//是否已添加
                for(SimpleTitleTip temp:newDragTips){
                    if(allTips[i].equals(temp.getTip())){
                        isContain=true;
                        break;
                    }
                }
                //不存在已添加序列里才添加
                if(!isContain) {
                    SimpleTitleTip tip = new SimpleTitleTip();
                    tip.setTip(allTips[i]);
                    tip.setId(i + result.size());
                    tip.setIsAdd(0);
                    result.add(tip);
                }
            }

            GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().deleteAll();
            GreenDaoManager.getDaoInstant().getSimpleTitleTipDao().insertOrReplaceInTx(result);
    }

    /**
     * 合并两个数组
     * @param a
     * @param b
     * @return
     */
    static String[] concat(String[] a, String[] b) {
        String[] c= new String[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
