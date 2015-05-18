package com.oyty.bean;

import java.util.List;

public class NewsCenterBean {
	public List<DataItem> data;

	public class DataItem {
		public String id;
        public String title;
        public String type;
        public List<ChildrenItem> children;

	}

	public class ChildrenItem {
		public String id;
		public String title;
		public String type;
		public String url;
	}
	
}
