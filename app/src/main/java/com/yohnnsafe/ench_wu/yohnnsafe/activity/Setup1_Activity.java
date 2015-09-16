package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
/**
 * Setup1_Activity
 *
 * @author Ench_Wu
 */
public class Setup1_Activity extends BaseSetupActivity_parent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    protected void setupPrevious() {
        startActivity(new Intent(this, LostFind_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
    }

    @Override
    protected void setupNext() {
        startActivity(new Intent(this, Setup2_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }

    public void btnNext(View v) {
        startActivity(new Intent(this, Setup2_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
    }

}
