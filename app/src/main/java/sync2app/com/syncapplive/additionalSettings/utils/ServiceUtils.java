package sync2app.com.syncapplive.additionalSettings.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval;
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService;

public class ServiceUtils {

    public static boolean foregroundServiceRunningOnChange(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
            for (ActivityManager.RunningServiceInfo service : runningServices) {
                if (OnChnageService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

        public static boolean foregroundServiceRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
            for (ActivityManager.RunningServiceInfo service : runningServices) {
                if (SyncInterval.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }



}
