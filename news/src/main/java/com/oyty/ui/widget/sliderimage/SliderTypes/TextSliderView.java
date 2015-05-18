package com.oyty.ui.widget.sliderimage.SliderTypes;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.news.R;


/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = View.inflate(getContext(), R.layout.render_type_text, null);
        SimpleDraweeView target = (SimpleDraweeView) v.findViewById(R.id.slider_image);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
