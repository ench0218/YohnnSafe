package com.yohnnsafe.ench_wu.yohnnsafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Ench_Wu on 2015/9/7.
 */
public class ServiceUtils {

    public static boolean isServiceRunning(Context context,String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices
                ) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)){
                return true;
            }
        }

        return false;

    }
}
