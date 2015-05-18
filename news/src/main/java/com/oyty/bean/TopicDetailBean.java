package com.oyty.bean;

import java.util.List;

/**
 * Created by oyty on 5/11/15.
 */
public class TopicDetailBean {

    public String title;
    public String listimage;
    public String description;
    public List<TopicDetailNews> news;

    public class TopicDetailNews {
        public String title;
        public String url;
        public String listimage;
        public String pubdate;
        public boolean is_read;
    }
}
