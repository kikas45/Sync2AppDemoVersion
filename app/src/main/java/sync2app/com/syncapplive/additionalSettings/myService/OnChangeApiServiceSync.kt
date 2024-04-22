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
import retrofit2.HttpException
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.OnFileChange.Retro_On_Change
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DownloadHelper
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.ZipDownloader
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import java.io.File

class OnChangeApiServiceSync : Service() {





    private lateinit var mUserViewModel: FilesViewModel

    private var currentDownloadIndex = 0
    private var totalFiles: Int = 0

    private var countdownTimer: CountDownTimer? = null
    private var countRefresh: CountDownTimer? = null



    var lauchWebView = false
    var isExecutionCompleted = false


    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    var isDownloading = false
    var isRxtracting = false

    var manager: DownloadManager? = null


    private var countdownTimerServerUpdater: CountDownTimer? = null


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

    private val simple_saved_passowrd: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SIMPLE_SAVED_PASSWORD,
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

        val editorDN = myDownloadClass.edit()
        editorDN.remove(Constants.SynC_Status)
        //  editorDN.remove(Constants.SAVED_CN_TIME)
        editorDN.remove(Constants.textDownladByes)
        editorDN.remove(Constants.progressBarPref)
        editorDN.remove(Constants.progressBarPref)
        editorDN.remove(Constants.filesChange)
        editorDN.remove(Constants.numberOfFiles)

        editorDN.apply()


        mUserViewModel = FilesViewModel(application)



        manager = getApplicationContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager


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

