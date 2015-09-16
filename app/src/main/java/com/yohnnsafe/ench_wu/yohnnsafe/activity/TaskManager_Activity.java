package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yohnnsafe.ench_wu.yohnnsafe.R;

import java.util.List;

public class TaskManager_Activity extends Activity {
    @ViewInject(R.id.tv_task)
    private TextView tvTask;
    @ViewInject(R.id.tv_ram)
    private TextView tvRam;
    private int processCount;
//哈哈修改了
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        initDate();
        initUI();
    }

    private void initDate() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        //获取进程
        processCount = runningAppProcesses.size();
        System.out.println("processCount:" + processCount);
        //获取当前可用的RAM
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);

        long availMem = outInfo.availMem;
        System.out.println("availMem" + availMem / 1024 + "M");

    }

    public void initUI() {

        ViewUtils.inject(this);
        tvTask.setText("进程:" + processCount);
    }

}
