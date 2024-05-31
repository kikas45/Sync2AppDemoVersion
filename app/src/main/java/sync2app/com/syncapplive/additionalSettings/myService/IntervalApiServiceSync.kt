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
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myFailedDownloadfiles.DnFailedApi
import sync2app.com.syncapplive.additionalSettings.myFailedDownloadfiles.DnFailedViewModel
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import java.io.File
import java.util.Objects


class IntervalApiServiceSync : Service() {

    private lateinit var mFilesViewModel: FilesViewModel
    private lateinit var dnFailedViewModel: DnFailedViewModel

    private var currentDownloadIndex = 0
    private var totalFiles: Int = 0
    private var downloadedFilesCount = 0
    private var isFailedDownload = false
    private var isWebViewReadyForLaunch = false

    private var countdownTimer: CountDownTimer? = null
    private var countRefresh: CountDownTimer? = null


    var manager: DownloadManager? = null


    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    override fun onCreate() {
        super.onCreate()



        mFilesViewModel = FilesViewModel(application)
        dnFailedViewModel = DnFailedViewModel(application)

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
        // editor.remove(Constants.SAVED_CN_TIME)
        editor.remove(Constants.textDownladByes)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.filesChange)
        editor.remove(Constants.numberOfFiles)

        editor.apply()
        countdownTimer?.cancel()
        countRefresh?.cancel()


        showToastMessage("Sync On Interval Activated")

        second_cancel_ongoing_download()

        myHandler.postDelayed(Runnable {

            val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
            val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
            // val Zip = myDownloadClass.getString("Zip", "").toString()
            val fileName = myDownloadClass.getString("fileName", "").toString()
            val baseUrl = myDownloadClass.getString("baseUrl", "").toString()

            val editor22 = myDownloadClass.edit()
            editor22.putString(Constants.Zip, "Api")
            editor22.apply()

            if (baseUrl.isNotEmpty() && getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty() && fileName.isNotEmpty()) {

                //  showToastMessage("Sync Interval Stated")
                makeAPIRequest()
                SyncIntervalDownload()
                synReapeatTime();


            } else {
                showToastMessage("Invalid Path Format")
            }


        }, 500)



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

            newsTitle = "Sync Interval Api in  $valuesTime Minute"

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


