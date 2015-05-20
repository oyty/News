package com.oyty.db.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oyty.db.bean.Cache;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oyty on 5/20/15.
 */
public class CacheDbHelper extends OrmLiteSqliteOpenHelper {


    private static final String LOG_TAG = CacheDbHelper.class.getSimpleName();

    private static CacheDbHelper mInstance;

    private static final String DB_NAME = "news_cache.db";

    private static int DB_VERSION = 1;

    /**
     * 对dao进行缓存
     */
    private Map<String, Dao> daoMaps = new HashMap<String, Dao>();

    private CacheDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 单例模式，拿到helper对象
     *
     * @param context
     * @return
     */
    public static CacheDbHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CacheDbHelper.class) {
                if (mInstance == null)
                    mInstance = new CacheDbHelper(context);
            }
        }
        return mInstance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daoMaps.containsKey(className)) {
            dao = daoMaps.get(className);
        } else {
            dao = super.getDao(clazz);
            daoMaps.put(className, dao);
        }
        return dao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cache.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Unable to create databases ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Cache.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Unable to upgrade database from version " + oldVersion + "to new " + newVersion, e);
        }
    }

    @Override
    public void close() {
        super.close();
        for (String key : daoMaps.keySet()) {
            Dao dao = daoMaps.get(key);
            dao = null;
        }
    }
}
