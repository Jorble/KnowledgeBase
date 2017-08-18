package com.giant.knowledgebase.app.constant;

/*
 * @Title:  DbConst
 * @Copyright:  GuangZhou F.R.O Electronic Technology Co.,Ltd. Copyright 2006-2016,  All rights reserved
 * @Description:  ${TODO}<数据库相关常量>
 * @author:  Jorble
 * @data:  2017/2/27 14:37
 * @version:  V1.0
 */
public class DbConst {

    /**
     * 数据库存储路径
     */
    //已root的手机路径="data/data/com.fro.homeplatform/databases"
    //普通手机有权限的路径="mnt/sdcard/com.fro.homeplatform/databases"
    //一般手机存储位置="storage/emulated/0/homeplatform/databases"
    public static final String DB_PATH = "mnt/sdcard/com.giant.knowledgbase/databases";
    public static final String DB_NAME = "knowledgbase.db";
    public static final int DB_VERSION = 1;
    public static int oldVersion = -1;
}
