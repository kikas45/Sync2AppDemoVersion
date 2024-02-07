package sync2app.com.syncapplive.additionalSettings.myService

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import java.io.File
import java.io.FileInputStream
import java.util.Objects
import java.util.zip.ZipInputStream

class NotificationService : Service() {


    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    var lauchWebView = false


    var repEatMyProcess = false

    // private ConnectivityReceiver connectivityReceiver;
    private var countdownTimer: CountDownTimer? = null

    val connectivityManager: ConnectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    val networkInfo: NetworkInfo? by lazy {
        connectivityManager.activeNetworkInfo
    }


    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }


    override fun onCreate() {
        super.onCreate()


        registerReceiver(
            downloadCompleteReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        val filter222 = IntentFilter(Constants.UpdateTimmer_Reciver)
        registerReceiver(UpdateTimmerBroad_Reciver, filter222)


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground()

        } else {
            startForeground(1, Notification())

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        myHandler.postDelayed(Runnable {

            val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
            val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
            val Zip = myDownloadClass.getString("Zip", "").toString()
            val fileName = myDownloadClass.getString("fileName", "").toString()
            val Extracted = myDownloadClass.getString("Extracted", "").toString()
            val baseUrl = myDownloadClass.getString("baseUrl", "").toString()


            if (baseUrl.isNotEmpty() && getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty() && fileName.isNotEmpty() && Zip.isNotEmpty()) {

                showToastMessage("Sync Interval Stated")
                makeAPIRequest()
                SyncIntervalDownload()
                synReapeatTime();

            } else {
                showToastMessage("Invalid Path Format")
            }


        }, 200)




        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onDestroy() {
        Toast.makeText(this, "Sync Cancelled", Toast.LENGTH_SHORT).show()
        val editor = myDownloadClass.edit()
        editor.remove(Constants.SynC_Status)
        editor.remove(Constants.SAVED_CN_TIME)
        editor.apply()
        countdownTimer?.cancel()
        unregisterReceiver(downloadCompleteReceiver)
        unregisterReceiver(UpdateTimmerBroad_Reciver)
        myHandler.removeCallbacksAndMessages(null)


    }


    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {

                if (repEatMyProcess == true) {
                    funUnZipFile()
                    repEatMyProcess = false
                }


            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {


        val getTimeDefined = myDownloadClass.getString(Constants.getTimeDefined, "")

        var newsTitle: String

        if (getTimeDefined.equals("Sync interval timer")) {
            newsTitle = "Sync in  1 Minute"
        } else {
            newsTitle = "Sync in $getTimeDefined"
        }


        val builder = NotificationCompat.Builder(applicationContext, "ChannelId")
            .setSmallIcon(R.drawable.img_logo_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(newsTitle)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ChannelId", "News", importance)

            notificationManager.createNotificationChannel(channel)

            startForeground(2, builder.build())
        }
    }


    private fun makeAPIRequest() {


        val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
        val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
        val Zip = myDownloadClass.getString("Zip", "").toString()
        val fileName = myDownloadClass.getString("fileName", "").toString()
        val Extracted = myDownloadClass.getString("Extracted", "").toString()
        val baseUrl = myDownloadClass.getString("baseUrl", "").toString()

        download(baseUrl, getFolderClo, getFolderSubpath, Zip, fileName, Extracted)
    }


    private fun download(
        url: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileNamy: String,
        Extracted: String,
    ) {


        val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"

        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive$DeleteFolderPath"
        val file = File(directoryPath)
        delete(file)



        myHandler.postDelayed(Runnable {

            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
            val Syn2AppLive = "Syn2AppLive"

            val editior = myDownloadClass.edit()
            editior.putString(Constants.getFolderClo, getFolderClo)
            editior.putString(Constants.getFolderSubpath, getFolderSubpath)
            editior.putString("Zip", Zip)
            editior.putString("fileNamy", fileNamy)
            editior.putString("Extracted", Extracted)
            editior.putString("baseUrl", url)
            editior.apply()


            val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val folder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Download/$Syn2AppLive/$finalFolderPath"
            )

            if (!folder.exists()) {
                folder.mkdirs()
            }

            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(fileNamy)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "/$Syn2AppLive/$finalFolderPath/$fileNamy"
            )
            val downloadReferenceMain = managerDownload.enqueue(request)

            val editor = myDownloadClass.edit()
            editor.putLong(Constants.downloadKey, downloadReferenceMain)
            editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
            editor.apply()


            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            intent22.putExtra(Constants.SynC_Status, Constants.PR_Downloading)
            sendBroadcast(intent22)



            repEatMyProcess = true

        }, 1000)


    }


    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        } else if (file.isDirectory) {
            for (subFile in Objects.requireNonNull(file.listFiles())) {
                if (!delete(subFile)) return false
            }
            return file.delete()
        }
        return false
    }


    private fun SyncIntervalDownload() {
        val getTimeDefined = myDownloadClass.getString(Constants.getTimeDefined, "")


        if (getTimeDefined.equals(Constants.t_2min)) {
            attemptRequestAgain(2)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 2 minute")
            } else {
                showToastMessage("No internet Connection") 
            }


        } else if (getTimeDefined.equals(Constants.t_5min)) {
            attemptRequestAgain(5)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 5 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_10min)) {
            attemptRequestAgain(10)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 10 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_15min)) {
            attemptRequestAgain(15)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 15 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_30min)) {

            attemptRequestAgain(30)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 30 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_60min)) {
            attemptRequestAgain(60)

            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 60 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_120min)) {
            attemptRequestAgain(120)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 120 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_180min)) {
            attemptRequestAgain(180)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 180 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else if (getTimeDefined.equals(Constants.t_240min)) {
            attemptRequestAgain(240)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 240 minute")
            } else {
                showToastMessage("No internet Connection")
            }


        } else {
            attemptRequestAgain(1)
            if (networkInfo != null && networkInfo!!.isConnected) {
                showToastMessage("System will Sync in 1 Minute Default time")
            } else {
                showToastMessage("No internet Connection")
            }


        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun funUnZipFile() {
        try {
            // Retrieve your parameters here...

            GlobalScope.launch(Dispatchers.IO) {
                val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "")
                val getFolderSubpath = myDownloadClass.getString(Constants.getFolderSubpath, "")
                val Zip = myDownloadClass.getString("Zip", "")
                val fileNamy = myDownloadClass.getString("fileNamy", "")
                val Extracted = myDownloadClass.getString("Extracted", "")


                val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
                val finalFolderPathDesired = "/$getFolderClo/$getFolderSubpath/$Extracted"


                val directoryPathString =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPath
                val destinationFolder =
                    File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPathDesired)

                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs()
                }

                val myFile = File(directoryPathString, File.separator + fileNamy)
                //  val myFile = File(directoryPathString, fileNamy)
                if (myFile.exists()) {
                    extractZip(myFile.toString(), destinationFolder.toString())
                } else {
                    withContext(Dispatchers.Main) {
                        //  showToastMessage("Zip file could not be found")

                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        intent22.putExtra(Constants.SynC_Status, Constants.PR_Zip_error)
                        sendBroadcast(intent22)


                        val editor = myDownloadClass.edit()
                        editor.putString(Constants.SynC_Status, Constants.PR_Zip_error)
                        editor.apply()
                    }
                }
            }
        } catch (_: Exception) {
            // Handle exceptions if necessary
        }
    }

    suspend fun extractZip(zipFilePath: String, destinationPath: String) {
        try {
            withContext(Dispatchers.Main) {
                showToastMessage("Zip extraction started")
                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                intent22.putExtra(Constants.SynC_Status, Constants.PR_Extracting)
                sendBroadcast(intent22)

                val editor = myDownloadClass.edit()
                editor.putString(Constants.SynC_Status, Constants.PR_Extracting)
                editor.apply()

            }

            val buffer = ByteArray(1024)

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


                // Notify MediaScanner about the extracted file
                MediaScannerConnection.scanFile(
                    applicationContext,
                    arrayOf(entryFile.absolutePath),
                    null,
                    object : MediaScannerConnection.OnScanCompletedListener {
                        override fun onScanCompleted(path: String?, uri: Uri?) {
                            Log.i("ExternalStorage", "Scanned $path:")
                            Log.i("ExternalStorage", "-> uri=$uri")
                        }
                    }
                )

            }

            zipInputStream.close()

            withContext(Dispatchers.Main) {
                stratMyACtivity()
                showToastMessage("Zip completed")

            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                showToastMessage("Error during zip extraction")
                stratMyACtivity()
            }
        }
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }

    private fun stratMyACtivity() {
        try {

            lauchWebView = true


            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            intent22.putExtra(Constants.SynC_Status, Constants.PR_running)
            sendBroadcast(intent22)

            val editor = myDownloadClass.edit()
            editor.putString(Constants.SynC_Status, Constants.PR_running)
            editor.apply()


        } catch (_: Exception) {
        }
    }


    private fun attemptRequestAgain(minutes: Long) {
        val milliseconds = minutes * 60 * 1000 // Convert minutes to

        val editor = myDownloadClass.edit()
        editor.putLong(Constants.SaVed_CN_Time, System.currentTimeMillis())
        editor.apply()


        countdownTimer = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                try {
                    countdownTimer?.cancel()

                    if (lauchWebView == true) {

                        // for loading webvuiew after sync
                        val intent = Intent(Constants.SEND_SERVICE_NOTIFY)
                        sendBroadcast(intent)

                        // for getting states
                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        intent22.putExtra(Constants.SynC_Status, Constants.PR_Refresh)
                        sendBroadcast(intent22)

                        val editor = myDownloadClass.edit()
                        editor.putString(Constants.SynC_Status, Constants.PR_Refresh)
                        editor.apply()

                        makeAPIRequest()

                        synReapeatTime()
                        lauchWebView = false

                    } else {
                        //  showToastMessage("Sync Already in Progress")
                        synReapeatTime()

                        val intent = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                        sendBroadcast(intent)


                    }

                    attemptRequestAgain(minutes)

                } catch (ignored: java.lang.Exception) {
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                try {


                    val totalSecondsRemaining = millisUntilFinished / 1000
                    var minutesUntilFinished = totalSecondsRemaining / 60
                    var remainingSeconds = totalSecondsRemaining % 60

                    // Adjusting minutes if seconds are in the range of 0-59
                    if (remainingSeconds == 0L && minutesUntilFinished > 0) {
                        minutesUntilFinished--
                        remainingSeconds = 59
                    }
                    val displayText =
                        String.format("CD: %d:%02d", minutesUntilFinished, remainingSeconds)
                    //  showToastMessage(displayText)

                } catch (ignored: java.lang.Exception) {
                }
            }
        }
        countdownTimer?.start()
    }


    private val UpdateTimmerBroad_Reciver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null && intent.action == Constants.UpdateTimmer_Reciver) {
                countdownTimer?.cancel()

                SyncIntervalDownload();
                showToastMessage("Update Sync Time")
                synReapeatTime()

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    startMyOwnForeground()

                } else {
                    startForeground(1, Notification())

                }


            }
        }
    }

    private fun startOrResumeTimer(minutes: Long) {
        val milliseconds = minutes * 60 * 1000
        if (milliseconds > 0) {
            val savedTime = System.currentTimeMillis() + milliseconds
            val editor = myDownloadClass.edit()
            editor.putLong(Constants.SAVED_CN_TIME, savedTime)
            editor.apply()
        }
    }


    private fun synReapeatTime() {
        val my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, MODE_PRIVATE)
        val getTimeDefined = my_DownloadClass.getString(Constants.getTimeDefined, "")
        if (getTimeDefined == Constants.t_2min) {
            startOrResumeTimer(2)
        } else if (getTimeDefined == Constants.t_5min) {
            startOrResumeTimer(5)
        } else if (getTimeDefined == Constants.t_10min) {
            startOrResumeTimer(10)
        } else if (getTimeDefined == Constants.t_15min) {
            startOrResumeTimer(15)
        } else if (getTimeDefined == Constants.t_30min) {
            startOrResumeTimer(30)
        } else if (getTimeDefined == Constants.t_60min) {
            startOrResumeTimer(60)
        } else if (getTimeDefined == Constants.t_120min) {
            startOrResumeTimer(120)
        } else if (getTimeDefined == Constants.t_180min) {
            startOrResumeTimer(180)
        } else if (getTimeDefined == Constants.t_240min) {
            startOrResumeTimer(240)
        } else if (getTimeDefined == Constants.Sync_interval_timer) {
            startOrResumeTimer(1)
        }
    }


}




