package sync2app.com.syncapplive.additionalSettings.myService

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DownloadHelper
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.ZipDownloader
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import java.io.File


class MyApiService : Service() {
    private lateinit var mUserViewModel: FilesViewModel

    private var currentDownloadIndex = 0
    private var totalFiles: Int = 0

    private var countdownTimer: CountDownTimer? = null
    private var countRefresh: CountDownTimer? = null


    var lauchWebView = false




    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate() {
        super.onCreate()
        mUserViewModel = FilesViewModel(application)

        val filter = IntentFilter(Constants.UpdateTimmer_Reciver)
        registerReceiver(UpdateTimmerBroad_Reciver, filter)


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground()

        } else {
            startForeground(1, Notification())

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        currentDownloadIndex = 0
        val editor = myDownloadClass.edit()
        editor.remove(Constants.SynC_Status)
        editor.remove(Constants.SAVED_CN_TIME)
        editor.remove(Constants.textDownladByes)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.filesChange)
        editor.remove(Constants.numberOfFiles)

        editor.apply()
        countdownTimer?.cancel()
        countRefresh?.cancel()


        showToastMessage("Sync On Interval Activated")


        myHandler.postDelayed(Runnable {

            val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
            val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
            val Zip = myDownloadClass.getString("Zip", "").toString()
            val fileName = myDownloadClass.getString("fileName", "").toString()
            val baseUrl = myDownloadClass.getString("baseUrl", "").toString()


            if (baseUrl.isNotEmpty() && getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty() && fileName.isNotEmpty() && Zip.isNotEmpty()) {

                //  showToastMessage("Sync Interval Stated")
                makeAPIRequest()
                SyncIntervalDownload()
                synReapeatTime();

                refeshingTimmer(1)

            } else {
                showToastMessage("Invalid Path Format")
            }


        }, 200)



        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {


        val getTimeDefined = myDownloadClass.getLong(Constants.getTimeDefined, 0)
        val valuesTime = myDownloadClass.getLong(Constants.getTimeDefined, 0).toString()


        var newsTitle: String


        if (getTimeDefined != 0L) {

            newsTitle = "Api Sync in $valuesTime Minute"

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


    }


    override fun onDestroy() {
        mUserViewModel.deleteAllFiles()

        val editor = myDownloadClass.edit()
        editor.remove(Constants.SynC_Status)
        editor.remove(Constants.SAVED_CN_TIME)
        editor.remove(Constants.textDownladByes)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.filesChange)
        editor.remove(Constants.numberOfFiles)

        editor.apply()
        countdownTimer?.cancel()
        countRefresh?.cancel()
        unregisterReceiver(UpdateTimmerBroad_Reciver)
        myHandler.removeCallbacksAndMessages(null)
    }

    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]

            getZipDownloads(file.SN, file.FolderName, file.FileName)

        } else {

            // All files are downloaded

            showToastMessage("All files are downloaded")

        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getDownloadMyCSV() {


        val intent22 = Intent(Constants.RECIVER_PROGRESS)
        intent22.putExtra(Constants.SynC_Status, Constants.PR_running)
        sendBroadcast(intent22)


        GlobalScope.launch(Dispatchers.IO) {

            val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "").toString()
            val getFolderSubpath =
                myDownloadClass.getString(Constants.getFolderSubpath, "").toString()
            val get_ModifiedUrl =
                myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()

            val lastEnd = "Start/start1.csv";
            val csvDownloader = CSVDownloader()
            val csvData =
                csvDownloader.downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath, lastEnd)
            saveURLPairs(csvData)


            withContext(Dispatchers.Main) {

                mUserViewModel.readAllData.observeForever { files ->
                    if (files.isNotEmpty()) {
                        downloadSequentially(files)

                        totalFiles = files.size.toInt()

                        val numberOfFiles = "NF : " + files.size + ""

                        val editor = myDownloadClass.edit()
                        editor.putString(Constants.numberOfFiles, numberOfFiles)
                        editor.apply()


                    } else {
                        showToastMessage("No files found")
                    }
                }
            }


        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getZipDownloads(sN: String, folderName: String, fileName: String) {

        Thread {

            val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "").toString()
            val getFolderSubpath =
                myDownloadClass.getString(Constants.getFolderSubpath, "").toString()
            val get_ModifiedUrl =
                myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()


            val Syn2AppLive = Constants.Syn2AppLive
            val saveMyFileToStorage = "/$Syn2AppLive/$getFolderClo/$getFolderSubpath/$folderName"

            // Adjusting the file path to save the downloaded file
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                saveMyFileToStorage
            )
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val getFile_name = fileName.toString()
            val getFileUrl =
                "$get_ModifiedUrl/$getFolderClo/$getFolderSubpath/$folderName/$fileName"
            val fileDestination = File(dir.absolutePath, getFile_name)


            //start download
            ZipDownloader(object : DownloadHelper {
                @SuppressLint("SetTextI18n")
                override fun afterExecutionIsComplete() {


                        currentDownloadIndex++
                        downloadSequentially(mUserViewModel.readAllData.value ?: emptyList())


                        val totalPercentage =
                            ((sN.toDouble() / totalFiles.toDouble()) * 100).toInt()

                        ///   showToastMessage(totalPercentage.toString())
                        val editor = myDownloadClass.edit()
                        editor.putString(Constants.textDownladByes, "$totalPercentage%")
                        editor.putString(Constants.progressBarPref, "$totalPercentage")
                        editor.putString(Constants.SynC_Status, Constants.PR_running)
                        editor.apply()

                        if (sN == totalFiles.toString()) {

                            editor.putString(Constants.textDownladByes, "100%")
                            editor.putString(Constants.progressBarPref, "100")
                            editor.putString(Constants.SynC_Status, "Completed")
                            editor.apply()
                            mUserViewModel.deleteAllFiles()


                            myHandler.postDelayed(Runnable {
                                stratMyACtivity()
                            }, 1000)

                        }
                    }


                override fun whenExecutionStarts() {


                }

                @SuppressLint("SetTextI18n")
                override fun whileInProgress(i: Int) {

                    val filesCounts = "CF : $sN"
                    val editor = myDownloadClass.edit()
                    editor.putString(Constants.filesChange, filesCounts)
                    editor.apply()


                }
            }).execute(getFileUrl, fileDestination.absolutePath)
        }.start()
    }

    private fun showToastMessage(messages: String) {
        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun saveURLPairs(csvData: String) {
        val pairs = parseCSV(csvData)

        // Add files to Room Database
        GlobalScope.launch(Dispatchers.IO) {
            for ((index, line) in pairs.withIndex()) {
                val parts = line.split(",").map { it.trim() }
                if (parts.size < 2) continue // Skip lines with insufficient data

                val sn = parts[0].toIntOrNull() ?: continue // Skip lines with invalid SN
                val folderAndFile = parts[1].split("/")

                val folderName = if (folderAndFile.size > 1) {
                    folderAndFile.dropLast(1).joinToString("/")
                } else {
                    "MyApiFolder" // Assuming default folder name
                }

                val fileName =
                    folderAndFile.lastOrNull() ?: continue // Skip lines with missing file name
                val status = "" // Set your status here

                val files = FilesApi(
                    SN = sn.toString(),
                    FolderName = folderName,
                    FileName = fileName,
                    Status = status
                )
                mUserViewModel.addFiles(files)
            }
        }
    }


    // for no need of comma CSV
    private fun parseCSV(csvData: String): List<String> {
        val pairs = mutableListOf<String>()
        val lines = csvData.split("\n")
        for (line in lines) {
            if (line.isNotBlank()) {
                pairs.add(line.trim())
            }
        }
        return pairs
    }


    private fun makeAPIRequest() {

        val connectivityManager22: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo

        if (networkInfo22 != null && networkInfo22.isConnected) {

            getDownloadMyCSV()
        } else {
            showToastMessage("No Internet Connection")
        }
    }


    private fun SyncIntervalDownload() {
        val getTimeDefined = myDownloadClass.getLong(Constants.getTimeDefined, 0)

        val connectivityManager22: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo



        if (getTimeDefined != 0L) {

            attemptRequestAgain(getTimeDefined)

            if (networkInfo22 != null && networkInfo22.isConnected) {
                // showToastMessage("System will Sync in $getTimeDefined minute")
            } else {
                showToastMessage("No internet Connection")
            }

        }


    }


    private fun stratMyACtivity() {
        try {


            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            intent22.putExtra(Constants.SynC_Status, Constants.PR_running)
            sendBroadcast(intent22)

            val editor = myDownloadClass.edit()
            editor.putString(Constants.SynC_Status, Constants.PR_running)
            editor.apply()


            // also update the web-view right away
            val intent = Intent(Constants.SEND_SERVICE_NOTIFY)
            sendBroadcast(intent)


            // send to updte the timmer
            val intent333 = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
            sendBroadcast(intent333)

            lauchWebView = true

            currentDownloadIndex = 0

        } catch (_: Exception) {
        }
    }


    private fun attemptRequestAgain(minutes: Long) {
        val milliseconds = minutes * 60 * 1000 // Convert minutes to
        val editor = myDownloadClass.edit()

        countdownTimer = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                try {
                    countdownTimer?.cancel()

                    val connectivityManager22: ConnectivityManager =
                        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo

                    if (networkInfo22 != null && networkInfo22.isConnected) {

                        if (lauchWebView == true) {

                            val intent22 = Intent(Constants.RECIVER_PROGRESS)
                            intent22.putExtra(Constants.SynC_Status, Constants.PR_Refresh)
                            sendBroadcast(intent22)

                            val editor = myDownloadClass.edit()
                            editor.putString(Constants.SynC_Status, Constants.PR_Refresh)
                            editor.apply()

                            makeAPIRequest()

                            synReapeatTime()
                            lauchWebView = false

                            val intent = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                            sendBroadcast(intent)

                        } else {
                            showToastMessage("Sync Already in Progress")
                            //  showToastMessage("DL error")
                            synReapeatTime()

                            val intent = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                            sendBroadcast(intent)


                            val intent22 = Intent(Constants.RECIVER_PROGRESS)
                            sendBroadcast(intent22)

                        }

                    } else {
                        showToastMessage("No Internet Connection")

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
                showToastMessage("Sync Time Updated")
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

        val getTimeDefined = my_DownloadClass.getLong(Constants.getTimeDefined, 0)

        if (getTimeDefined != 0L) {
            startOrResumeTimer(getTimeDefined)
        }

    }


    private fun refeshingTimmer(minutes: Long) {
        val milliseconds = minutes * 20 * 1000 // Convert minutes to

        countRefresh = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {

                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                sendBroadcast(intent22)
                countRefresh!!.cancel()

                refeshingTimmer(1)


            }

            @SuppressLint("SuspiciousIndentation")
            override fun onTick(millisUntilFinished: Long) {
            }
        }
        countRefresh?.start()
    }



}










