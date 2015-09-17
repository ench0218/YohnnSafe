package com.yohnnsafe.ench_wu.yohnnsafe.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

/**
 * author:ench_wu
 * Created on 2015/9/16.
 */
public class SystemInfoUtils {

    /**
     * 获取进程数
     * @param context
     * @return
     */
    public static int getProcessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        //获取进程数

        return  runningAppProcesses.size();
    }

    /**
     * 获取当前可用内存
     * @param context
     * @return
     */
    public static long getAvailMem(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return  memoryInfo.availMem;
    }

    /**
     * 获取总内存
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalMem(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return  memoryInfo.totalMem;
    }



}
