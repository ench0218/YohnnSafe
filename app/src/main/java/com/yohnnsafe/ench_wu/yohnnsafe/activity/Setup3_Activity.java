package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
/**
 * Setup3_Activity
 *
 * @author Ench_Wu
 */
public class Setup3_Activity extends BaseSetupActivity_parent {
    private EditText et_safe_phone;
    private SharedPreferences sp;
    private String customPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_safe_phone = (EditText) findViewById(R.id.et_safe_phone);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        String cphone = sp.getString("cphone", null);
        String phone = sp.getString("phone", null);
        String name = sp.getString("name", null);

        if (!TextUtils.isEmpty(cphone)) {
            et_safe_phone.setText(cphone);
        } else if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(name)) {
            et_safe_phone.setText(phone + "(" + name + ")");
        }

    }

    public void btnNext(View v) {
        String phone = et_safe_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Setup3_Activity.this, "请输入正确的号码", Toast.LENGTH_SHORT).show();
        }  else {
            customPhone = et_safe_phone.getText().toString();
            sp.edit().putString("cphone", customPhone).commit();
            System.out.println(customPhone);
            startActivity(new Intent(this, Setup4_Activity.class));
            finish();
            overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        }


    }


    public void btnPrevious(View v) {
        startActivity(new Intent(this, Setup2_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);

    }

    @Override
    public void setupNext() {
        startActivity(new Intent(this, Setup4_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }

    @Override
    public void setupPrevious() {
        startActivity(new Intent(this, Setup2_Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    public void btnSelectContacts(View v) {
        Intent intent = new Intent(this, Contacts_Activity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            name = name.replaceAll("-", "").replaceAll(" ", "");
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", "");

//        String[] safe_phone = new String[]{name,phone};
            et_safe_phone.setText(phone + "(" + name + ")");

            sp.edit().putString("name", name).commit();
            sp.edit().putString("phone", phone).commit();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
