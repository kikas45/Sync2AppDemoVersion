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
import retrofit2.HttpException
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.OnFileChange.Retro_On_Change
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.additionalSettings.utils.IndexFileChecker
import java.io.File
import java.io.FileInputStream
import java.util.Objects
import java.util.zip.ZipInputStream

class OnChnageService : Service() {


    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    var isDownloading = false
    var isRxtracting = false

    var manager: DownloadManager? = null

    var repEatMyProcess = false

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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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


        manager = getApplicationContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager

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

        val editor = myDownloadClass.edit()
        editor.putString(Constants.Zip, Constants.Zip)
        editor.remove(Constants.SynC_Status)
        // editor.remove(Constants.SAVED_CN_TIME)
        editor.remove(Constants.textDownladByes)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.progressBarPref)
        editor.remove(Constants.filesChange)
        editor.remove(Constants.numberOfFiles)

        editor.apply()
        countdownTimerServerUpdater?.cancel()

        //  second_cancel_download()


        showToastMessage("Sync On Change Activated")


        val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
        val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
        val fileName = myDownloadClass.getString("fileName", "").toString()
        val baseUrl = myDownloadClass.getString("baseUrl", "").toString()

        handler.postDelayed(Runnable {

            if (baseUrl.isNotEmpty() && getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty() && fileName.isNotEmpty()) {
                SyncIntervalDownload()
                synReapeatTime();

                if (networkInfo != null && networkInfo!!.isConnected) {
                    val get_imagSwtichUseIndexCahngeOrTimeStamp =
                        sharedBiometric.getString(Constants.imagSwtichUseIndexCahngeOrTimeStamp, "")

                    if (get_imagSwtichUseIndexCahngeOrTimeStamp.equals(Constants.imagSwtichUseIndexCahngeOrTimeStamp)) {

                        fetchIndexChangeTime()

                    } else {
                        fecthPtTime()
                    }


                } else {
                    showToastMessage("No internet Connection")
                }

            } else {
                showToastMessage("Invalid Path Format")
            }

        }, 500)

        return START_STICKY
    }

    private fun fetchIndexChangeTime() {

        val currentTime =
            myDownloadClass.getString(Constants.CurrentServerTime_for_IndexChange, "") + ""
        val severTime = myDownloadClass.getString(Constants.SeverTimeSaved_For_IndexChange, "") + ""

        if (currentTime.isEmpty() || severTime.isEmpty()) {

            val get_tMaster: String =
                myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
            val get_UserID: String =
                myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "").toString()
            val get_LicenseKey: String =
                myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "").toString()

            val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

            if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {

                val urlPath =
                    "https://cp.cloudappserver.co.uk/app_base/public/$get_UserID/$get_LicenseKey/App/index.html"
                checkIndexFileChange(urlPath)

            } else {

                val urlPath = "$get_tMaster$get_UserID/$get_LicenseKey/App/index.html"
                checkIndexFileChange(urlPath)
            }


            val editor = myDownloadClass.edit()
            editor.putString(Constants.SynC_Status, Constants.PR_running)
            editor.apply()

            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent22)
        }


    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun checkIndexFileChange(url: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val checker = IndexFileChecker(url)
            val result = checker.checkIndexFileChange()
            val editor = myDownloadClass.edit()
            editor.putString(Constants.SeverTimeSaved, result)
            editor.putString(Constants.CurrentServerTime, result)
            editor.apply()
        }
    }


    private fun fecthPtTime() {
        val currentTime = myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
        val severTime = myDownloadClass.getString(Constants.SeverTimeSaved, "") + ""

        if (currentTime.isEmpty() || severTime.isEmpty()) {

            val get_tMaster: String =
                myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
            val get_UserID: String =
                myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "").toString()
            val get_LicenseKey: String =
                myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "").toString()

            val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

            if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {
                val un_dynaic_path = "https://cp.cloudappserver.co.uk/app_base/public/"
                val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                getServerTimeFromJson(un_dynaic_path, dynamicPart)

                Log.d("OnChnageService", " $un_dynaic_path$dynamicPart")

            } else {

                val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                getServerTimeFromJson(get_tMaster, dynamicPart)

                Log.d("OnChnageService", "$get_tMaster$dynamicPart")

            }


            val editor = myDownloadClass.edit()
            editor.putString(Constants.SynC_Status, Constants.PR_running)
            editor.apply()

            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent22)
        }
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
        unregisterReceiver(downloadCompleteReceiver)
        unregisterReceiver(UpdateTimmerBroad_Reciver)
        myHandler.removeCallbacksAndMessages(null)
        handler.removeCallbacksAndMessages(null)

        //  second_cancel_download()

    }


    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val get_value_if_Api_is_required =
                    sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")
                if (repEatMyProcess == true) {
                    if (get_value_if_Api_is_required.equals(Constants.imagSwtichEnableSyncFromAPI)) {
                        funUnZipFile()
                    } else {
                        stratMyACtivity()
                        Log.d("OnChnageService", "onReceive: Api Completed")
                    }
                    repEatMyProcess = false
                }


            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {


        var newsTitle = "Sync on Change"

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


    private fun makeADownload() {

        isDownloading = true

        val getFolderClo = myDownloadClass.getString("getFolderClo", "").toString()
        val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "").toString()
        val Zip = myDownloadClass.getString("Zip", "").toString()
        val fileName = myDownloadClass.getString("fileName", "").toString()
        val Extracted = myDownloadClass.getString(Constants.Extracted, "").toString()
        val baseUrl = myDownloadClass.getString("baseUrl", "").toString()

        download(baseUrl, getFolderClo, getFolderSubpath, Zip, fileName, Extracted)
    }


    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    private fun download(
        url: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileNamy: String,
        Extracted: String,
    ) {


        val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip/$fileNamy"

        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive$DeleteFolderPath"
        val file = File(directoryPath)
        delete(file)


        val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")


        val get_imagSwtichEnableSyncFromAPI =
            sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")

        if (get_imagSwtichEnableSyncFromAPI.equals(Constants.imagSwtichEnableSyncFromAPI)) {

            // when enable Sync Zip is  toggle On from Syn manager Page
            if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {

                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Zip/App.zip"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {
                        manageDownload(
                            baseUrl,
                            getFolderClo,
                            getFolderSubpath,
                            "Zip",
                            Constants.fileNmae_App_Zip,
                            Extracted
                        )

                        withContext(Dispatchers.Main) {
                            //   showToastMessage("partner url On Chnage (Zip)  $baseUrl")
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
                val baseUrl = "$get_tMaster$getFolderClo/$getFolderSubpath/Zip/App.zip"

                GlobalScope.launch {
                    val result = checkUrlExistence(baseUrl)
                    if (result) {
                        manageDownload(
                            baseUrl,
                            getFolderClo,
                            getFolderSubpath,
                            "Zip",
                            Constants.fileNmae_App_Zip,
                            Extracted
                        )
                        withContext(Dispatchers.Main) {
                            //   showToastMessage("Master url Onchnage (Zip)  $baseUrl")
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

    private fun manageDownload(
        url: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileNamy: String,
        Extracted: String,
    ) {


        val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip/$fileNamy"

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
            editior.putString(Constants.Extracted, Extracted)
            editior.putString(Constants.baseUrl, url)
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
            //  request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(fileNamy)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "/$Syn2AppLive/$finalFolderPath/$fileNamy"
            )
            val downloadReferenceMain = managerDownload.enqueue(request)

            val editor = myDownloadClass.edit()
            editor.putLong(Constants.downloadKey, downloadReferenceMain)
            editor.apply()


            repEatMyProcess = true

            //   showToastMessage("Downloading")

            editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
            editor.apply()

            Log.d("MYService", "download: started")

            val intent22 = Intent(Constants.RECIVER_PROGRESS)
            sendBroadcast(intent22)

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


    @OptIn(DelicateCoroutinesApi::class)
    private fun funUnZipFile() {
        try {
            // Retrieve your parameters here...

            GlobalScope.launch(Dispatchers.IO) {
                val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "")
                val getFolderSubpath = myDownloadClass.getString(Constants.getFolderSubpath, "")
                val Zip = myDownloadClass.getString("Zip", "")
                val fileNamy = myDownloadClass.getString("fileNamy", "")
                val Extracted = myDownloadClass.getString(Constants.Extracted, "")


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

                if (myFile.exists()) {
                    extractZip(myFile.toString(), destinationFolder.toString())
                } else {

                    withContext(Dispatchers.Main) {

                        val intent22 = Intent(Constants.RECIVER_PROGRESS)
                        intent22.putExtra(Constants.SynC_Status, Constants.PR_Zip_error)
                        sendBroadcast(intent22)

                        isDownloading = false


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
                //  showToastMessage("Zip extraction started")

                isRxtracting = true

                showToastMessage(Constants.Extracting)

                val intent22 = Intent(Constants.RECIVER_PROGRESS)
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
                showToastMessage(Constants.media_ready)

                val intent22 = Intent(Constants.RECIVER_PROGRESS)
                sendBroadcast(intent22)

                val editor = myDownloadClass.edit()
                editor.putString(Constants.SynC_Status, Constants.PR_Refresh)
                editor.apply()


            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                showToastMessage(Constants.Error_during_zip_extraction)
                stratMyACtivity()

            }
        }
    }


    private fun stratMyACtivity() {
        try {


            myHandler.postDelayed(Runnable {


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
    private fun fetch_Data_Index_OnChange(urlPath: String) {


        val editor = myDownloadClass.edit()

        GlobalScope.launch(Dispatchers.IO) {
            try {


                val checker = IndexFileChecker(urlPath)
                val getvalue = checker.checkIndexFileChange()


                editor.putString(Constants.CurrentServerTime, getvalue)
                editor.apply()


                val currentTime = myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
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
                                editor.putString(Constants.SynC_Status, Constants.PR_Extracting)
                                editor.apply()

                            } else {
                                //  editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                editor.putString(Constants.SynC_Status, Constants.PR_Downloading)
                                editor.apply()

                            }

                        }


                    }, 1300)


                } else {

                    if (isDownloading == false) {
                        makeADownload()

                        checkIndexFileChange(urlPath)


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


    } catch (e: HttpException){
        withContext(Dispatchers.Main) {
            showToastMessage("HTTP Exception: ${e.message()}")

        }
    } catch (e: Exception)
    {
        withContext(Dispatchers.Main) {
            showToastMessage("Error: ${e.message}")

            showToastMessage("No internet Connection")


        }
    }
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
                            makeADownload()

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

                    val get_tMaster: String =
                        myDownloadClass.getString(Constants.get_ModifiedUrl, "").toString()
                    val get_UserID: String = myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "").toString()
                    val get_LicenseKey: String = myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "").toString()

                    val imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")

                    val get_imagSwtichUseIndexCahngeOrTimeStamp = sharedBiometric.getString(Constants.imagSwtichUseIndexCahngeOrTimeStamp, "")


                    if (imagSwtichPartnerUrl == Constants.imagSwtichPartnerUrl) {


                        if (get_imagSwtichUseIndexCahngeOrTimeStamp.equals(Constants.imagSwtichUseIndexCahngeOrTimeStamp)) {
                            val urlPath = "https://cp.cloudappserver.co.uk/app_base/public/$get_UserID/$get_LicenseKey/App/index.html"
                            fetch_Data_Index_OnChange(urlPath)

                        } else {
                            val un_dynaic_path = "https://cp.cloudappserver.co.uk/app_base/public/"
                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            fetchData(un_dynaic_path, dynamicPart)
                            Log.d("OnChnageService", "Img  $un_dynaic_path$dynamicPart")
                        }


                    } else {

                        if (get_imagSwtichUseIndexCahngeOrTimeStamp.equals(Constants.imagSwtichUseIndexCahngeOrTimeStamp)) {

                            val urlPath = "$get_tMaster/$get_UserID/$get_LicenseKey/App/index.html"
                            fetch_Data_Index_OnChange(urlPath)

                        }else{
                            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
                            fetchData(get_tMaster, dynamicPart)
                            Log.d("OnChnageService", "$get_tMaster$dynamicPart")
                        }


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




