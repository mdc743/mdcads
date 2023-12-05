package com.marketdigitalcode.adslibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.marketdigitalcode.adslibrary.R;
public class NativeAdView extends LinearLayout {
    private Context mContext;


    public NativeAdView(Context context) {
        super(context);
        this.mContext = context;

    }

    public NativeAdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public NativeAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setLarge() {
        inflate(mContext, R.layout.view_native_ad_large, this);
    }
    public void setMedium() {
        inflate(mContext, R.layout.view_native_ad_medium, this);
    }
    public void setNews() {
        inflate(mContext, R.layout.view_native_ad_news, this);
    }
    public void setRadio() {
        inflate(mContext, R.layout.view_native_ad_radio, this);
    }
    public void setVideoLarge() {
        inflate(mContext, R.layout.view_native_ad_video_large, this);
    }
    public void setVideoSmall() {
        inflate(mContext, R.layout.view_native_ad_video_small, this);
    }
}