        try {
            if (downloadCompleteReceiver != null) {
                unregisterReceiver(downloadCompleteReceiver)
            }

            if (UpdateTimmerBroad_Reciver != null) {
                unregisterReceiver(UpdateTimmerBroad_Reciver)
            }


            mFilesViewModel.deleteAllFiles()
            dnFailedViewModel.deleteAllFiles()

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
            myHandler.removeCallbacksAndMessages(null)
            handler.removeCallbacksAndMessages(null)


        }catch (e:Exception){}
    }

    private fun downloadSequentiallyManually(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]
            handler.postDelayed(Runnable {

                getZipDownloads_Manual(file.SN, file.FolderName, file.FileName)

            }, 500)


        }


    }


    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]
            handler.postDelayed(Runnable {

                getZipDownloads(file.SN, file.FolderName, file.FileName)

            }, 500)


        }


    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun ManageGetDownloadMyCSVManual(baseUrl: String) {


        GlobalScope.launch {
            val result = checkUrlExistence(baseUrl)
            if (result) {
                manageDownload_for_Manual(baseUrl)
            } else {
                withContext(Dispatchers.Main) {
                    showToastMessage("Invalid url")
                }
            }
        }


    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun ManageGetDownloadMyCSV() {


        val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
        val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()


        val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

        val get_imagSwtichEnableSyncFromAPI =
            sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")

        if (!get_imagSwtichEnableSyncFromAPI.equals(Constants.imagSwtichEnableSyncFromAPI)) {

            // when enable Sync Zip is  toggle On from Syn manager Page
            if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {

                val lastEnd = "/Api/update1.csv"
                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/$lastEnd"
                val get_ModifiedUrl = "https://cp.cloudappserver.co.uk/app_base/public/"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {
                        manageDownload(get_ModifiedUrl, getFolderClo, getFolderSubpath)

                        withContext(Dispatchers.Main) {
                            //  showToastMessage("Partner (API) interval $baseUrl")
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            showToastMessage("Invalid url")
                        }
                    }
                }


            } else {

                // No partner url

                val get_tMaster: String =
                    myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
                val lastEnd = "/Api/update1.csv"
                val baseUrl = "$get_tMaster/$getFolderClo/$getFolderSubpath/$lastEnd"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {

                        manageDownload(get_tMaster, getFolderClo, getFolderSubpath)

                        withContext(Dispatchers.Main) {
                            // showToastMessage("Master (API)  interval $baseUrl")
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            showToastMessage("Invalid url")
                        }
                    }
                }

            }
        }


    }


    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    private fun manageDownload_for_Manual(get_getSavedEditTextInputSynUrlZip: String) {

        val intent22 = Intent(Constants.RECIVER_PROGRESS)
        sendBroadcast(intent22)
        val editor = myDownloadClass.edit()
        editor.putString(Constants.SynC_Status, Constants.PR_checking)
        editor.apply()


        mFilesViewModel.deleteAllFiles()
        dnFailedViewModel.deleteAllFiles()


        GlobalScope.launch(Dispatchers.IO) {
            val csvDownloader = CSVDownloader()
            val csvData = csvDownloader.downloadCSV(get_getSavedEditTextInputSynUrlZip, "", "", "")
            saveURLPairs(csvData)

            val intent = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent)

            editor.putString(Constants.SynC_Status, Constants.PR_preparing)
            editor.apply()

            myHandler.postDelayed(runnableManual, 4000)

        }

    }


    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    private fun manageDownload(
        get_ModifiedUrl: String,
        getFolderClo: String,
        getFolderSubpath: String,
    ) {

        val intent22 = Intent(Constants.RECIVER_PROGRESS)
        sendBroadcast(intent22)
        val editor = myDownloadClass.edit()
        editor.putString(Constants.SynC_Status, Constants.PR_checking)
        editor.apply()

        mFilesViewModel.deleteAllFiles()
        dnFailedViewModel.deleteAllFiles()

        GlobalScope.launch(Dispatchers.IO) {
            val lastEnd = "/Api/update1.csv"
            //  val lastEnd = "/Start/start1.csv"
            val csvDownloader = CSVDownloader()
            val csvData =
                csvDownloader.downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath, lastEnd)
            saveURLPairs(csvData)

            val intent = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent)

            editor.putString(Constants.SynC_Status, Constants.PR_preparing)
            editor.apply()

            myHandler.postDelayed(runnable, 4000)

        }

    }


    private val runnable: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {

            mFilesViewModel.readAllData.observeForever { files ->
                if (files.isNotEmpty()) {
                    myHandler.postDelayed(Runnable {
                    downloadSequentially(files)

                    totalFiles = files.size.toInt()

                    val numberOfFiles = files.size.toString()

                    val editor = myDownloadClass.edit()
                    editor.putString(Constants.numberOfFiles, numberOfFiles)
                    editor.putString(Constants.SynC_Status, Constants.PR_running)
                    editor.putString(Constants.filesChange, "")
                    editor.apply()

                    myHandler.postDelayed(Runnable {
                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        sendBroadcast(intent22)

                    }, 100)

                    }, 1000)
                } else {
                    ///   showToastMessage("No files found")
                }
            }
        }

    }


    private val runnableManual: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            totalFiles = 0
            mFilesViewModel.readAllData.observeForever { files ->
                if (files.isNotEmpty()) {
                    myHandler.postDelayed(Runnable {
                        downloadSequentiallyManually(files)

                        totalFiles = files.size.toInt()

                        val numberOfFiles = files.size.toString()

                        val editor = myDownloadClass.edit()
                        editor.putString(Constants.numberOfFiles, numberOfFiles)
                        editor.putString(Constants.SynC_Status, Constants.PR_running)
                        editor.putString(Constants.filesChange, "")
                        editor.apply()

                        myHandler.postDelayed(Runnable {
                            val intent22 = Intent(Constants.RECIVER_PROGRESS)
                            sendBroadcast(intent22)

                        }, 100)

                    }, 1000)

                } else {
                    ///   showToastMessage("No files found")
                }
            }
        }

    }


    private fun getZipDownloads_Manual(sN: String, folderName: String, fileName: String) {

        isWebViewReadyForLaunch = false

        val Syn2AppLive = Constants.Syn2AppLive
        val saveMyFileToStorage = "/$Syn2AppLive/CLO/MANUAL/DEMO/$folderName"

        val getSavedEditTextInputSynUrlZip =
            myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

        var replacedUrl = getSavedEditTextInputSynUrlZip


        if (getSavedEditTextInputSynUrlZip.contains("/Start/start1.csv")) {
            replacedUrl = getSavedEditTextInputSynUrlZip.replace(
                "/Start/start1.csv",
                "/$folderName/$fileName"
            )

        } else if (getSavedEditTextInputSynUrlZip.contains("/Api/update1.csv")) {
            replacedUrl =
                getSavedEditTextInputSynUrlZip.replace("/Api/update1.csv", "/$folderName/$fileName")
        } else {

            Log.d("getZipDownloadsManually", "Unable to replace this url")
        }


        // delete existing files first
        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/" + saveMyFileToStorage
        val myFile = File(directoryPath, fileName)
        delete(myFile)


        GlobalScope.launch {
            try {
                val result = checkUrlExistence(replacedUrl)
                if (result) {
                    handler.postDelayed(Runnable {

                        val editior = myDownloadClass.edit()
                        editior.putString(Constants.fileNumber, sN)
                        editior.putString(Constants.folderName, folderName)
                        editior.putString(Constants.fileName, fileName)
                        editior.apply()


                        val dir = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            saveMyFileToStorage
                        )
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }


                        val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

                        // save files to this folder
                        val folder = File(
                            Environment.getExternalStorageDirectory()
                                .toString() + "/Download/$saveMyFileToStorage"
                        )

                        if (!folder.exists()) {
                            folder.mkdirs()
                        }

                        val request = DownloadManager.Request(Uri.parse(replacedUrl))
                        request.setTitle(fileName)
                        request.allowScanningByMediaScanner()
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, "/$saveMyFileToStorage/$fileName"
                        )
                        val downloadReferenceMain = managerDownload.enqueue(request)

                        val editor = myDownloadClass.edit()
                        editor.putLong(Constants.downloadKey, downloadReferenceMain)
                        editor.apply()

                    }, 300)
                } else {

                    val editior = myDownloadClass.edit()
                    editior.putString(Constants.fileNumber, sN)
                    editior.putString(Constants.folderName, folderName)
                    editior.putString(Constants.fileName, fileName)
                    editior.apply()


                }
            } catch (e: Exception) {
            }

        }


    }


    @SuppressLint("SetTextI18n")
    private fun getZipDownloads(sn: String, folderName: String, fileName: String) {

        isWebViewReadyForLaunch = false

        val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "").toString()
        val getFolderSubpath = myDownloadClass.getString(Constants.getFolderSubpath, "").toString()
        val get_ModifiedUrl = myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()


        val Syn2AppLive = Constants.Syn2AppLive
        val saveMyFileToStorage = "/$Syn2AppLive/$getFolderClo/$getFolderSubpath/$folderName"


        // delete existing files first
        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/" + saveMyFileToStorage
        val myFile = File(directoryPath, fileName)
        delete(myFile)

        val getFileUrl = "$get_ModifiedUrl/$getFolderClo/$getFolderSubpath/$folderName/$fileName"

        GlobalScope.launch {
            try {
                val result = checkUrlExistence(getFileUrl)
                if (result) {
                    handler.postDelayed(Runnable {

                        val editior = myDownloadClass.edit()
                        editior.putString(Constants.fileNumber, sn)
                        editior.putString(Constants.folderName, folderName)
                        editior.putString(Constants.fileName, fileName)
                        editior.apply()


                        val dir = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            saveMyFileToStorage
                        )
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }


                        val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

                        // save files to this folder
                        val folder = File(
                            Environment.getExternalStorageDirectory()
                                .toString() + "/Download/$saveMyFileToStorage"
                        )

                        if (!folder.exists()) {
                            folder.mkdirs()
                        }

                        val request = DownloadManager.Request(Uri.parse(getFileUrl))
                        request.setTitle(fileName)
                        request.allowScanningByMediaScanner()
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, "/$saveMyFileToStorage/$fileName"
                        )
                        val downloadReferenceMain = managerDownload.enqueue(request)

                        val editor = myDownloadClass.edit()
                        editor.putLong(Constants.downloadKey, downloadReferenceMain)
                        editor.apply()

                    }, 300)
                } else {

                    val editior = myDownloadClass.edit()
                    editior.putString(Constants.fileNumber, sn)
                    editior.putString(Constants.folderName, folderName)
                    editior.putString(Constants.fileName, fileName)
                    editior.apply()

                }
            } catch (e: Exception) {
            }

        }


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


    private fun showToastMessage(messages: String) {
        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun saveURLPairs(csvData: String) {


        val pairs = parseCSV(csvData)

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
                val id = System.currentTimeMillis()
                val files = FilesApi(
                    id,
                    SN = sn.toString(),
                    FolderName = folderName,
                    FileName = fileName,
                    Status = status
                )
                mFilesViewModel.addFiles(files)


                val dnFailedApi = DnFailedApi(
                    SN = sn.toString(),
                    FolderName = folderName,
                    FileName = fileName,
                    Status = status
                )
                dnFailedViewModel.addFiles(dnFailedApi)


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
        val filter222 = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteReceiver, filter222)

        val connectivityManager22: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo

        if (networkInfo22 != null && networkInfo22.isConnected) {


            isWebViewReadyForLaunch = false

            mFilesViewModel.deleteAllFiles()
            dnFailedViewModel.deleteAllFiles()
            totalFiles = 0
            currentDownloadIndex = 0

            val imagUsemanualOrnotuseManual =
                sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

            if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {

                handler.postDelayed(Runnable {
                    val getSavedEditTextInputSynUrlZip =
                        myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "")
                            .toString()

                    var replacedUrl = getSavedEditTextInputSynUrlZip

                    if (getSavedEditTextInputSynUrlZip.contains("/Start/start1.csv") || getSavedEditTextInputSynUrlZip.contains(
                            "/Api/update1.csv"
                        )
                    ) {
                        replacedUrl = getSavedEditTextInputSynUrlZip.replace(
                            "/Start/start1.csv",
                            "/Api/update1.csv"
                        )
                        ManageGetDownloadMyCSVManual(replacedUrl)
                        //  showToastMessage("Interval Api $replacedUrl")

                    } else {
                        Log.d(
                            "getZipDownloadsManually",
                            "Unable to replace this url for update.csv"
                        )
                        // showToastMessage("Unable to read CSV file from locatio
                    }


                }, 1000)


            } else {

                handler.postDelayed(Runnable {
                    ManageGetDownloadMyCSV()
                }, 1000)
            }


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


    private fun startWebViewRefresh() {
        try {

            val editor33 = myDownloadClass.edit()
            editor33.putString(Constants.SynC_Status, Constants.PR_running)
            editor33.apply()

            currentDownloadIndex =0
            downloadedFilesCount =0
            totalFiles = 0

            myHandler.postDelayed(Runnable {

                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                sendBroadcast(intent22)


                // also update the web-view right away
                val intent = Intent(Constants.SEND_SERVICE_NOTIFY)
                sendBroadcast(intent)


                // send to updte the timmer
                val intent333 = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                sendBroadcast(intent333)


                if (downloadCompleteReceiver == null){
                    val filter222 = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    registerReceiver(downloadCompleteReceiver, filter222)

                }

                isWebViewReadyForLaunch = true
                
            }, 1000)

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

                        if (isWebViewReadyForLaunch == true) {
                            second_cancel_ongoing_download()

                            myHandler.postDelayed(Runnable {
                                val editor = myDownloadClass.edit()
                                editor.putString(Constants.SynC_Status, Constants.PR_checking)
                                editor.apply()

                                makeAPIRequest()

                                synReapeatTime()
                                isWebViewReadyForLaunch = false


                                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                                sendBroadcast(intent22)

                                val intent = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                                sendBroadcast(intent)
                            },1000)

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


    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                handler.postDelayed(Runnable {

                    val imagUsemanualOrnotuseManual =
                        sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

                    if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {
                        pre_Laucnh_Files_For_Manual()

                    } else {
                        pre_Laucnh_Files()
                    }

                }, 200)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun pre_Laucnh_Files() {
        currentDownloadIndex++
        downloadSequentially(mFilesViewModel.readAllData.value ?: emptyList())

        downloadedFilesCount++
        copyFilesToNewFolder(mFilesViewModel.readAllData.value ?: emptyList())


        val get_fileNumber = myDownloadClass.getString(Constants.fileNumber, "").toString()
        val get_folderName = myDownloadClass.getString(Constants.folderName, "").toString()
        val get_fileName = myDownloadClass.getString(Constants.fileName, "").toString()


        val fileNum = currentDownloadIndex.toDouble()
        val totalPercentage = ((fileNum / totalFiles.toDouble()) * 100).toInt()
        val numberOfFiles = "$totalFiles"

        val editor = myDownloadClass.edit()
        editor.putString(Constants.progressBarPref, totalPercentage.toString())
        editor.putString(Constants.textDownladByes, totalPercentage.toString())

        editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
        editor.putString(Constants.numberOfFiles, numberOfFiles)
        editor.putString(Constants.filesChange, "$currentDownloadIndex")
        editor.apply()

        myHandler.postDelayed(Runnable {
            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent22)

        }, 100)

        // remove a successful the failed downloads
        val dnApiFailed = DnFailedApi(
            SN = get_fileNumber,
            FolderName = get_folderName,
            FileName = get_fileName,
            Status = "true"
        )
        dnFailedViewModel.deleteFiles(dnApiFailed)

    }

    @SuppressLint("SetTextI18n")
    private fun pre_Laucnh_Files_For_Manual() {
        currentDownloadIndex++
        downloadSequentiallyManually(mFilesViewModel.readAllData.value ?: emptyList())

        downloadedFilesCount++
        copyFilesToNewFolder(mFilesViewModel.readAllData.value ?: emptyList())


        val get_fileNumber = myDownloadClass.getString(Constants.fileNumber, "").toString()
        val get_folderName = myDownloadClass.getString(Constants.folderName, "").toString()
        val get_fileName = myDownloadClass.getString(Constants.fileName, "").toString()


        val fileNum = currentDownloadIndex.toDouble()
        val totalPercentage = ((fileNum / totalFiles.toDouble()) * 100).toInt()
        val numberOfFiles = "$totalFiles"

        val editor = myDownloadClass.edit()
        editor.putString(Constants.progressBarPref, totalPercentage.toString())
        editor.putString(Constants.textDownladByes, totalPercentage.toString())

        editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
        editor.putString(Constants.numberOfFiles, numberOfFiles)
        editor.putString(Constants.filesChange, "$currentDownloadIndex")
        editor.apply()


        myHandler.postDelayed(Runnable {
            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent22)

        }, 200)

        // remove a successful the failed downloads
        val dnApiFailed = DnFailedApi(
            SN = get_fileNumber,
            FolderName = get_folderName,
            FileName = get_fileName,
            Status = "true"
        )
        dnFailedViewModel.deleteFiles(dnApiFailed)


    }


    private fun copyFilesToNewFolder(files: List<FilesApi>) {
        if (downloadedFilesCount == totalFiles) {



            if (isFailedDownload == true) {


            } else {

                val totalPercentage = 100
                ///  binding.textPercentageCompleted.text = "$totalPercentage% Complete"


                try {
                    if (downloadCompleteReceiver != null) {
                        unregisterReceiver(downloadCompleteReceiver)
                    }


                }catch (e:Exception){}

                mFilesViewModel.deleteAllFiles()
                dnFailedViewModel.deleteAllFiles()

                val editor = myDownloadClass.edit()
               // editor.putString(Constants.progressBarPref, "100")
               // editor.putString(Constants.textDownladByes, "100")


                editor.remove(Constants.textDownladByes)
                editor.remove(Constants.progressBarPref)
                editor.remove(Constants.filesChange)
                editor.remove(Constants.numberOfFiles)
                editor.apply()


                myHandler.postDelayed(Runnable {
                    startWebViewRefresh()
                }, 2000)

            }

        }

    }


    private fun copyFilesToFailedDownloads() {

        dnFailedViewModel.getAllFiles().observeForever { filesList ->

            if (filesList.isNotEmpty()) {
                val dnFailedList = filesList.map { file ->
                    FilesApi(
                        id = file.id,
                        SN = file.SN,
                        FolderName = file.FolderName,
                        FileName = file.FileName,
                        Status = file.Status
                    )
                }
                mFilesViewModel.addMultipleFiles(dnFailedList)

            } else {



                try {
                    if (downloadCompleteReceiver != null) {
                        unregisterReceiver(downloadCompleteReceiver)
                    }


                }catch (e:Exception){}

                mFilesViewModel.deleteAllFiles()
                dnFailedViewModel.deleteAllFiles()

                val editor = myDownloadClass.edit()
              //  editor.putString(Constants.progressBarPref, "100")
              //  editor.putString(Constants.textDownladByes, "100")

                editor.remove(Constants.textDownladByes)
                editor.remove(Constants.progressBarPref)
                editor.remove(Constants.filesChange)
                editor.remove(Constants.numberOfFiles)
                editor.apply()

                myHandler.postDelayed(Runnable {
                    startWebViewRefresh()
                }, 2000)


            }

        }
    }
    private fun second_cancel_ongoing_download() {

        try {

            val download_ref: Long = myDownloadClass.getLong(Constants.downloadKey, -15)

            val query = DownloadManager.Query()
            query.setFilterById(download_ref)
            val c =
                (applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).query(
                    query
                )
            if (c.moveToFirst()) {
                manager!!.remove(download_ref)
                val editor: SharedPreferences.Editor = myDownloadClass.edit()
                editor.remove(Constants.downloadKey)
                editor.apply()
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

}

