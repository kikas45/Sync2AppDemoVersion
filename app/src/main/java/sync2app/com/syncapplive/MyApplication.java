package sync2app.com.syncapplive;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sync2app.com.syncapplive.additionalSettings.CrashReportDB.CrashHandler;
import sync2app.com.syncapplive.additionalSettings.myService.NotificationService;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;


public class MyApplication extends MultiDexApplication {

    private static Application instance;

    private static int numberOfRunningActivities = 0;



    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(instance));

                if (!(constants.OnesigID ==null)){
                    OneSignal.initWithContext(MyApplication.this);
                    OneSignal.setAppId(constants.OnesigID);
                }

            }
        }, 3000);


        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {
                String launchURL = result.getNotification().getLaunchURL();

                if (launchURL != null) {
//                    Log.d(Const.DEBUG, "Launch URL: " + launchURL);
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", launchURL);
                    startActivity(intent);
                }
            }
        });
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }


    public static void incrementRunningActivities() {
        numberOfRunningActivities++;
    }

    public static void decrementRunningActivities() {
        numberOfRunningActivities--;
        // Check if all activities are stopped
        if (numberOfRunningActivities == 0) {
            // Stop the service
            instance.stopService(new Intent(instance.getApplicationContext(), NotificationService.class));
        }
    }


}