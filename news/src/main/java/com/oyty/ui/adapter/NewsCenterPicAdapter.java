package com.oyty.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.bean.NewsCenterPicBean;
import com.oyty.news.R;

import java.util.List;

/**
 * Created by oyty on 5/10/15.
 */
public class NewsCenterPicAdapter extends BaseAdapter<NewsCenterPicBean.PicNews> {

    public NewsCenterPicAdapter(Context context, List<NewsCenterPicBean.PicNews> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_news_center_pic, null);
            viewHolder.mPicImg = (SimpleDraweeView) convertView.findViewById(R.id.mPicImg);
            viewHolder.mPicLabel = (TextView) convertView.findViewById(R.id.mPicLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsCenterPicBean.PicNews news = getDataLists().get(i);
        viewHolder.mPicImg.setImageURI(Uri.parse(news.largeimage));
        viewHolder.mPicLabel.setText(news.title);
        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView mPicImg;
        TextView mPicLabel;
    }
}
