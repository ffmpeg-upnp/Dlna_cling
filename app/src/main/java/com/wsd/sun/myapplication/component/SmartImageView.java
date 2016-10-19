package com.wsd.sun.myapplication.component;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wsd.sun.myapplication.R;

public class SmartImageView extends ImageView {


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setImageUrl(String url) {
        setImage(url, 0, 0);
    }

    public void setImageUrl(String url, @DrawableRes int errorDrawable) {
        setImage(url, errorDrawable, 0);
    }

    public void setImageUrl(String url, @DrawableRes int errorDrawable, @DrawableRes int loadDrawable) {
        setImage(url, errorDrawable, loadDrawable);
    }

    private void setImage(String url, @DrawableRes int errorDrawable, @DrawableRes int loadDrawable) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.contains("https")) {
            url = url.replace("https", "http");
        }
        if (errorDrawable == 0) {
            errorDrawable = R.drawable.default_bg;
        }
        if (loadDrawable == 0) {
            loadDrawable = R.drawable.default_bg;
        }
        setScaleType(ScaleType.FIT_XY);
        if (getContext() != null) {
            Glide.with(getContext().getApplicationContext())
                    .load(url)
                    .fitCenter()
                    .placeholder(loadDrawable)
                    .error(errorDrawable)
                    .crossFade()
                    .into(this);
        }
        invalidate();
    }

}