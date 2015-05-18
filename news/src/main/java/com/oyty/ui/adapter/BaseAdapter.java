package com.oyty.ui.adapter;

import android.content.Context;

import java.util.List;

/**
 * 自定义BaseAdapter
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    public Context context;
    private List<T> mLists;


    public BaseAdapter(Context context) {
        this.context = context;
    }

    public BaseAdapter(Context context, List<T> lists) {
        this.context = context;
        this.mLists = lists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDataLists(List<T> lists) {
        this.mLists = lists;
    }

    public List<T> getDataLists() {
        return mLists;
    }
}
