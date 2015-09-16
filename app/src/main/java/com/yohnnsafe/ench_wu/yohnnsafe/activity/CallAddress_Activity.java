package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.db.AddressDao;

public class CallAddress_Activity extends Activity {
    private EditText etNumber;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_addres);
        etNumber = (EditText) findViewById(R.id.et_number);
        tvResult = (TextView) findViewById(R.id.tv_result);
        // 监听EditText的变化
        etNumber.addTextChangedListener(new TextWatcher() {

            // 文字 发生变化时的回调
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String address = AddressDao.getAddress(s.toString());
                tvResult.setText(address);
            }

            // 文字变化前的回调
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            // 文字变化结束之后的回调
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void query(View v) {

        String number = etNumber.getText().toString();
        if (!TextUtils.isEmpty(number)) {
            String address = AddressDao.getAddress(number);
            tvResult.setText(address);
        } else {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200);
            Toast.makeText(CallAddress_Activity.this, "请输入号码", Toast.LENGTH_SHORT).show();
        }

    }

}
