package com.oyty.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.bean.ServiceDataModel;
import com.oyty.news.R;

import java.util.List;

/**
 * 首页下面工具栏adapter
 *
 * Created by oyty on 5/9/15.
 */
public class ServiceDataAdapter extends BaseAdapter<ServiceDataModel> {

    public ServiceDataAdapter(Context context, List<ServiceDataModel> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_service_model, null);
            viewHolder.mDescriptionLabel = (TextView) convertView.findViewById(R.id.mDescriptionLabel);
            viewHolder.mDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.mDraweeView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ServiceDataModel item = getDataLists().get(position);
        viewHolder.mDescriptionLabel.setText(item.title);
        viewHolder.mDraweeView.setImageURI(Uri.parse(item.fiticon));

        return convertView;
    }

    class ViewHolder {
        TextView mDescriptionLabel;
        SimpleDraweeView mDraweeView;
    }

}
