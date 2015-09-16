package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;

/**
 * LostFind_Activity
 *
 * @author Ench_Wu
 */
public class LostFind_Activity extends Activity {
    private TextView tv_safe_phone;
    private ImageView iv_lock;
    String cphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPres = getSharedPreferences("config", MODE_PRIVATE);

        boolean configed = mPres.getBoolean("configed", false);
        boolean simCheck = mPres.getBoolean("simCheck", false);
        cphone = mPres.getString("cphone", "");
        if (configed) {
            setContentView(R.layout.activity_lost_find);

            //手机防盗页面更新
            //安全号码更新
            cphone = mPres.getString("cphone", "");
            tv_safe_phone = (TextView) findViewById(R.id.safe_phone);
            tv_safe_phone.setText(cphone);

            //锁更新
            iv_lock = (ImageView) findViewById(R.id.iv_lock);
            iv_lock.setBackgroundResource(R.mipmap.lock);


        } else if (simCheck| !TextUtils.isEmpty(cphone)) {
            setContentView(R.layout.activity_lost_find);
            System.out.println(simCheck);
        } else {
            //进入引导页
            startActivity(new Intent(this, Setup1_Activity.class));
            finish();
        }

    }

    public void reEnter(View v) {
        startActivity(new Intent(this, Setup1_Activity.class));
        finish();
//            overridePendingTransition();
    }

}