        val editor = myDownloadClass.edit()
        editor.putString(Constants.Zip,  Constants.App)
        editor.remove(Constants.SynC_Status)
        // editor.remove(Constants.SAVED_CN_TIME)
        editor.remove(Constants.textDownladByes)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.filesChange)
        editor.remove(Constants.numberOfFiles)

        editor.apply()
        countdownTimerServerUpdater?.cancel()
        countRefresh?.cancel()


     //   second_cancel_download()


        showToastMessage("Sync On Change Activated")

        val currentTime = myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
        val severTime = myDownloadClass.getString(Constants.SeverTimeSaved, "") + ""


        val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
        val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
        val fileName = myDownloadClass.getString("fileName", "").toString()
        val baseUrl = myDownloadClass.getString("baseUrl", "").toString()

        handler.postDelayed(Runnable {

            if (baseUrl.isNotEmpty() && getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty() && fileName.isNotEmpty()) {
                SyncIntervalDownload()
                synReapeatTime();

                refeshingTimmer(1)

                if (networkInfo != null && networkInfo!!.isConnected) {

                    if (currentTime.isEmpty() || severTime.isEmpty()) {

                        val get_tMaster: String = myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
                        val get_UserID: String = myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "").toString()
                        val get_LicenseKey: String = myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "").toString()

                        val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

                        if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {
                            val un_dynaic_path = "https://cp.cloudappserver.co.uk/app_base/public/"
                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            getServerTimeFromJson(un_dynaic_path, dynamicPart)

                            Log.d("OnChnageService"," $un_dynaic_path$dynamicPart")

                        } else {

                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            getServerTimeFromJson(get_tMaster, dynamicPart)

                            Log.d("OnChnageService","$get_tMaster$dynamicPart")

                        }






                        editor.putString(Constants.SynC_Status, Constants.PR_running)
                        editor.apply()

                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        sendBroadcast(intent22)
                    }

                } else {
                    showToastMessage("No internet Connection")
                }

            } else {
                showToastMessage("Invalid Path Format")
            }

        },500)

        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onDestroy() {
        //  Toast.makeText(this, "Sync DN Cancelled", Toast.LENGTH_SHORT).show()

        val editor = myDownloadClass.edit()
        editor.remove(Constants.SynC_Status)
        editor.remove(Constants.SAVED_CN_TIME)
        editor.apply()


        countdownTimerServerUpdater?.cancel()
        unregisterReceiver(UpdateTimmerBroad_Reciver)
        myHandler.removeCallbacksAndMessages(null)
        handler.removeCallbacksAndMessages(null)
        countRefresh?.cancel()

      //  second_cancel_download()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {


        var newsTitle = "Sync on Change (Api)"

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


    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]

            getZipDownloads(file.SN, file.FolderName, file.FileName)

        } else {

            // All files are downloaded

            //   showToastMessage("All files are downloaded")

        }
    }

    private fun downloadSequentiallyManually(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]
            handler.postDelayed(Runnable {

                getZipDownloads_Manual(file.SN, file.FolderName, file.FileName)

            }, 500)


        }


    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun ManageGetDownloadMyCSVManual(baseUrl:String) {

        val get_getSavedEditTextInputSynUrlZip = myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

        GlobalScope.launch {
            val result = checkUrlExistence(baseUrl)
            if (result) {
                manageDownload_for_Manual(get_getSavedEditTextInputSynUrlZip)
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
                val baseUrl = "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/$lastEnd"
                val get_ModifiedUrl = "https://cp.cloudappserver.co.uk/app_base/public/"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {
                        manageDownload(get_ModifiedUrl, getFolderClo, getFolderSubpath)

                        withContext(Dispatchers.Main) {
                            //  showToastMessage("Partner (On change API) interval $baseUrl")
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            showToastMessage("Invalid url")
                        }
                    }
                }


            } else {

                // No partner url

                val get_tMaster: String = myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
                val lastEnd = "/Api/update1.csv"
                val baseUrl = "$get_tMaster/$getFolderClo/$getFolderSubpath/$lastEnd"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {
                        manageDownload(get_tMaster, getFolderClo, getFolderSubpath)
                        withContext(Dispatchers.Main) {
                         //   showToastMessage("Master (On change API)   interval $baseUrl")
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
    private fun manageDownload_for_Manual(get_getSavedEditTextInputSynUrlZip:String ) {

        val intent22 = Intent(Constants.RECIVER_PROGRESS)
        intent22.putExtra(Constants.SynC_Status, Constants.PR_running)
        sendBroadcast(intent22)

        GlobalScope.launch(Dispatchers.IO) {
            val csvDownloader = CSVDownloader()
            val csvData = csvDownloader.downloadCSV(get_getSavedEditTextInputSynUrlZip, "", "", "")
            saveURLPairs(csvData)

            myHandler.postDelayed(runnableManual, 500)

        }

    }





    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    private fun manageDownload(get_ModifiedUrl:String, getFolderClo:String, getFolderSubpath:String, ) {

        val intent22 = Intent(Constants.RECIVER_PROGRESS)
        intent22.putExtra(Constants.SynC_Status, Constants.PR_running)
        sendBroadcast(intent22)

        GlobalScope.launch(Dispatchers.IO) {
            val lastEnd = "/Api/update1.csv"
            //  val lastEnd = "/Start/start1.csv"
            val csvDownloader = CSVDownloader()
            val csvData = csvDownloader.downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath, lastEnd)
            saveURLPairs(csvData)

            myHandler.postDelayed(runnable, 500)

        }

    }


    private val runnableManual: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {

            mUserViewModel.readAllData.observeForever { files ->
                if (files.isNotEmpty()) {
                    downloadSequentiallyManually(files)

                    totalFiles = files.size.toInt()

                    val numberOfFiles = "NF : " + files.size + ""

                    val editor = myDownloadClass.edit()
                    editor.putString(Constants.numberOfFiles, numberOfFiles)
                    editor.apply()


                } else {
                    ///   showToastMessage("No files found")
                }
            }
        }

    }


    private val runnable: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {

            mUserViewModel.readAllData.observeForever { files ->
                if (files.isNotEmpty()) {
                    downloadSequentially(files)

                    totalFiles = files.size.toInt()

                    val numberOfFiles = "NF : " + files.size + ""

                    val editor = myDownloadClass.edit()
                    editor.putString(Constants.numberOfFiles, numberOfFiles)
                    editor.apply()


                } else {
                    ///   showToastMessage("No files found")
                }
            }
        }

    }




    private fun getZipDownloads_Manual(sN: String, folderName: String, fileName: String) {

        Thread {

            isDownloading = true

            val Syn2AppLive = Constants.Syn2AppLive
            val saveMyFileToStorage = "/$Syn2AppLive/CLO/MANUAL/DEMO/$folderName"

            val getSavedEditTextInputSynUrlZip = myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

            var replacedUrl = getSavedEditTextInputSynUrlZip // Initialize it with original value


            if (getSavedEditTextInputSynUrlZip.contains("/Api/update1.csv")) {
                replacedUrl = getSavedEditTextInputSynUrlZip.replace("/Api/update1.csv", "/$folderName/$fileName")
            }else{

                Log.d("getZipDownloadsManually", "Unable to replace this url")
            }

            // Adjusting the file path to save the downloaded file
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                saveMyFileToStorage
            )
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val getFile_name = fileName.toString()

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

                        val editor22 = myDownloadClass.edit()

                        editor22.remove(Constants.textDownladByes)
                        editor22.remove(Constants.progressBarPref)

                        editor22.putString(Constants.SynC_Status, "Completed")
                        editor22.putString(Constants.numberOfFiles, "NF: 0")
                        editor22.putString(Constants.filesChange, "CF: 0")
                        editor22.apply()


                        isExecutionCompleted = true
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
            }).execute(replacedUrl, fileDestination.absolutePath)
        }.start()
    }


    private fun getZipDownloads(sN: String, folderName: String, fileName: String) {

        Thread {

            isDownloading = true

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

                        val editor22 = myDownloadClass.edit()

                        editor22.remove(Constants.textDownladByes)
                        editor22.remove(Constants.progressBarPref)

                        editor22.putString(Constants.SynC_Status, "Completed")
                        editor22.putString(Constants.numberOfFiles, "NF: 0")
                        editor22.putString(Constants.filesChange, "CF: 0")
                        editor22.apply()


                        isExecutionCompleted = true
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

            mUserViewModel.deleteAllFiles()


            val imagUsemanualOrnotuseManual = sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

            if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {

                handler.postDelayed(Runnable {
                    val getSavedEditTextInputSynUrlZip = myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

                    if  (getSavedEditTextInputSynUrlZip.contains("/Api/update1.csv")) {
                        ManageGetDownloadMyCSVManual(getSavedEditTextInputSynUrlZip)
                    }else{
                        showToastMessage("Unable to read CSV file from location")
                    }

                }, 1000)


            }else {

            handler.postDelayed(Runnable {
                ManageGetDownloadMyCSV()
            }, 1000)

        }


        } else {
            showToastMessage("No Internet Connection")
        }
    }





    private fun stratMyACtivity() {
        try {


            val editor = myDownloadClass.edit()
            editor.putString(Constants.SynC_Status, Constants.PR_running)
            editor.putString(Constants.numberOfFiles, "NF: 0")
            editor.putString(Constants.filesChange, "CF: 0")

            editor.remove(Constants.textDownladByes)
            editor.remove(Constants.progressBarPref)
            editor.apply()


            myHandler.postDelayed(Runnable {

                isExecutionCompleted = false

                isDownloading = false

                isRxtracting = false

                showToastMessage("Refreshing..");
                val intent = Intent(Constants.SEND_SERVICE_NOTIFY)
                sendBroadcast(intent)

            }, 500)


            myHandler.postDelayed(Runnable {

                val editor = myDownloadClass.edit()

                editor.putString(Constants.SynC_Status, Constants.PR_running)
                editor.apply()

                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                sendBroadcast(intent22)

            }, 1000)


        } catch (_: Exception) {
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData(baseUrl: String, dynamicPart: String) {

        val editor = myDownloadClass.edit()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val api = Retro_On_Change.create(baseUrl)
                val response = api.getAppConfig(dynamicPart)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        val getvalue = response.body()?.last_updated.toString()
                        editor.putString(Constants.CurrentServerTime, getvalue)
                        editor.apply()


                        val currentTime =
                            myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
                        val severTime = myDownloadClass.getString(Constants.SeverTimeSaved, "") + ""

                        if (currentTime == severTime) {


                            editor.putString(Constants.SynC_Status, Constants.PR_NO_CHange)
                            editor.apply()


                            val intent22 = Intent(Constants.RECIVER_PROGRESS)
                            sendBroadcast(intent22)

                            myHandler.postDelayed(Runnable {

                                if (isDownloading == false) {
                                    editor.putString(Constants.SynC_Status, Constants.PR_running)
                                    editor.apply()
                                    val intent2244 = Intent(Constants.RECIVER_PROGRESS)
                                    sendBroadcast(intent2244)

                                } else {
                                    val intent22111 = Intent(Constants.RECIVER_PROGRESS)
                                    sendBroadcast(intent22111)

                                    showToastMessage("Sync Already in Progress")


                                    if (isRxtracting == true) {
                                        //  editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                        editor.putString(
                                            Constants.SynC_Status,
                                            Constants.PR_Extracting
                                        )
                                        editor.apply()

                                    } else {
                                        //  editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                        editor.putString(
                                            Constants.SynC_Status,
                                            Constants.PR_Downloading
                                        )
                                        editor.apply()

                                    }

                                }


                            }, 1300)


                        } else {

                            if (isDownloading == false) {
                                makeAPIRequest()

                                getServerTimeFromJson(baseUrl, dynamicPart)

                                editor.putString(Constants.SynC_Status, Constants.PR_Change_Found)
                                editor.apply()

                                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                                sendBroadcast(intent22)


                            } else {
                                showToastMessage("Sync Already in Progress")

                                myHandler.postDelayed(Runnable {
                                    val intent22 = Intent(Constants.RECIVER_PROGRESS)
                                    sendBroadcast(intent22)

                                    if (isRxtracting == true) {
                                        //  editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                        editor.putString(
                                            Constants.SynC_Status,
                                            Constants.PR_Extracting
                                        )
                                        editor.apply()

                                    } else {
                                        //  editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                        editor.putString(
                                            Constants.SynC_Status,
                                            Constants.PR_Downloading
                                        )
                                        editor.apply()

                                    }


                                }, 1300)

                            }

                        }

                    } else {
                        showToastMessage("bad request")
                    }


                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")

                    showToastMessage("No internet Connection")


                }
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getServerTimeFromJson(baseUrl: String, dynamicPart: String) {


        val editor = myDownloadClass.edit()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val api = Retro_On_Change.create(baseUrl)
                val response = api.getAppConfig(dynamicPart)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        val getvalue = response.body()?.last_updated.toString()

                        editor.putString(Constants.SeverTimeSaved, getvalue)
                        editor.putString(Constants.CurrentServerTime, getvalue)
                        editor.apply()


                    } else {
                        showToastMessage("bad request")
                    }


                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")

                }
            }
        }
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun attemptRequestAgain(minutes: Long) {
        val milliseconds = minutes * 60 * 1000 // Convert minutes to
        val editor = myDownloadClass.edit()


        val connectivityManager22: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo



        countdownTimerServerUpdater = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                try {
                    countdownTimerServerUpdater?.cancel()
                    attemptRequestAgain(minutes)
                    startOrResumeTimer(minutes)


                    if (networkInfo22 != null && networkInfo22.isConnected) {

                        // start the time again

                        val get_tMaster: String = myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
                        val get_UserID: String = myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "").toString()
                        val get_LicenseKey: String = myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "").toString()

                        val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

                        if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {
                            val un_dynaic_path = "https://cp.cloudappserver.co.uk/app_base/public/"
                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            fetchData(un_dynaic_path, dynamicPart)
                            Log.d("OnChnageService","Img  $un_dynaic_path$dynamicPart")
                        } else {

                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            fetchData(get_tMaster, dynamicPart)
                            Log.d("OnChnageService","$get_tMaster$dynamicPart")
                        }


                        val intent11 = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                        sendBroadcast(intent11)

                        editor.putString(Constants.SynC_Status, Constants.PR_running)
                        editor.apply()
                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        sendBroadcast(intent22)


                    } else {
                        showToastMessage("No internet Connection")


                        //  editor.putString(Constants.SynC_Status, "Error Network")
                        //   editor.apply()

                        //    val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        //   sendBroadcast(intent22)

                        val intent11 = Intent(Constants.SEND_UPDATE_TIME_RECIEVER)
                        sendBroadcast(intent11)


                    }


                } catch (ignored: java.lang.Exception) {
                }
            }

            @SuppressLint("SuspiciousIndentation")
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
                    // showToastMessage(displayText)

                } catch (ignored: java.lang.Exception) {
                }
            }
        }
        countdownTimerServerUpdater?.start()
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


    private val UpdateTimmerBroad_Reciver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null && intent.action == Constants.UpdateTimmer_Reciver) {
                countdownTimerServerUpdater?.cancel()

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


    private fun SyncIntervalDownload() {
        val getTimeDefined = myDownloadClass.getLong(Constants.getTimeDefined, 0)

        if (getTimeDefined != 0L) {

            attemptRequestAgain(getTimeDefined)

            if (networkInfo != null && networkInfo!!.isConnected) {
                // showToastMessage("System will Sync in $getTimeDefined minute")
            } else {
                showToastMessage("No internet Connection")
            }

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
        val milliseconds = minutes * 10 * 1000 // Convert minutes to

        countRefresh = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {


                if (isExecutionCompleted == true) {
                    stratMyACtivity()
                    //  showToastMessage("refrshing")
                } else {
                    val intent22 = Intent(Constants.RECIVER_PROGRESS)
                    sendBroadcast(intent22)
                    // showToastMessage("sendBroadcast")
                }

                countRefresh!!.cancel()

                refeshingTimmer(1)


            }

            @SuppressLint("SuspiciousIndentation")
            override fun onTick(millisUntilFinished: Long) {


            }
        }
        countRefresh?.start()
    }



    private fun second_cancel_download() {
        try {

            val download_ref: Long = myDownloadClass.getLong(Constants.downloadKey, -15)

            if (download_ref != -15L) {
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

            }

        } catch (ignored: java.lang.Exception) {
        }
    }


}




