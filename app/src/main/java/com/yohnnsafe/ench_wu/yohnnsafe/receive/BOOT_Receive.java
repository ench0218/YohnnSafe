package com.yohnnsafe.ench_wu.yohnnsafe.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BOOT_Receive extends BroadcastReceiver {
    public BOOT_Receive() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        System.out.println("广播启动了!!!!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        boolean configed = sp.getBoolean("configed", false);

        if (configed){
            //得到原始sim卡信息
            String oldSim = sp.getString("sim", "");
            System.out.println("odSim"+oldSim+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String sim = tm.getSimSerialNumber();
            System.out.println("newSim"+sim);
            if (oldSim.equals(sim)){
                System.out.println("手机安全");
            }else {
                System.out.println("请注意!Sim卡已被更换!");

                SmsManager smsManager = SmsManager.getDefault();
                String cphone = sp.getString("cphone", "");
                smsManager.sendTextMessage(cphone,null,"sim card change",null,null);
            }

        }else {

        }

    }
}
