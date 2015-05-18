package com.oyty.bean;

import java.util.List;

public class NewsCategoryBean {

    public Data data;

	public class Data {
        public String title;
        public List<TopNews> topnews;
        public List<Topic> topic;
        public List<News> news;
        public String countcommenturl;
        public String more;
	}

    public class TopNews {
        public String id;
        public String title;
        public String topimage;
        public String url;
        public String pubdate;
        public String comment;
        public String commenturl;
        public String type;
        public String commentlist;
    }

    public class News {
        public String id;
        public String title;
        public String url;
        public String listimage;
        public String pubdate;
        public String comment;
        public String commenturl;
        public String type;
        public String commentlist;
        public boolean is_read;

	}
    public class Topic {
        public String description;
        public String id;
        public String listimage;
        public int sort;
        public String title;
        public String url;

	}

}














