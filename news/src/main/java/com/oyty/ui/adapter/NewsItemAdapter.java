package com.oyty.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.bean.NewsCategoryBean.News;
import com.oyty.news.R;

import java.util.List;

/**
 * 新闻中心，新闻条目adapter
 */
public class NewsItemAdapter extends BaseAdapter<News> {
    private Context mContext;

    public NewsItemAdapter(Context context, List<News> lists) {
        super(context, lists);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_img = (SimpleDraweeView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_pub_date = (TextView) convertView.findViewById(R.id.tv_pub_date);
            viewHolder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        News item = getDataLists().get(position);

        if(item.listimage == null) {
            viewHolder.iv_img.setVisibility(View.GONE);
        } else {
            viewHolder.iv_img.setVisibility(View.VISIBLE);
            viewHolder.iv_img.setImageURI(Uri.parse(item.listimage));
        }

        //标记新闻是否阅读过
        if (getDataLists().get(position).is_read) {
            viewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.news_item_has_read_textcolor));
        } else {
            viewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.news_item_no_read_textcolor));
        }
        viewHolder.tv_title.setText(item.title);
        viewHolder.tv_pub_date.setText(item.pubdate);
        //viewHolder.tv_comment_count.setText(item.commentlist);

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView iv_img;
        TextView tv_title;
        TextView tv_pub_date;
        TextView tv_comment_count;
    }

}
