package com.yohnnsafe.ench_wu.yohnnsafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.yohnnsafe.ench_wu.yohnnsafe.db.BlackNumberDao;

public class CallSafe_Service extends Service {

    private BlackNumberDao dao;
    private String originatingAddress;

    public CallSafe_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new BlackNumberDao(this);
        //初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(2147483647);
        registerReceiver(innerReceiver, intentFilter);

    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("短信来了");

            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects) {// 短信最多140字节,
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();// 短信来源号码
//                String messageBody = message.getMessageBody();// 短信内容
                //通过短信的电话号码查询拦截的模式
                String mode = dao.findNumber(originatingAddress);

                if (mode.equals("1")) {
                    abortBroadcast();
                }
                if (mode.equals("2")) {
                    abortBroadcast();
                }
                if (mode.equals("3")) {
                    abortBroadcast();
                }
                break;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
