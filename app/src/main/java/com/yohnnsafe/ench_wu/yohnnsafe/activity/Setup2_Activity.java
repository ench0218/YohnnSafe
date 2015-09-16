package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.view.SettingItemView;

/**
 * Setup2_Activity
 *
 * @author Ench_Wu
 */
public class Setup2_Activity extends BaseSetupActivity_parent {

    private SettingItemView sivSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        sivSim = (SettingItemView) findViewById(R.id.siv_sim);


        final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String sim = sp.getString("sim", "");
        boolean simCheck = sp.getBoolean("simCheck", false);
        //判断是否已选择获取sim卡
        if (simCheck) {
            sivSim.setChecked(true);
            System.out.println(sim);
        } else {
            sivSim.setChecked(false);
        }


        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sivSim.isChecked()) {
                    sivSim.setChecked(true);
                    cheekSim();
                    sp.edit().putString("sim", cheekSim()).commit();
                    sp.edit().putBoolean("simCheck", true).commit();
                } else {
                    sivSim.setChecked(false);
                    sp.edit().putBoolean("simCheck", false).commit();

                }
            }
        });


    }

    //获取SIM卡序列号
    public String cheekSim() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String simNum = telephonyManager.getSimSerialNumber();

        return simNum;
    }


    public void btnNext(View v) {
        startActivity(new Intent(this, Setup3_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }

    public void btnPrevious(View v) {
        startActivity(new Intent(this, Setup1_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画


    }

    //BaseSetupActivity_parent里的两个抽象方法
    @Override
    protected void setupPrevious() {
        startActivity(new Intent(this, Setup1_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    @Override
    protected void setupNext() {
        startActivity(new Intent(this, Setup3_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }
}
