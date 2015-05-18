package com.oyty.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.bean.NewsCenterTopicBean;
import com.oyty.news.R;

import java.util.List;

/**
 * Created by oyty on 5/11/15.
 */
public class NewsCenterTopicAdapter extends BaseAdapter<NewsCenterTopicBean.TopicNews> {

    public NewsCenterTopicAdapter(Context context, List<NewsCenterTopicBean.TopicNews> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_news_center_topic, null);
            viewHolder.mTopicLabel = (TextView) convertView.findViewById(R.id.mTopicLabel);
            viewHolder.mTopicImg = (SimpleDraweeView) convertView.findViewById(R.id.mTopicImg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsCenterTopicBean.TopicNews news = getDataLists().get(i);
        viewHolder.mTopicLabel.setText(news.title);
        viewHolder.mTopicImg.setImageURI(Uri.parse(news.listimage));
        return convertView;
    }

    class ViewHolder {
        TextView mTopicLabel;
        SimpleDraweeView mTopicImg;
    }
}
