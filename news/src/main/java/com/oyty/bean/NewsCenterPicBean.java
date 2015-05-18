package com.oyty.bean;

import java.util.List;

/**
 * Created by oyty on 5/10/15.
 */
public class NewsCenterPicBean {

    public String title;
    public List<PicNews> news;
    public String more;

    public class PicNews {
        public String title;
        public String url;
        public String largeimage;

    }
}
