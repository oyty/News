package com.oyty.bean;

import java.util.List;

/**
 * Created by oyty on 5/10/15.
 */
public class SmartServiceBean {

    public List<DataItem> data;

    public class DataItem {
        public String id;
        public String title;
        public List<ServiceDataModel> items;
    }

}
