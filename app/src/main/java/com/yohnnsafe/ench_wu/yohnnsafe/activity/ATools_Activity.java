package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.utils.BackUpSmsUtils;

public class ATools_Activity extends Activity {


    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void tvCheckCallAddress(View v) {
        Intent intent = new Intent(ATools_Activity.this, CallAddress_Activity.class);
        startActivity(intent);
    }

    public void BackUpSms(View view) {

        pd = new ProgressDialog(this);
        pd.setTitle("备份短信");
        pd.setMessage("正在备份请等一等呗.....");
        pd.setProgressStyle(pd.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

        pd.show();
        new Thread() {
            @Override
            public void run() {
                BackUpSmsUtils.backUp(ATools_Activity.this, pd);
            }
        }.start();

    }
}
