package com.yohnnsafe.ench_wu.yohnnsafe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;


/**
 * Created by Ench_Wu on 2015/8/30.
 */
public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.yohnnsafe.ench_wu.yohnnsafe";
    private String mDescOff;
    private String mDescOn;
    private String mTitle;
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitle = attrs.getAttributeValue(NAMESPACE, "title_insetting");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_Title);
        tvDesc = (TextView) findViewById(R.id.tv_Desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        setTvTitle(mTitle);
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTvDesc(String desc) {
        tvDesc.setText(desc);
    }

    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean checked) {
        cbStatus.setChecked(checked);

        if (checked) {
            setTvDesc(mDescOn);
        } else {
            setTvDesc(mDescOff);
        }
    }
}
