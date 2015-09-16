package com.yohnnsafe.ench_wu.yohnnsafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.db.AddressDao;

/**
 * 来电取点调用此服务，来电使用service，去电使BroadcastReceiver
 * Created by Ench_Wu on 2015/9/7.
 */
public class AddressService extends Service {
    private WindowManager.LayoutParams params;
    private TelephonyManager tm;
    private MyListener listener;
    private OutCallReceiver receiver;
    private WindowManager mWM;
    private View view;
    private SharedPreferences mPres;
    private int startX, startY;
    private int winHeight;
    private int winWidth;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获得来电号码
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        mPres = getSharedPreferences("config", MODE_PRIVATE);
        //注册OutCallReceiver
        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);

    }

    class MyListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话铃响时
//                    System.out.println("电话响铃");
//                    System.out.println("incomingNumber" + incomingNumber);
                    String address = AddressDao.getAddress(incomingNumber);
//                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE://电话空闲时
                    if (mWM != null && view != null) {
                        mWM.removeView(view);
                        view = null;
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);

        }
    }

    /**
     * 呼出时广播接收者监听
     */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resultData = getResultData();

            String address = AddressDao.getAddress(resultData);
//         Toast.makeText(context, address, Toast.LENGTH_LONG).show();
            showToast(address);
        }
    }

    /**
     * 自定义Toast
     */
    private void showToast(String address) {
        mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        // 获取屏幕宽高
        winWidth = mWM.getDefaultDisplay().getWidth();
        winHeight = mWM.getDefaultDisplay().getHeight();

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;// 电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
        params.gravity = Gravity.LEFT + Gravity.TOP;// 将重心位置设置为左上方,
        // 也就是(0,0)从左上方开始,而不是默认的重心位置
        params.setTitle("Toast");

        int lastX = mPres.getInt("lastX", 0);
        int lastY = mPres.getInt("lastY", 0);

        // 设置浮窗的位置, 基于左上方的偏移量
        params.x = lastX;
        params.y = lastY;

        // view = new TextView(this);
        view = View.inflate(this, R.layout.addres_toast, null);

        TextView myToast = (TextView) view.findViewById(R.id.tv_address_toast);

        int[] bgs = new int[]{
                R.mipmap.call_locate_white,
                R.mipmap.call_locate_orange,
                R.mipmap.call_locate_blue,
                R.mipmap.call_locate_gray,
                R.mipmap.call_locate_green};

        int style = mPres.getInt("address_style", 0);
        System.out.println(style);
        myToast.setBackgroundResource(bgs[style]);
        myToast.setText(address);

        mWM.addView(view, params);// 将view添加在屏幕上(Window)

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新浮窗位置
                        params.x += dx;
                        params.y += dy;

                        // 防止坐标偏离屏幕
                        if (params.x < 0) {
                            params.x = 0;
                        }

                        if (params.y < 0) {
                            params.y = 0;
                        }

                        // 防止坐标偏离屏幕
                        if (params.x > winWidth - view.getWidth()) {
                            params.x = winWidth - view.getWidth();
                        }

                        if (params.y > winHeight - view.getHeight()) {
                            params.y = winHeight - view.getHeight();
                        }

                        // System.out.println("x:" + params.x + ";y:" + params.y);

                        mWM.updateViewLayout(view, params);

                        // 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mPres.edit().putInt("lastX", params.x).commit();
                        mPres.edit().putInt("lastY", params.y).commit();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);
    }

}
