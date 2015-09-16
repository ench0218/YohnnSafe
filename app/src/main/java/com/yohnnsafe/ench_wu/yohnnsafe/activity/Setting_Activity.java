package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.service.AddressService;
import com.yohnnsafe.ench_wu.yohnnsafe.service.CallSafe_Service;
import com.yohnnsafe.ench_wu.yohnnsafe.utils.ServiceUtils;
import com.yohnnsafe.ench_wu.yohnnsafe.view.SettingClickView;
import com.yohnnsafe.ench_wu.yohnnsafe.view.SettingItemView;

/**
 * 设置页面
 */
public class Setting_Activity extends Activity {

    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SettingItemView sivCallSafe;
    private SharedPreferences mPref;
    private SettingClickView scvAddressSytle;
    private SettingClickView scvAddressLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        initUpdate();
        initAddress();
        initCallSafe();
        initAddressStyle();
        initAddressLocation();
    }

    /**
     * 是否开启自动更新
     */
    private void initUpdate() {
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//        sivUpdate.setTvTitle("自动更新开关设置");
//        sivUpdate.setTvDesc("自动更新已开启");


        //使用sp记忆选择状态
        mPref = getSharedPreferences("auto_update", MODE_PRIVATE);
        boolean autoUpdate = mPref.getBoolean("auto_update", true);

        if (autoUpdate) {
//            sivUpdate.setTvDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
//            sivUpdate.setTvDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }


        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
//                    sivUpdate.setTvDesc("自动更新已关闭");
                    mPref.edit().putBoolean("auto_update", false).commit();

                } else {
                    sivUpdate.setChecked(true);
//                    sivUpdate.setTvDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    /**
     * 是否开启归属地显示
     */
    private void initAddress() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        boolean serviceRunning = ServiceUtils.isServiceRunning(Setting_Activity.this, "com.yohnnsafe.ench_wu.yohnnsafe.service.AddressService");
        System.out.println(serviceRunning);
        if (serviceRunning) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }

        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(Setting_Activity.this,
                            AddressService.class));
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(Setting_Activity.this,
                            AddressService.class));
                }
            }
        });
    }


    public void initCallSafe(){
        sivCallSafe = (SettingItemView) findViewById(R.id.siv_callsafe);
        boolean serviceRunning = ServiceUtils.isServiceRunning(Setting_Activity.this, "com.yohnnsafe.ench_wu.yohnnsafe.service.CallSafe_Service");
        System.out.println(serviceRunning);
        if (serviceRunning) {
            sivCallSafe.setChecked(true);
        } else {
            sivCallSafe.setChecked(false);
        }

        sivCallSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivCallSafe.isChecked()) {
                    sivCallSafe.setChecked(false);
                    stopService(new Intent(Setting_Activity.this,
                            CallSafe_Service.class));
                } else {
                    sivCallSafe.setChecked(true);
                    startService(new Intent(Setting_Activity.this,
                            CallSafe_Service.class));
                }
            }
        });
    }

    final String[] items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

    /**
     * 修改提示框显示风格
     */

    private void initAddressStyle() {
        scvAddressSytle = (SettingClickView) findViewById(R.id.scv_address_style);
        scvAddressSytle.setTvTitle("归属地提示框风格");
        int style = mPref.getInt("address_style", 0);
        scvAddressSytle.setTvDesc(items[style]);
        scvAddressSytle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });

    }


    /**
     * 弹出选择风格的单选框
     */
    private void showSingleChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("归属地提示框风格");
        final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        int style = sp.getInt("address_style", 0);// 读取保存的style

        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp.edit().putInt("address_style", which).commit();//保存选择的风格
                dialog.dismiss();
                System.out.println("which:" + which);
                scvAddressSytle.setTvDesc(items[which]);// 更新组合控件的描述信息
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 归属地提示框显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTvTitle("归属地提示框显示位置");
        scvAddressLocation.setTvDesc("设置归属地提示框的显示位置");
        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_Activity.this, Address_Location_Activity.class));
            }
        });

    }
}
