package com.oyty.db.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.oyty.base.Constants;
import com.oyty.db.bean.Cache;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by oyty on 5/5/15.
 */
public class CacheDao {

    private Dao<Cache, Integer> mCacheDao;


    public CacheDao(Context context) {
        try {
            mCacheDao = CacheDbHelper.getInstance(context).getDao(Cache.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据cacheKey获取cacheValue
     * @param cacheKey
     * @return
     */
    public String getCache(String cacheKey) {
        try {
            List<Cache> caches = mCacheDao.queryBuilder()
                    .where()
                    .eq(Constants.CacheColumns.CACHEKEY, cacheKey)
                    .query();
            if(caches != null && caches.size() > 0) {
                return caches.get(0).getCacheValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加缓存数据
     * @param cacheKey
     * @param cacheValue
     */
    public void putCache(String cacheKey, String cacheValue) {
        try {
            mCacheDao.create(new Cache(cacheKey, cacheValue));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除缓存数据表数据
     */
    public boolean clearCache() {
        try {
            int count = mCacheDao.queryForAll().size();
            return mCacheDao.delete(mCacheDao.queryForAll()) == count ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
