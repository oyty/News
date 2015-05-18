package com.oyty.bean;

import java.util.List;

/**
 * Created by oyty on 5/11/15.
 */
public class NewsCenterTopicBean {

    public List<TopicNews> topic;

    public String more;

    public class TopicNews {
        public String title;
        public String id;
        public String url;
        public String listimage;
        public String description;
    }

}
