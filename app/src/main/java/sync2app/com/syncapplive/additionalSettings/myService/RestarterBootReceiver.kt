package sync2app.com.syncapplive.additionalSettings.myService

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils

class RestarterBootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {

        if (intent!!.action.equals("RestarterBootReceiver")) {
            if (!ServiceUtils.foregroundServiceRunning(context)) {
                context.applicationContext.startService(
                    Intent(
                        context.applicationContext, NotificationService::class.java
                    )
                )
            }

            if (!ServiceUtils.foregroundServiceRunningOnChange(context)) {
                context.applicationContext.startService(
                    Intent(
                        context.applicationContext, OnChnageService::class.java
                    )
                )
            }



        }

    }

}