package com.yohnnsafe.ench_wu.yohnnsafe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;


/**
 * Created by Ench_Wu on 2015/8/30.
 */
public class SettingClickView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.yohnnsafe.ench_wu.yohnnsafe";
    private TextView tvTitle;
    private TextView tvDesc;

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.setting_click, this);
        tvTitle = (TextView) findViewById(R.id.tv_Title);
        tvDesc = (TextView) findViewById(R.id.tv_Desc);
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTvDesc(String desc) {
        tvDesc.setText(desc);
    }


}
