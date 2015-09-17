package com.yohnnsafe.ench_wu.yohnnsafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author:ench_wu
 * Created on 2015/9/16.
 */
public class TaskInfos {

    private static PackageManager pm;
    private static ActivityManager activityManager;

    public static List<TaskInfo> getTaskInfos(Context context) {
        pm = context.getPackageManager();

        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();

        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcesse : runningAppProcesses
                ) {

            TaskInfo taskInfo = new TaskInfo();
            String processName = runningAppProcesse.processName;
            taskInfo.setPackageName(processName);
            try {

                // 获取到内存基本信息
                /**
                 * 这个里面一共只有一个数据
                 */
                MemoryInfo[] memoryInfo = activityManager
                        .getProcessMemoryInfo(new int[]{runningAppProcesse.pid});
                // Dirty弄脏
                // 获取到总共弄脏多少内存(当前应用程序占用多少内存)
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;


//				System.out.println("==========="+totalPrivateDirty);

                taskInfo.setMemorySize(totalPrivateDirty);



                PackageInfo packageInfo = pm.getPackageInfo(processName, 0);

                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);

                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.setAppName(appName);

                int flags = packageInfo.applicationInfo.flags;
                if ((flags& ApplicationInfo.FLAG_SYSTEM)!=0){
                    taskInfo.setUserApp(false);
                }else {
                    taskInfo.setUserApp(true);
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.setAppName(processName);
                taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
            }

            taskInfos.add(taskInfo);
        }

        return taskInfos;
    }
}
