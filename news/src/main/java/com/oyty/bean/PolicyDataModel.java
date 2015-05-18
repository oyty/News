package com.oyty.bean;

import java.util.List;

/**
 * Created by oyty on 5/12/15.
 */
public class PolicyDataModel {
    public String title;

    public List<PolicyItem> children;

    public class PolicyItem {
        public String title;
        public String url;
        public List<PolicyChildDataBean.PolicyChildItem> items;
    }

}
