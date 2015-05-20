package com.oyty.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by oyty on 5/20/15.
 */
@DatabaseTable(tableName = "cache")
public class Cache {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String cacheKey;

    @DatabaseField
    private String cacheValue;

    public Cache() {

    }

    public Cache(String cacheKey, String cacheValue) {
        this.cacheKey = cacheKey;
        this.cacheValue = cacheValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }
}
