package com.giant.knowledgebase.mvp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.giant.knowledgebase.app.GlobalConfiguration;
import com.giant.knowledgebase.mvp.model.db.gen.DaoMaster;
import com.giant.knowledgebase.mvp.model.db.gen.DaoSession;

/**
 * Created by Jorble on 2017/6/15.
 */

public class GreenDaoManager {

    private static DaoSession daoSession;

    /**
     * 配置数据库
     */
    public static void setupDatabase(Context context,String dbName) {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
