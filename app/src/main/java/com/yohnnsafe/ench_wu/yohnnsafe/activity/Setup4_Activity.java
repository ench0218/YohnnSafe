package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yohnnsafe.ench_wu.yohnnsafe.R;

/**
 * Setup4_Activity
 *
 * @author Ench_Wu
 */
public class Setup4_Activity extends BaseSetupActivity_parent {
    private CheckBox checkBox;
    private SharedPreferences mPres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        mPres = getSharedPreferences("config", MODE_PRIVATE);

        boolean cbCheck = mPres.getBoolean("configed", false);

        if (cbCheck){
            checkBox.setText("防盗保护已开启");
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
            checkBox.setText("防盗保护没有开启");
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBox.setText("防盗保护已开启");
                    checkBox.setChecked(true);
                    mPres.edit().putBoolean("configed", true).commit();
                }else {
                    checkBox.setText("防盗保护没有开启");
                    checkBox.setChecked(false);
                    mPres.edit().putBoolean("configed", false).commit();
                }
            }
        });
    }

    @Override
    protected void setupPrevious() {

        startActivity(new Intent(this, Setup3_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    @Override
    protected void setupNext() {
        startActivity(new Intent(this, LostFind_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画


        mPres.edit().putBoolean("configed", true).commit();
    }

    public void btnNext(View v) {
        startActivity(new Intent(this, LostFind_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

    }

    public void btnPrevious(View v) {
        startActivity(new Intent(this, Setup3_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);

    }
}
