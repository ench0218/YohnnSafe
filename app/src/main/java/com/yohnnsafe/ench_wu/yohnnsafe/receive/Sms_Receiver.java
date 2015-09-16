package com.yohnnsafe.ench_wu.yohnnsafe.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.service.LocationService;

public class Sms_Receiver extends BroadcastReceiver {
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        for (Object object : objects) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String messageBody = message.getMessageBody();

            if (messageBody.equals("#*alarm*#")) {

                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                abortBroadcast();

            } else if (messageBody.equals("#*location*#")) {
                // 获取经纬度坐标
//                context.startService(new Intent(context, LocationService.class));// 开启定位服务
//
//                SharedPreferences sp = context.getSharedPreferences("config",
//                        Context.MODE_PRIVATE);
//                String location = sp.getString("location",
//                        "getting location...");
//
//                System.out.println("location:" + location);
//                abortBroadcast();
                context.startService(new Intent(context, LocationService.class));// 开启定位服务

                String cphone = sp.getString("cphone", "");
                String location = sp.getString("location", "");
                System.out.println("sms:" + location);
                SmsManager smsManager = SmsManager.getDefault();
                System.out.println("cphone：" + cphone);
                smsManager.sendTextMessage(cphone, null, location, null, null);
                abortBroadcast();

            }

        }

    }
}
