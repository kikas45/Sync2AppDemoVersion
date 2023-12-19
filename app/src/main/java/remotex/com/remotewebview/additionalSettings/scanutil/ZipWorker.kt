package remotex.com.remotewebview.additionalSettings.scanutil

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import remotex.com.remotewebview.WebActivity
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream

class ZipWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {

            val zipFilePath = inputData.getString("zipFilePath")
            val destinationPath = inputData.getString("destinationPath")

            funUnZipFile(zipFilePath!!, destinationPath!!)
            
            Result.success()
        } catch (e: Exception) {

            Result.failure()
        }

    }


    private fun funUnZipFile(zipFilePath: String, destinationPath: String) {
        try {
            val folderToPickFrom = File(zipFilePath)
            val folderToExtractTo = File(destinationPath)

            // Check if the zip file exists
            if (folderToExtractTo.exists()) {
                folderToExtractTo.listFiles()?.forEach { it.delete() }
            }

            // Extract the new zip file
            extractZip(folderToPickFrom.path, folderToExtractTo.path)

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions as needed
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun extractZip(zipFilePath: String, destinationPath: String) {
        val buffer = ByteArray(1024)

        try {
            showNotification("Zip extraction started")

            val zipInputStream = ZipInputStream(FileInputStream(zipFilePath))
            var entry = zipInputStream.nextEntry

            while (entry != null) {
                val entryFile = File(destinationPath, entry.name)
                val entryDir = File(entryFile.parent)

                if (!entryDir.exists()) {
                    entryDir.mkdirs()
                }

                val outputStream = entryFile.outputStream()

                var len = zipInputStream.read(buffer)
                while (len > 0) {
                    outputStream.write(buffer, 0, len)
                    len = zipInputStream.read(buffer)
                }

                outputStream.close()
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }

            zipInputStream.close()

            showNotification("Zip extraction completed")

//            try {
//                applicationContext.startActivity(Intent(applicationContext, WebActivity::class.java))
//            }catch (_:Exception){}


        } catch (e: Exception) {
            e.printStackTrace()

          showNotification("Error during zip extraction")
//            try {
//                applicationContext.startActivity(Intent(applicationContext, WebActivity::class.java))
//            }catch (_:Exception){}


            Log.d("ZIppppp", "extractZip: ${e.message}")
        }
    }



    private fun showNotification(message: String) {
        try {

            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create a notification channel for Android Oreo and higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "zip_extraction_channel"
                val channelName = "Zip Extraction Channel"
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            // Build the notification
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "zip_extraction_channel")
                .setContentTitle("Zip Extraction")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Show the notification
            notificationManager.notify(1, notificationBuilder.build())
        } catch (e: Exception) {
            Log.e("NOTIFICATION", "Error showing notification", e)
        }
        Log.d("NOTIFICATION", message)
    }
}