package sync2app.com.syncapplive.additionalSettings


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.SettingsActivity
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.ApiUrls.ApiUrlViewModel
import sync2app.com.syncapplive.additionalSettings.ApiUrls.DomainUrl
import sync2app.com.syncapplive.additionalSettings.ApiUrls.SavedApiAdapter
import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.SavedHistoryListAdapter
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.User
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.UserViewModel
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.urlchecks.isUrlValid
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils
import sync2app.com.syncapplive.databinding.ActivitySyncPowellBinding
import sync2app.com.syncapplive.databinding.ContinueWithConfigDownloadBinding
import sync2app.com.syncapplive.databinding.CustomApiHardCodedLayoutBinding
import sync2app.com.syncapplive.databinding.CustomApiUrlLayoutBinding
import sync2app.com.syncapplive.databinding.CustomContinueDownloadLayoutBinding
import sync2app.com.syncapplive.databinding.CustomDefinedTimeIntervalsBinding
import sync2app.com.syncapplive.databinding.CustomSavedHistoryLayoutBinding
import sync2app.com.syncapplive.databinding.CustomSelectLauncOrOfflinePopLayoutBinding
import sync2app.com.syncapplive.databinding.CustomSetUpTimerOptionLayoutBinding
import sync2app.com.syncapplive.databinding.FinishWithConfigDownloadBinding
import sync2app.com.syncapplive.databinding.ProgressDialogLayoutBinding
import sync2app.com.syncapplive.databinding.SampleProgressConfigLayoutBinding
import java.io.File
import java.io.FileInputStream
import java.util.Objects
import java.util.zip.ZipInputStream


class ReSyncActivity : AppCompatActivity(), SavedHistoryListAdapter.OnItemClickListener,
    SavedApiAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySyncPowellBinding

    private val mUserViewModel by viewModels<UserViewModel>()

    private val mApiViewModel by viewModels<ApiUrlViewModel>()


    private val adapter by lazy {
        SavedHistoryListAdapter(this)
    }

    var isDownloadComplete = false
    private val adapterApi by lazy {
        SavedApiAdapter(this)
    }


    private lateinit var customProgressDialog: Dialog
    private lateinit var customSavedDownloadDialog: Dialog
    private lateinit var custom_ApI_Dialog: Dialog


    private var fil_CLO = ""
    private var fil_DEMO = ""
    private var fil_baseUrl = ""

    private var getUrlBasedOnSpinnerText = ""
    private var API_Server = "API-Cloud App Server"
    private var CP_server = "CP-Cloud App Server"

    private var Minutes = " Minutes"


    private var getTimeDefined_Prime = ""
    private var timeMinuetesDefined = "Sync interval timer"
    private var timeMinuetes22 = 2L
    private var timeMinuetes55 = 5L
    private var timeMinuetes10 = 10L
    private var timeMinuetes15 = 15L
    private var timeMinuetes30 = 30L
    private var timeMinuetes60 = 60L
    private var timeMinuetes120 = 120L
    private var timeMinuetes180 = 180L
    private var timeMinuetes240 = 240L


    var hour = 0
    var min = 0


    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }


    private val simpleSavedPassword: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SIMPLE_SAVED_PASSWORD,
            Context.MODE_PRIVATE
        )
    }


    private var powerManager: PowerManager? = null
    private var wakeLock: PowerManager.WakeLock? = null


    var isCompltedAndReady = false


    var manager: DownloadManager? = null

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    var preferences: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n", "WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        preferences = PreferenceManager.getDefaultSharedPreferences(this)
//        if (preferences!!.getBoolean("darktheme", false)) {
//            setTheme(R.style.DarkThemeSettings)
//        }

        binding = ActivitySyncPowellBinding.inflate(layoutInflater)
        setContentView(binding.root)


        powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YourApp::MyWakelockTag")
        wakeLock!!.acquire()


        manager = applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?

        MyApplication.incrementRunningActivities()

        binding.apply {

            val getSavedCLOImPutFiled =
                myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "")
            val getSaveSubFolderInPutFiled =
                myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "")
            val getSavedEditTextInputSynUrlZip =
                myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "")

            if (!getSavedCLOImPutFiled.isNullOrEmpty()) {
                editTextCLOpath.setText(getSavedCLOImPutFiled)
            }

            if (!getSaveSubFolderInPutFiled.isNullOrEmpty()) {
                editTextSubPathFolder.setText(getSaveSubFolderInPutFiled)
            }


            if (!getSavedEditTextInputSynUrlZip.isNullOrEmpty()) {
                editTextInputSynUrlZip.setText(getSavedEditTextInputSynUrlZip)
            }

            initViewTooggle()




            textTestConnectionAPPer.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)
                try {

                    testConnectionSetup()

                } catch (_: Exception) {
                }


            }



            textDownloadZipSyncOrApiSyncNow.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)

                try {


                    second_cancel_download()

                    handler.postDelayed(Runnable {

                        testAndDownLoadZipConnection()

                    }, 300)


                } catch (_: Exception) {
                }


            }

            textLauncheSaveDownload.setOnClickListener {
                showPopForLaunch_Oblin_offline()
            }

            closeBs.setOnClickListener {

                val getStateNaviagtion =
                    sharedBiometric.getString(Constants.CALL_RE_SYNC_MANGER, "")

                val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")


                val editor = sharedBiometric.edit()
                if (getStateNaviagtion.equals(Constants.CALL_RE_SYNC_MANGER)) {
                    editor.remove(Constants.CALL_RE_SYNC_MANGER)
                    editor.apply()
                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {

                    if (get_navigationS2222.equals(Constants.SettingsPage)) {
                        val intent = Intent(applicationContext, SettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                        val intent =
                            Intent(applicationContext, AdditionalSettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (get_navigationS2222.equals(Constants.WebViewPage)) {
                        val intent = Intent(applicationContext, WebActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                }


            }


        }


    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun check_for_valid_confileUrl(){

        //  showCustomProgressDialog("Testing connection")

        //  val ServerUrl = "https://firebasestorage.googleapis.com/v0/b/syn2appkiller.appspot.com/o/myApp.zip?alt=media&token=8b303be6-4490-4fe0-ae6f-47a1b5278926"
        val ServerUrl = "https://cloudappserver.co.uk/cp/app_base/public/CLO/DE_MO_2021001/Zip/Config.zip"



        val sharedLicenseKeys = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, MODE_PRIVATE)

        val  get_UserID = sharedLicenseKeys.getString(Constants.get_UserID, "")!!
        val  get_LicenseKey = sharedLicenseKeys.getString(Constants.get_LicenseKey, "")!!


        if (isNetworkAvailable()){
            lifecycleScope.launch {
                try {
                    val result = checkUrlExistence(ServerUrl)
                    if (result) {
                        showConfirmationDialog(get_UserID, get_LicenseKey,  ServerUrl)
                    } else {

                        showPopsForMyConnectionTest("", "",
                            Constants.Invalid_Config_Url
                        )
                        customProgressDialog.dismiss()

                    }
                } finally {
//                    handler.postDelayed(Runnable {
//
//                    }, 900)
                }
            }
        }else{
            showToastMessage("No Internet Connection")
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showConfirmationDialog(get_UserID:String, get_LicenseKey:String, ServerUrl:String) {
        customProgressDialog.dismiss()

        val bindingCM: ContinueWithConfigDownloadBinding = ContinueWithConfigDownloadBinding.inflate(
            LayoutInflater.from(this)
        )
        val alertDialogBuilder = AlertDialog.Builder(this@ReSyncActivity)

        alertDialogBuilder.setView(bindingCM.root)
        alertDialogBuilder.setCancelable(false)

        val alertDialog = alertDialogBuilder.create()

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        bindingCM.textCancel.setOnClickListener {

            runOnUiThread {
                binding.imagSwtichEnableConfigFileOnline.isChecked = false
                isCompltedAndReady = false
                testAndDownLoadZipConnection()
            }

            alertDialog.dismiss()


        }

        bindingCM.textUpdate.setOnClickListener {
            showCustomDialog(get_UserID, get_LicenseKey , ServerUrl)
            alertDialog.dismiss()
        }


        alertDialog.show()


    }




    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showContinueDialog() {

        val bindingCM: FinishWithConfigDownloadBinding = FinishWithConfigDownloadBinding.inflate(
            LayoutInflater.from(this)
        )
        val alertDialogBuilder = AlertDialog.Builder(this@ReSyncActivity)

        alertDialogBuilder.setView(bindingCM.root)
        alertDialogBuilder.setCancelable(false)

        val alertDialog = alertDialogBuilder.create()

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        bindingCM.textCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        bindingCM.textDownloadSync.setOnClickListener {
            runOnUiThread {
                isCompltedAndReady = true
                testAndDownLoadZipConnection()
            }
            alertDialog.dismiss()

        }


        alertDialog.show()



    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showCustomDialog(get_UserID:String, get_LicenseKey:String, ServerUrl:String) {
        val bindingCM: SampleProgressConfigLayoutBinding = SampleProgressConfigLayoutBinding.inflate(
            LayoutInflater.from(this)
        )
        val alertDialogBuilder = AlertDialog.Builder(this@ReSyncActivity)

        alertDialogBuilder.setView(bindingCM.root)
        alertDialogBuilder.setCancelable(false)

        val alertDialog = alertDialogBuilder.create()

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        bindingCM.textCancel.setOnClickListener {
            alertDialog.dismiss()
            binding.imagSwtichEnableConfigFileOnline.isChecked = false
            binding.textConfigfileOnline.text = "Use online config"

            cancel_config_download(get_UserID,get_LicenseKey,ServerUrl )

            isDownloadComplete = true


        }


        downloadConfiGFile(get_UserID, get_LicenseKey, ServerUrl, Constants.Config)
        alertDialog.show()

        isDownloadComplete = false // Flag to track download completion

        val handler = Handler()
        Thread {
            try {
                while (!isDownloadComplete) {

                    runOnUiThread {
                        getDownloadStatus(bindingCM.progressBarPref, bindingCM.teextDisplaydownload, get_UserID, get_LicenseKey, ServerUrl, Constants.Config )
                    }



                    Thread.sleep(1000)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                handler.post {
                    alertDialog.dismiss()
                }
            }
        }.start()



    }


    private fun cancel_config_download(get_UserID:String, get_LicenseKey:String,fileName:String ) {
        try {

            val download_ref: Long = myDownloadClass.getLong(Constants.downloadKey, -15)

            val folderName = Constants.Config
            val Syn2AppLive = "Syn2AppLive"

            val DeleteFolderPath = "/$Syn2AppLive/$get_UserID/$get_LicenseKey/$folderName/$fileName"

            val directoryPath =
                Environment.getExternalStorageDirectory().absolutePath + "/Download/$DeleteFolderPath"
            val file = File(directoryPath)
            delete(file)



            if (download_ref !=- 15L){
                val query = DownloadManager.Query()
                query.setFilterById(download_ref)
                val c =
                    (applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).query(query)
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





    private fun downloadConfiGFile(get_UserID:String, get_LicenseKey:String, url: String, fileName: String) {

        val Syn2AppLive = "Syn2AppLive"

        val DeleteFolderPath = "/$Syn2AppLive/$get_UserID/$get_LicenseKey/$fileName"

        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/$DeleteFolderPath"
        val file = File(directoryPath)
        delete(file)



        val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileName)
        request.allowScanningByMediaScanner()
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, DeleteFolderPath
        )
        val downloadReferenceMain = managerDownload.enqueue(request)

        val editor = myDownloadClass.edit()
        editor.putLong(Constants.downloadKey, downloadReferenceMain)
        editor.apply()

    }



    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n", "Range")
    fun getDownloadStatus(progressBarPref: ProgressBar, textprogressPercentage:TextView, get_UserID:String, get_LicenseKey:String, url: String, fileName: String) {
        try {

            val download_ref = myDownloadClass.getLong(Constants.downloadKey, -15)
            val query = DownloadManager.Query()
            query.setFilterById(download_ref)
            val c =
                (applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).query(
                    query
                )
            if (c!!.moveToFirst()) {
                @SuppressLint("Range") val bytes_downloaded =
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        .toLong()
                @SuppressLint("Range") val bytes_total =
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)).toLong()
                val dl_progress =
                    (bytes_downloaded.toDouble() / bytes_total.toDouble() * 100f).toInt()
                progressBarPref.setProgress(dl_progress)


                // Calculate the percentage of completion
                val progressPercentage = (bytes_downloaded.toFloat() / bytes_total.toFloat() * 100).toInt()
                textprogressPercentage.text = ""+  progressPercentage.toString() + "%  Zip Downloaded"


                if (c.moveToFirst()) {
                    val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    if (status == DownloadManager.STATUS_SUCCESSFUL ) {

                        runOnUiThread {
                            isDownloadComplete = true
                            funUnZipFile(get_UserID, get_LicenseKey, url,fileName )
                        }
                    }

                }

            }
        } catch (ignored: java.lang.Exception) {
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun funUnZipFile( get_UserID:String, get_LicenseKey:String, url: String, fileName: String) {
        try {

            showCustomProgressDialog("Please wait for files to unpack")

            GlobalScope.launch(Dispatchers.IO) {

                val folderName = Constants.AppConfigFolder
                val Syn2AppLive = Constants.Syn2AppLive

                val finalFolderPath = "/$get_UserID/$get_LicenseKey"

                val get_Clo = simpleSavedPassword.getString(Constants.get_UserID, "")
                val get_DEmo = simpleSavedPassword.getString(Constants.get_LicenseKey, "")

                val newConfigFolder  = "/$get_Clo/$get_DEmo/App/"

                val directoryPathString = Environment.getExternalStorageDirectory().absolutePath + "/Download/$Syn2AppLive" + finalFolderPath
                val destinationFolder = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/$Syn2AppLive/" + newConfigFolder)

                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs()
                }

                val myFile = File(directoryPathString, File.separator + fileName)
                if (myFile.exists()) {
                    extractZip(myFile.toString(), destinationFolder.toString())
                } else {
                    withContext(Dispatchers.Main) {
                        showToastMessage("Zip file could not be found")
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    suspend fun extractZip(zipFilePath: String, destinationPath: String) {
        try {
            withContext(Dispatchers.Main) {
                showToastMessage(Constants.Extracting)
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
                        }
                    }
                )

            }

            zipInputStream.close()

            withContext(Dispatchers.Main) {
                stratMyACtivity()
                showToastMessage(Constants.media_ready)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            runOnUiThread {
                handler.postDelayed(Runnable {
                    showContinueDialog()
                    customProgressDialog.cancel()
                }, 1000)
            }
        }


    }


    override fun onBackPressed() {


        val getStateNaviagtion = sharedBiometric.getString(Constants.CALL_RE_SYNC_MANGER, "")

        val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")


        val editor = sharedBiometric.edit()
        if (getStateNaviagtion.equals(Constants.CALL_RE_SYNC_MANGER)) {
            editor.remove(Constants.CALL_RE_SYNC_MANGER)
            editor.apply()
            val intent = Intent(applicationContext, WebActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            if (get_navigationS2222.equals(Constants.SettingsPage)) {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                finish()
            } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                val intent = Intent(applicationContext, AdditionalSettingsActivity::class.java)
                startActivity(intent)
                finish()
            } else if (get_navigationS2222.equals(Constants.WebViewPage)) {
                val intent = Intent(applicationContext, WebActivity::class.java)
                startActivity(intent)
                finish()
            }


        }


    }

    override fun onResume() {
        super.onResume()

        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (ignored: java.lang.Exception) {
        }

    }

    override fun onStop() {
        super.onStop()

        try {

            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (wakeLock != null && wakeLock!!.isHeld) {
                wakeLock!!.release()
            }

        } catch (ignored: java.lang.Exception) {
        }




    }
    override fun onDestroy() {
        super.onDestroy()

        try {
            MyApplication.decrementRunningActivities()

            if (wakeLock != null && wakeLock!!.isHeld) {
                wakeLock!!.release()
            }

        }catch (_:Exception){}
    }


    private fun showCustomProgressDialog(message: String) {
        try {
            customProgressDialog = Dialog(this)
            val binding = ProgressDialogLayoutBinding.inflate(LayoutInflater.from(this))
            customProgressDialog.setContentView(binding.root)
            customProgressDialog.setCancelable(true)
            customProgressDialog.setCanceledOnTouchOutside(false)
            customProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.textLoading.text = message

            binding.imgCloseDialog.setOnClickListener {
                customProgressDialog.cancel()
            }

            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }


    private fun testConnectionSetup() {
        binding.apply {
            val getFolderClo = editTextCLOpath.text.toString().trim()
            val getFolderSubpath = editTextSubPathFolder.text.toString().trim()

            val editor = myDownloadClass.edit()
            if (isNetworkAvailable()) {
                if (!imagSwtichEnableManualOrNot.isChecked) {

                    if (imagSwtichPartnerUrl.isChecked) {

                        if (getUrlBasedOnSpinnerText.isNotEmpty()) {


                            when (getUrlBasedOnSpinnerText) {
                                CP_server -> {
                                    if (getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty()) {
                                        httpNetworkTester(getFolderClo, getFolderSubpath)
                                        editor.putString(
                                            Constants.getSavedCLOImPutFiled,
                                            getFolderClo
                                        )
                                        editor.putString(
                                            Constants.getSaveSubFolderInPutFiled,
                                            getFolderSubpath
                                        )
                                        editor.apply()

                                    } else {
                                        editTextCLOpath.error = "Input a valid path e.g CLO"
                                        editTextSubPathFolder.error =
                                            "Input a valid path e.g DE_MO_2021000"
                                        showToastMessage("Fields can not be empty")
                                    }
                                }

                                API_Server -> {
                                    showToastMessage("No Logic For API Server Yet")
                                }

                            }

                        } else {
                            showToastMessage("Select Partner Url")
                        }

                    } else {

                        // let consider Custom Domain was selected

                        val getFolderClo222 = binding.editTextCLOpath.text.toString().trim()
                        val getFolderSubpath22 =
                            binding.editTextSubPathFolder.text.toString().trim()


                        var Saved_Domains_Urls =
                            myDownloadClass.getString(Constants.Saved_Domains_Urls, "").toString()


                        if (Saved_Domains_Urls.isNotEmpty()) {

                            if (getFolderClo222.isNotEmpty() && getFolderSubpath22.isNotEmpty()) {
                                testConnectionSetup_API_Test(getFolderClo222, getFolderSubpath22)
                                editor.putString(Constants.getSavedCLOImPutFiled, getFolderClo222)
                                editor.putString(Constants.getSaveSubFolderInPutFiled, getFolderSubpath22)
                                editor.apply()

                            } else {
                                editTextCLOpath.error = "Input a valid path e.g CLO"
                                editTextSubPathFolder.error =
                                    "Input a valid path e.g DE_MO_2021000"
                                showToastMessage("Fields can not be empty")
                            }


                        } else {
                            showToastMessage("Select Custom Domain")
                        }

                    }

                } else {

                    /// when the button is checked

                    val editInputUrl = editTextInputSynUrlZip.text.toString().trim()
                    if (editInputUrl.isNotEmpty() && isUrlValid(editInputUrl)) {
                        httpNetSingleUrlTest(editInputUrl)
                        editor.putString(
                            Constants.getSavedEditTextInputSynUrlZip,
                            editInputUrl
                        )
                        editor.apply()
                    } else {
                        showToastMessage("Invalid url format")
                        binding.editTextInputSynUrlZip.error = "Invalid url format"
                    }

                }

            } else {
                showToastMessage("No Internet Connection")
            }


        }
    }


    private fun httpNetworkTester(getFolderClo: String, getFolderSubpath: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Testing connection")


            val editor = myDownloadClass.edit()

            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {
                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Zip/App.zip"
                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Successful"
                            )

                            // save also to room data base
                            val user =
                                User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                            mUserViewModel.addUser(user)
                            customProgressDialog.dismiss()

                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                            customProgressDialog.dismiss()
                        }
                    } finally {
                     //   customProgressDialog.dismiss()

                    }
                }

            } else {


                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Successful"
                            )

                            val user =
                                User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                            mUserViewModel.addUser(user)
                            customProgressDialog.dismiss()

                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                            customProgressDialog.dismiss()
                        }
                    } finally {
                      //  customProgressDialog.dismiss()
                    }
                }


            }


        }, 300)
    }


    private fun httpNetSingleUrlTest(baseUrl33: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Testing connection")


            val lastString = baseUrl33.substringAfterLast("/")
            val fileNameWithoutExtension = lastString.substringBeforeLast(".")

            lifecycleScope.launch {
                try {
                    val result = checkUrlExistence(baseUrl33)
                    if (result) {
                        showPopsForMyConnectionTest(
                            "CLO",
                            fileNameWithoutExtension,
                            "Successful"
                        )

                        val user = User(CLO = "", DEMO = "", EditUrl = baseUrl33)
                        mUserViewModel.addUser(user)
                        customProgressDialog.dismiss()

                    } else {

                        showPopsForMyConnectionTest("CLO", fileNameWithoutExtension, "Failed!")
                        customProgressDialog.dismiss()
                    }
                } finally {
                  //  customProgressDialog.dismiss()
                }
            }

        }, 300)
    }


    private fun initViewTooggle() {
        binding.apply {

            //for history

            constraintLayoutSavedDwonlaod.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)

                showSaveduserHistory()


            }


            // for server
            //  getUrlBasedOnSpinnerText = CP_server   // it was hard coded before, but now saved
            constraintLayout4.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)

                if (imagSwtichPartnerUrl.isChecked) {
                    serVerOptionDialog()

                } else {

                    show_API_Urls()
                }

            }


            //// inilaizing the Set Syn Timmer
            // for time intervals
            // getTimeDefined = timeMinuetesDefined

            initSyncTimmer()



            textIntervalsSelect.setOnClickListener {

                definedTimeIntervals()
            }


            textView12.setOnClickListener {
              //  setUpCustomeTimmer()
                showSyncDialog()
            }


            val imgLunchOnline =
                sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "")
            imagSwtichEnableLaucngOnline.isChecked =
                imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)

            val editor333 = myDownloadClass.edit()

            if (imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)) {
                textLunchOnline.setText("Launch online")
                editor333.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline)
                editor333.apply()


            } else {

                textLunchOnline.setText("Launch offline")
                editor333.putString(
                    Constants.Tapped_OnlineORoffline,
                    Constants.tapped_launchOffline
                )
                editor333.apply()
            }

            imagSwtichEnableLaucngOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")
                    editor.apply()

                    textLunchOnline.setText("Launch online")

                    editor333.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOnline
                    )
                    editor333.apply()

                } else {

                    editor.remove("imgAllowLunchFromOnline")
                    editor.apply()

                    textLunchOnline.setText("Launch offline")

                    editor333.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOffline
                    )
                    editor333.apply()

                }
            }


            /// use first Sync  or Do not use First Sync

            // enable satrt file for first synct

            val startFileFirstSync =
                sharedBiometric.getString(Constants.imgStartFileFirstSync, "")
            imgStartFileFirstSync.isChecked =
                startFileFirstSync.equals(Constants.imgStartFileFirstSync)


            if (startFileFirstSync.equals(Constants.imgStartFileFirstSync)) {
                textUseStartFile.setText("Use start file on")
            } else {
                textUseStartFile.setText("Use start file off")
            }


            imgStartFileFirstSync.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imgStartFileFirstSync, Constants.imgStartFileFirstSync
                    )
                    editor.apply()
                    textUseStartFile.setText("Use start file on")

                } else {

                    editor.remove(Constants.imgStartFileFirstSync)
                    editor.apply()
                    textUseStartFile.setText("Use start file off")

                }
            }


            // enable config

            val imgLCongigFile =
                sharedBiometric.getString(Constants.imagSwtichEnableConfigFileOnline, "")
            imagSwtichEnableConfigFileOnline.isChecked =
                imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)


            if (imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)) {
                textConfigfileOnline.setText("Config File Offline")
            } else {
                textConfigfileOnline.setText("Config File Online")
            }


            imagSwtichEnableConfigFileOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableConfigFileOnline,
                        "imagSwtichEnableConfigFileOnline"
                    )
                    editor.apply()
                    textConfigfileOnline.setText("Config File Offline")
                } else {

                    editor.remove("imagSwtichEnableConfigFileOnline")
                    editor.apply()

                    textConfigfileOnline.setText("Config File Online")
                }
            }


            // enable Sync on File Change

            val imgEnableFileOnSyncChange =
                sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "")
            imagSwtichEnableSyncOnFilecahnge.isChecked =
                imgEnableFileOnSyncChange.equals(Constants.imagSwtichEnableSyncOnFilecahnge)


            if (imgEnableFileOnSyncChange.equals(Constants.imagSwtichEnableSyncOnFilecahnge)) {
                textSyncOnFileChangeIntervals.setText("Download on Intervals")
            } else {
                textSyncOnFileChangeIntervals.setText("Download on change")
                applicationContext.stopService(
                    Intent(applicationContext, SyncInterval::class.java)

                )

            }


            imagSwtichEnableSyncOnFilecahnge.setOnCheckedChangeListener { compoundButton, isValued ->
                val editor = sharedBiometric.edit()
                second_cancel_download()
                // showToastMessage("Download cancel")

                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableSyncOnFilecahnge,
                        "imagSwtichEnableSyncOnFilecahnge"
                    )

                    // editor.putString(Constants.showDownloadSyncStatus, "showDownloadSyncStatus")

                    editor.apply()
                    textSyncOnFileChangeIntervals.setText("Download on Intervals")


                    val fil_CLO: String =
                        myDownloadClass.getString(Constants.getFolderClo, "").toString()
                    val fil_DEMO: String =
                        myDownloadClass.getString(Constants.getFolderSubpath, "").toString()


                    if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty()) {

                        if (!ServiceUtils.foregroundServiceRunning(applicationContext)) {
                            stopService(Intent(applicationContext, OnChnageService::class.java))
                            startService(
                                Intent(
                                    applicationContext,
                                    SyncInterval::class.java
                                )
                            )

                            val editorM = myDownloadClass.edit()
                            editorM.remove(Constants.SynC_Status)
                            editorM.remove(Constants.SAVED_CN_TIME)
                            editorM.apply()

                        }

                    } else {
                        // showToastMessage("Invalid Saved Paths")
                    }


                } else {

                    editor.remove("imagSwtichEnableSyncOnFilecahnge")
                    editor.apply()

                    textSyncOnFileChangeIntervals.setText("Download on change")


                    val fil_CLO: String =
                        myDownloadClass.getString(Constants.getFolderClo, "").toString()
                    val fil_DEMO: String =
                        myDownloadClass.getString(Constants.getFolderSubpath, "").toString()

                    if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty()) {

                        if (!ServiceUtils.foregroundServiceRunningOnChange(applicationContext)) {
                            stopService(Intent(applicationContext, SyncInterval::class.java))
                            startService(Intent(applicationContext, OnChnageService::class.java))

                            val editorM = myDownloadClass.edit()
                            editorM.remove(Constants.SynC_Status)
                            editorM.remove(Constants.SAVED_CN_TIME)
                            editorM.apply()

                        }

                    } else {
                        //  showToastMessage("Invalid Saved Paths")
                    }


                }
            }


            // enable Toggle Mode

            val imgEnableToggleMode =
                sharedBiometric.getString(Constants.imagSwtichEnablEnableToggleOrNot, "")
            imagSwtichEnablEnableToggleOrNot.isChecked =
                imgEnableToggleMode.equals(Constants.imagSwtichEnablEnableToggleOrNot)

            if (imgEnableToggleMode.equals(Constants.imagSwtichEnablEnableToggleOrNot)) {
                textToogleMode.setText("Disable Test Toggle Mode")
            } else {
                textToogleMode.setText("Enable Test Toggle Mode")
            }

            imagSwtichEnablEnableToggleOrNot.setOnCheckedChangeListener { compoundButton, isValued ->
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnablEnableToggleOrNot,
                        "imagSwtichEnablEnableToggleOrNot"
                    )
                    editor.apply()
                    textToogleMode.setText("Disable Test Toggle Mode")
                } else {

                    editor.remove("imagSwtichEnablEnableToggleOrNot")
                    editor.apply()

                    textToogleMode.setText("Enable Test Toggle Mode")
                }
            }






            funManulOrNotInteView()

            //// logic for Select Partner Url

            val imagPartnerurl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")
            imagSwtichPartnerUrl.isChecked =
                imagPartnerurl.equals(Constants.imagSwtichPartnerUrl)

            val get_Saved_Domains_Name = myDownloadClass.getString(Constants.Saved_Domains_Name, "")
            val Saved_Parthner_Name = myDownloadClass.getString(Constants.Saved_Parthner_Name, "")

            if (imagPartnerurl.equals(Constants.imagSwtichPartnerUrl)) {
                textPartnerUrlLunch.setText("Select Partner Url")


                if (Saved_Parthner_Name!!.isNotEmpty()) {
                    texturlsViews.setText(Saved_Parthner_Name)
                    getUrlBasedOnSpinnerText = Saved_Parthner_Name

                } else {
                    texturlsViews.setText("Select Partner Url")
                }


            } else {
                textPartnerUrlLunch.setText("Select Custom Domain")



                if (get_Saved_Domains_Name!!.isNotEmpty()) {
                    texturlsViews.setText(get_Saved_Domains_Name)

                } else {
                    texturlsViews.setText("Select Custom Domain")
                }


            }



            imagSwtichPartnerUrl.setOnCheckedChangeListener { compoundButton, isValued ->
                hideKeyBoard(binding.editTextInputSynUrlZip)
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imagSwtichPartnerUrl, "imagSwtichPartnerUrl")
                    editor.apply()
                    textPartnerUrlLunch.setText("Select Partner Url")


                    val Saved_Parthner_Name111 =
                        myDownloadClass.getString(Constants.Saved_Parthner_Name, "")


                    if (Saved_Parthner_Name111!!.isNotEmpty()) {
                        texturlsViews.setText(Saved_Parthner_Name111)
                        getUrlBasedOnSpinnerText = Saved_Parthner_Name111

                    } else {
                        texturlsViews.setText("Select Partner Url")
                    }


                } else {

                    editor.remove(Constants.imagSwtichPartnerUrl)
                    editor.apply()
                    textPartnerUrlLunch.setText("Select Custom Domain")


                    val get_Saved_Domains_Name111 =
                        myDownloadClass.getString(Constants.Saved_Domains_Name, "")

                    if (get_Saved_Domains_Name111!!.isNotEmpty()) {
                        texturlsViews.setText(get_Saved_Domains_Name111)

                    } else {
                        texturlsViews.setText("Select Custom Domain")
                    }

                }
            }



            imagSwtichEnableSyncFromAPI.isChecked = true

            val editor = sharedBiometric.edit()
            editor.putString(
                Constants.imagSwtichEnableSyncFromAPI,
                Constants.imagSwtichEnableSyncFromAPI
            )
            editor.apply()

            imagSwtichEnableSyncFromAPI.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imagSwtichEnableSyncFromAPI, Constants.imagSwtichEnableSyncFromAPI
                    )
                    editor.apply()
                    textSynfromApiZip.setText("Use ZIP Sync")
                    editTextInputSynUrlZip.setHint("Input url  ZIP Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect ZIP Sync")


                    textDownloadZipSyncOrApiSyncNow.setBackgroundResource(R.drawable.card_design_buy_gift_card)
                    textDownloadZipSyncOrApiSyncNow.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))


                } else {

                    editor.remove(Constants.imagSwtichEnableSyncFromAPI)
                    editor.apply()

                    textSynfromApiZip.setText("Use API Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect API Sync")
                    editTextInputSynUrlZip.setHint("Input url  API Sync")


                    textDownloadZipSyncOrApiSyncNow.setBackgroundResource(R.drawable.light_background_blue_color)
                    textDownloadZipSyncOrApiSyncNow.setTextColor(ContextCompat.getColor(applicationContext, R.color.deep_blue))



                }
            }

        }


    }


    private fun initSyncTimmer() {

        val get_savedIntervals = myDownloadClass.getLong(Constants.getTimeDefined, 0)

        if (get_savedIntervals != 0L) {

            binding.textIntervalsSelect.text = get_savedIntervals.toString() + Minutes
            binding.textDisplaytime.text = get_savedIntervals.toString() + Minutes

        } else {

            binding.textIntervalsSelect.text = "Sync interval timer"
            binding.textDisplaytime.text = "Selected time : 00:55"

        }

    }


    private fun showSaveduserHistory() {

        customSavedDownloadDialog = Dialog(this)
        val bindingCm = CustomSavedHistoryLayoutBinding.inflate(LayoutInflater.from(this))
        customSavedDownloadDialog.setContentView(bindingCm.root)
        customSavedDownloadDialog.setCancelable(true)
        customSavedDownloadDialog.setCanceledOnTouchOutside(true)
        customSavedDownloadDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingCm.apply {


            textClearAllData.setOnClickListener {
                mUserViewModel.deleteAllUsers()
                customSavedDownloadDialog.dismiss()
            }

            closeBs.setOnClickListener {
                customSavedDownloadDialog.dismiss()
            }


            imageCrossClose.setOnClickListener {
                customSavedDownloadDialog.dismiss()
            }

            textClearAllData.setOnClickListener {
                startActivity(Intent(applicationContext, FileExplorerActivity::class.java))
                customSavedDownloadDialog.dismiss()
            }



            handler.postDelayed(Runnable {
                recyclerSavedDownload.adapter = adapter
                recyclerSavedDownload.layoutManager = LinearLayoutManager(applicationContext)

                mUserViewModel.readAllData.observe(this@ReSyncActivity, Observer { user ->
                    adapter.setData(user)
                    if (user.isNotEmpty()) {
                        textErrorText.visibility = View.GONE
                        textClearAllData.visibility = View.VISIBLE
                    } else {
                        textClearAllData.visibility = View.GONE
                        textErrorText.visibility = View.VISIBLE
                    }
                })


            }, 100)
        }




        customSavedDownloadDialog.show()

    }


    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun definedTimeIntervals() {
        val bindingCm: CustomDefinedTimeIntervalsBinding =
            CustomDefinedTimeIntervalsBinding.inflate(
                layoutInflater
            )
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {


            imageCrossClose.setOnClickListener {
                alertDialog.dismiss()
            }

            closeBs.setOnClickListener {
                alertDialog.dismiss()
            }



            textTwoMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes22.toString() + " $Minutes"
                binding.textDisplaytime.text = "2 Minutes"
                getTimeDefined_Prime = timeMinuetes22.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_2min)
                editor.apply()
                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    // showToastMessage("Service not running")
                }


            }


            text55minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes55.toString() + " $Minutes"
                binding.textDisplaytime.text = "5 Minutes"
                getTimeDefined_Prime = timeMinuetes55.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_5min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    // showToastMessage("Service not running")
                }


            }

            text100minutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes10.toString() + " $Minutes"
                binding.textDisplaytime.text = "10 Minutes"
                getTimeDefined_Prime = timeMinuetes10.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_10min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    // showToastMessage("Service not running")
                }


            }


            text1500minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes15.toString() + " $Minutes"
                binding.textDisplaytime.text = "15 Minutes"
                getTimeDefined_Prime = timeMinuetes15.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_15min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()

                } else {
                    //   showToastMessage("Service not running")
                }


            }

            text3000minutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes30.toString() + " $Minutes"
                binding.textDisplaytime.text = "30 Minutes"
                getTimeDefined_Prime = timeMinuetes30.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_30min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    //  showToastMessage("Service not running")
                }


            }

            text6000minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes60.toString() + " $Minutes"
                binding.textDisplaytime.text = "60 Minutes"
                getTimeDefined_Prime = timeMinuetes60.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_60min)
                editor.apply()


                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    // showToastMessage("Service not running")
                }


            }


            textOneTwentyMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes120.toString() + " $Minutes"
                binding.textDisplaytime.text = "2 Hours"
                getTimeDefined_Prime = timeMinuetes120.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_120min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    //   showToastMessage("Service not running")
                }


            }



            textOneEightThyMinutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes180.toString() + " $Minutes"
                binding.textDisplaytime.text = "3 hours"
                getTimeDefined_Prime = timeMinuetes180.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_180min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    //  showToastMessage("Service not running")
                }


            }


            tex24000ThyMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes240.toString() + " $Minutes"
                binding.textDisplaytime.text = "4 hours"
                getTimeDefined_Prime = timeMinuetes240.toString()
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putLong(Constants.getTimeDefined, Constants.t_240min)
                editor.apply()

                if (ServiceUtils.foregroundServiceRunning(applicationContext)
                    || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                ) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                } else {
                    //   showToastMessage("Service not running")
                }


            }


        }


        alertDialog.show()
    }


    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun serVerOptionDialog() {
        val bindingCm: CustomApiHardCodedLayoutBinding =
            CustomApiHardCodedLayoutBinding.inflate(
                layoutInflater
            )
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {

            textApiServer.setOnClickListener {
                binding.texturlsViews.text = CP_server
                getUrlBasedOnSpinnerText = CP_server

                val editor = myDownloadClass.edit()
                editor.putString(Constants.Saved_Parthner_Name, CP_server)
                editor.apply()

                alertDialog.dismiss()
            }


            textCloudServer.setOnClickListener {
                binding.texturlsViews.text = API_Server
                getUrlBasedOnSpinnerText = API_Server

                val editor = myDownloadClass.edit()
                editor.putString(Constants.Saved_Parthner_Name, API_Server)
                editor.apply()

                alertDialog.dismiss()
            }


            imageCrossClose.setOnClickListener {
                alertDialog.dismiss()
            }

            closeBs.setOnClickListener {
                alertDialog.dismiss()
            }


        }


        alertDialog.show()
    }


        @SuppressLint("InflateParams", "SuspiciousIndentation")
        private fun setUpCustomeTimmer() {
            val bindingCm: CustomSetUpTimerOptionLayoutBinding =
                CustomSetUpTimerOptionLayoutBinding.inflate(
                    layoutInflater
                )
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setView(bindingCm.getRoot())
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.setCancelable(true)

            // Set the background of the AlertDialog to be transparent
            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            bindingCm.apply {


                textCancel.setOnClickListener {
                    alertDialog.dismiss()
                }



                textOkayAccept.setOnClickListener {
                    val getHourValue = editTexthour.text.toString()
                    val getHourMinutes = editTextMinutes.text.toString()

                    val hours = getHourValue.toIntOrNull() ?: 0
                    val minutes = getHourMinutes.toIntOrNull() ?: 0

                    val totalMinutes = hours * 60 + minutes

                    if (totalMinutes < 1) {
                        showToastMessage("Time cannot be less than 1 minute")
                        alertDialog.dismiss()
                        return@setOnClickListener // Abort further execution
                    }

                    if (getHourMinutes.isNotEmpty() || getHourMinutes.isNotEmpty()) {

                        binding.textDisplaytime.text = "$totalMinutes $Minutes"
                        binding.textIntervalsSelect.text = "$totalMinutes $Minutes"

                        val editor = myDownloadClass.edit()
                        editor.putLong(Constants.getTimeDefined, totalMinutes.toLong())
                        editor.apply()


                        if (ServiceUtils.foregroundServiceRunning(applicationContext)
                            || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)
                        ) {
                            val intent = Intent(Constants.UpdateTimmer_Reciver)
                            sendBroadcast(intent)

                            val editor = myDownloadClass.edit()
                            editor.remove(Constants.SAVED_CN_TIME)
                            editor.apply()
                        } else {
                            // showToastMessage("Service not running")
                        }
                    } else {
                        showToastMessage("Invalid Input Filed")
                        binding.textDisplaytime.text = "Sync interval timer"
                        binding.textIntervalsSelect.text = "Sync interval timer"
                    }

                    alertDialog.dismiss()
                }


            }

            alertDialog.show()
        }



    private fun showSyncDialog() {

        //create dialog
        val syncDialog = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val viewOptions: View = inflater.inflate(R.layout.time_picker_dialog, null)

        //widgets
        val timePicker = viewOptions.findViewById<TimePicker>(R.id.timePicker)
        val cancelBtn = viewOptions.findViewById<Button>(R.id.cancelBtn)
        val setBtn = viewOptions.findViewById<Button>(R.id.setBtn)

        //dialog props
        syncDialog.setView(viewOptions)
        syncDialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        syncDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //initialize time picker
        timePicker.hour = 0
        timePicker.minute = 0
        timePicker.setIs24HourView(true)
        timePicker.setOnTimeChangedListener { view: TimePicker?, hourOfDay: Int, minute: Int ->

            //get time
            hour = hourOfDay
            min = minute
        }

        //cancel
        cancelBtn.setOnClickListener { v: View? ->

            //dismiss
            syncDialog.dismiss()
        }

        //grant access
        setBtn.setOnClickListener { v: View? ->

            //calculate minutes
            val totalMins: Int = hour * 60 + min


            if (totalMins < 1) {
                showToastMessage("Time cannot be less than 1 minute")
                syncDialog.dismiss()
                return@setOnClickListener // Abort further execution
            }


            //set on edt
            binding.textDisplaytime.setText(String.format("%02d:%02d", hour, min))
            binding.textIntervalsSelect.setText(String.format("%02d:%02d", hour, min))


            //reset temp data
            hour = 0
            min = 0


            val editor = myDownloadClass.edit()
            editor.putLong(Constants.getTimeDefined, totalMins.toLong())
            editor.apply()


            if (ServiceUtils.foregroundServiceRunning(applicationContext)
                || ServiceUtils.foregroundServiceRunningOnChange(applicationContext)) {
                val intent = Intent(Constants.UpdateTimmer_Reciver)
                sendBroadcast(intent)

                val editor = myDownloadClass.edit()
                editor.remove(Constants.SAVED_CN_TIME)
                editor.apply()
            } else {
                // showToastMessage("Service not running")
            }


            //close dialog
            syncDialog.dismiss()
        }

        //show dialog
        syncDialog.show()
    }


    private fun funManulOrNotInteView() {
        binding.apply {
            // logic for use manual or not
            val imagUsemanualOrnotuseManual =
                sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")
            imagSwtichEnableManualOrNot.isChecked =
                imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)


            if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {
                textUseManual.setText("Use manual")

                editTextInputSynUrlZip.visibility = View.VISIBLE
                editTextInputIndexManual.visibility = View.VISIBLE
                // for clos
                editTextCLOpath.visibility = View.GONE
                editTextSubPathFolder.visibility = View.GONE


            } else {
                textUseManual.setText("Do not use manual")

                editTextInputSynUrlZip.visibility = View.GONE
                editTextInputIndexManual.visibility = View.GONE
                // for clos
                editTextCLOpath.visibility = View.VISIBLE
                editTextSubPathFolder.visibility = View.VISIBLE


            }

            imagSwtichEnableManualOrNot.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableManualOrNot,
                        "imagSwtichEnableManualOrNot"
                    )
                    editor.apply()
                    textUseManual.setText("Use manual")

                    editTextInputSynUrlZip.visibility = View.VISIBLE
                    editTextInputIndexManual?.visibility = View.VISIBLE
                    // for clos
                    editTextCLOpath.visibility = View.GONE
                    editTextSubPathFolder.visibility = View.GONE


                } else {

                    editor.remove("imagSwtichEnableManualOrNot")
                    editor.apply()
                    textUseManual.setText("Do not use manual")

                    editTextInputSynUrlZip.visibility = View.GONE
                    editTextInputIndexManual?.visibility = View.GONE
                    // for clos
                    editTextCLOpath.visibility = View.VISIBLE
                    editTextSubPathFolder.visibility = View.VISIBLE

                }
            }

        }
    }


    private fun hideKeyBoard(editText: EditText) {
        try {
            editText.clearFocus()
            val imm =
                applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (ignored: java.lang.Exception) {
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun testAndDownLoadZipConnection() {

        binding.apply {
            val getFolderClo = editTextCLOpath.text.toString().trim()
            val getFolderSubpath = editTextSubPathFolder.text.toString().trim()
            val editor = myDownloadClass.edit()

            if (isNetworkAvailable()) {
                if (!imagSwtichEnableManualOrNot.isChecked) {


                    if (imagSwtichPartnerUrl.isChecked) {

                        if (getUrlBasedOnSpinnerText.isNotEmpty()) {

                            when (getUrlBasedOnSpinnerText) {
                                CP_server -> {
                                    if (getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty()) {
                                        httpNetworkDownloadsMultiplePaths(
                                            getFolderClo,
                                            getFolderSubpath
                                        )
                                        editor.putString(
                                            Constants.getSavedCLOImPutFiled,
                                            getFolderClo
                                        )
                                        editor.putString(
                                            Constants.getSaveSubFolderInPutFiled,
                                            getFolderSubpath
                                        )
                                        editor.apply()

                                    } else {
                                        editTextCLOpath.error = "Input a valid path e.g CLO"
                                        editTextSubPathFolder.error =
                                            "Input a valid path e.g DE_MO_2021000"
                                        showToastMessage("Fields can not be empty")
                                    }
                                }

                                API_Server -> {
                                    showToastMessage("No Logic For API Server Yet")

                                }

                            }

                        } else {
                            showToastMessage("Select Partner Url")
                        }

                    } else {

                        val getFolderClo222 = binding.editTextCLOpath.text.toString().trim()
                        val getFolderSubpath22 =
                            binding.editTextSubPathFolder.text.toString().trim()

                        var Saved_Domains_Urls =
                            myDownloadClass.getString(Constants.Saved_Domains_Urls, "").toString()

                        if (Saved_Domains_Urls.isNotEmpty()) {


                            if (getFolderClo222.isNotEmpty() && getFolderSubpath22.isNotEmpty()) {
                                testAndDownLoad_My_API(getFolderClo222, getFolderSubpath22)
                                editor.putString(Constants.getSavedCLOImPutFiled, getFolderClo222)
                                editor.putString(
                                    Constants.getSaveSubFolderInPutFiled,
                                    getFolderSubpath22
                                )
                                editor.apply()

                            } else {
                                editTextCLOpath.error = "Input a valid path e.g CLO"
                                editTextSubPathFolder.error =
                                    "Input a valid path e.g DE_MO_2021000"
                                showToastMessage("Fields can not be empty")
                            }


                        } else {
                            showToastMessage("Select Custom Domain")
                        }

                    }
                } else {

                    val editInputUrl = editTextInputSynUrlZip.text.toString().trim()
                    if (editInputUrl.isNotEmpty() && isUrlValid(editInputUrl)) {
                        httpNetSingleDwonload(editInputUrl)
                        editor.putString(
                            Constants.getSavedEditTextInputSynUrlZip,
                            editInputUrl
                        )
                        editor.apply()

                    } else {
                        showToastMessage("Invalid url format")
                        binding.editTextInputSynUrlZip.error = "Invalid url format"
                    }

                }
            } else {
                showToastMessage("No Internet Connection")
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun httpNetworkDownloadsMultiplePaths(
        getFolderClo: String,
        getFolderSubpath: String,
    ) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Please wait")

            val editior = myDownloadClass.edit()

            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {

                val baseUrl = "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Zip/App.zip"



                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startMyDownlaodsMutiplesPath(
                                baseUrl,
                                getFolderClo,
                                getFolderSubpath,
                                "Zip",
                                "App.zip",
                            )

                            // save also to room data base
                            val user =
                                User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                            mUserViewModel.addUser(user)


                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )

                            customProgressDialog.dismiss()
                        }
                    } finally {

                    }
                }


            } else {

                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"



                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startMyDownlaodsMutiplesPath(
                                baseUrl,
                                getFolderClo,
                                getFolderSubpath,
                                Constants.App,
                                "index.html",
                            )

                            val user =
                                User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                            mUserViewModel.addUser(user)


                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                            customProgressDialog.dismiss()
                        }
                    } finally {

                    }
                }

            }


        }, 300)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun httpNetSingleDwonload(baseUrl: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Please wait")
            val lastString = baseUrl.substringAfterLast("/")
            val fileNameWithoutExtension = lastString.substringBeforeLast(".")


            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startDownloadSingles(baseUrl, "Zip", "App.zip")

                            val user = User(CLO = "", DEMO = "", EditUrl = baseUrl)
                            mUserViewModel.addUser(user)

                        } else {
                            showPopsForMyConnectionTest(
                                "CLO",
                                fileNameWithoutExtension,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }

            } else {

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startDownloadSingles(baseUrl, "Api", "App.zip")
                            val user = User(CLO = "", DEMO = "", EditUrl = baseUrl)
                            mUserViewModel.addUser(user)
                        } else {

                            showPopsForMyConnectionTest(
                                "CLO",
                                fileNameWithoutExtension,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }
            }


        }, 300)
    }


    private fun testConnectionSetup_API_Test(getFolderClo: String, getFolderSubpath: String) {

        showCustomProgressDialog("Testing connection")

        binding.apply {

            var Saved_Domains_Urls =
                myDownloadClass.getString(Constants.Saved_Domains_Urls, "").toString()


            if (isNetworkAvailable()) {

                val get_ModifiedUrl = Saved_Domains_Urls
                val editor = myDownloadClass.edit()
                editor.putString(Constants.get_ModifiedUrl, get_ModifiedUrl)
                editor.apply()



                if (binding.imagSwtichEnableSyncFromAPI.isChecked) {
                    val get_Full_url =
                        Saved_Domains_Urls + "/$getFolderClo/$getFolderSubpath/Zip/App.zip"

                    showToastMessageLong(get_Full_url)

                    lifecycleScope.launch {
                        try {
                            val result = checkUrlExistence(get_Full_url)
                            if (result) {
                                showPopsForMyConnectionTest(
                                    getFolderClo, getFolderSubpath,
                                    "Successful"
                                )

                                val user = User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                                mUserViewModel.addUser(user)
                                customProgressDialog.dismiss()

                            } else {
                                showPopsForMyConnectionTest(
                                    getFolderClo,
                                    getFolderSubpath,
                                    "Failed!"
                                )

                                customProgressDialog.dismiss()
                            }
                        } finally {
                           // customProgressDialog.dismiss()
                        }
                    }


//                    handler.postDelayed(Runnable {
//                        customProgressDialog.cancel()
//                    }, 900)


                } else {
                    val get_Full_url =
                        Saved_Domains_Urls + "/$getFolderClo/$getFolderSubpath/App/index.html"

                    showToastMessageLong(get_Full_url)

                    lifecycleScope.launch {
                        try {
                            val result = checkUrlExistence(get_Full_url)
                            if (result) {
                                showPopsForMyConnectionTest(
                                    getFolderClo, getFolderSubpath,
                                    "Successful"
                                )

                                val user = User(
                                    CLO = getFolderClo,
                                    DEMO = getFolderSubpath,
                                    EditUrl = ""
                                )
                                mUserViewModel.addUser(user)
                                customProgressDialog.dismiss()

                            } else {
                                showPopsForMyConnectionTest(
                                    getFolderClo,
                                    getFolderSubpath,
                                    "Failed!"
                                )
                                customProgressDialog.dismiss()
                            }
                        } finally {
                         //   customProgressDialog.dismiss()
                        }
                    }


//                    handler.postDelayed(Runnable {
//                        customProgressDialog.cancel()
//                    }, 900)


                }


            } else {
                showToastMessage("No Internet Connection")
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun testAndDownLoad_My_API(getFolderClo: String, getFolderSubpath: String) {


        showCustomProgressDialog("Testing connection")

        binding.apply {

            var Saved_Domains_Urls =
                myDownloadClass.getString(Constants.Saved_Domains_Urls, "").toString()


            if (isNetworkAvailable()) {

                val get_ModifiedUrl = Saved_Domains_Urls
                val editor = myDownloadClass.edit()
                editor.putString(Constants.get_ModifiedUrl, get_ModifiedUrl)
                editor.apply()



                if (binding.imagSwtichEnableSyncFromAPI.isChecked) {
                    val get_Full_url =
                        Saved_Domains_Urls + "/$getFolderClo/$getFolderSubpath/Zip/App.zip"

                    showToastMessageLong(get_Full_url)

                    lifecycleScope.launch {
                        try {
                            val result = checkUrlExistence(get_Full_url)
                            if (result) {
                                startMyDownlaodsMutiplesPath(
                                    get_Full_url,
                                    getFolderClo,
                                    getFolderSubpath,
                                    "Zip",
                                    "App.zip",
                                )

                                // save also to room data base
                                val user =
                                    User(CLO = getFolderClo, DEMO = getFolderSubpath, EditUrl = "")
                                mUserViewModel.addUser(user)

                            } else {
                                showPopsForMyConnectionTest(
                                    getFolderClo,
                                    getFolderSubpath,
                                    "Failed!"
                                )

                                customProgressDialog.dismiss()
                            }
                        } finally {
//                            handler.postDelayed(Runnable {
//                                customProgressDialog.dismiss()
//                            }, 900)
                        }
                    }

//                    handler.postDelayed(Runnable {
//                        customProgressDialog.cancel()
//                    }, 900)


                } else {
                    val get_Full_url =
                        Saved_Domains_Urls + "/$getFolderClo/$getFolderSubpath/App/index.html"

                    showToastMessageLong(get_Full_url)

                    lifecycleScope.launch {
                        try {
                            val result = checkUrlExistence(get_Full_url)
                            if (result) {
                                startMyDownlaodsMutiplesPath(
                                    get_Full_url,
                                    getFolderClo,
                                    getFolderSubpath,
                                    Constants.App,
                                    "index.html",
                                )

                                // save also to room data base
                                val user =
                                    User(CLO = getFolderClo, DEMO = getFolderSubpath, EditUrl = "")
                                mUserViewModel.addUser(user)

                            } else {
                                showPopsForMyConnectionTest(
                                    getFolderClo,
                                    getFolderSubpath,
                                    "Failed!"
                                )
                                customProgressDialog.dismiss()
                            }
                        } finally {
                            handler.postDelayed(Runnable {
                              //  customProgressDialog.dismiss()
                            }, 900)
                        }
                    }

//                    handler.postDelayed(Runnable {
//                        customProgressDialog.cancel()
//                    }, 900)


                }


            } else {
                showToastMessage("No Internet Connection")
            }
        }

    }


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun showPopsForMyConnectionTest(
        getFolderClo: String,
        getFolderSubpath: String,
        message: String,
    ) {

        val bindingCM: CustomContinueDownloadLayoutBinding =
            CustomContinueDownloadLayoutBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(bindingCM.root)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(true)


        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        bindingCM.apply {

            if (message.equals(Constants.Invalid_Config_Url)){
                textYourUrlTest.text = Constants.Invalid_Config_Url
            }else{
                val userPath = "Username-$getFolderClo\nlicense-$getFolderSubpath"
                textYourUrlTest.text = userPath
            }


            textSucessful.text = message

            textContinuPassword2.setOnClickListener {
                alertDialog.dismiss()
            }

            imgCloseDialog.setOnClickListener {
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startMyDownlaodsMutiplesPath(
        baseUrl: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileName: String,
    ) {

        if (binding.imagSwtichEnableConfigFileOnline.isChecked && isCompltedAndReady == false) {
            check_for_valid_confileUrl()

        }else{
            binding.apply {

                customProgressDialog.dismiss()
                val threeFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"


                val Extracted = "App"



                download(baseUrl, getFolderClo, getFolderSubpath, Zip, fileName, Extracted, threeFolderPath)

                /// similar but used on under second cancel downoad in danwload pager
                val editor = myDownloadClass.edit()
                editor.putString(Constants.getFolderClo, getFolderClo)
                editor.putString(Constants.getFolderSubpath, getFolderSubpath)
                editor.putString("Zip", Zip)
                editor.putString("fileName", fileName)
                editor.putString(Constants.Extracted, Extracted)
                editor.putString("baseUrl", baseUrl)


                val url =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"


                if (binding.imagSwtichEnableLaucngOnline.isChecked) {
                    editor.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline)
                    editor.putString(Constants.syncUrl, url)

                } else {
                    editor.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOffline)

                }

                editor.apply()
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startDownloadSingles(baseUrl: String, Zip: String, fileNamy: String) {

        if (binding.imagSwtichEnableConfigFileOnline.isChecked) {
            check_for_valid_confileUrl()

        }else{
            binding.apply {

                val Extracted = Constants.App
                val threeFolderPath = "/MANUAL/DEMO/$Zip"

                download(baseUrl, "MANUAL", "DEMO", Zip, fileNamy, Extracted, threeFolderPath)

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getFolderClo, "MANUAL")
                editor.putString(Constants.getFolderSubpath, "DEMO")
                editor.putString("Zip", Zip)
                editor.putString("fileName", fileNamy)
                editor.putString(Constants.Extracted, Extracted)
                editor.putString("baseUrl", baseUrl)
                editor.apply()

            }
        }




    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }

    private fun showToastMessageLong(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun download(
        url: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileNamy: String,
        Extracted: String,
        threeFolderPath: String,
    ) {


        //  val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/"

        val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip/$fileNamy"

        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive$DeleteFolderPath"
        val file = File(directoryPath)
        delete(file)



        handler.postDelayed(Runnable {

            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
            val Syn2AppLive = "Syn2AppLive"

            val editior = myDownloadClass.edit()
            editior.putString(Constants.getFolderClo, getFolderClo)
            editior.putString(Constants.getFolderSubpath, getFolderSubpath)
            editior.putString("Zip", Zip)
            editior.putString("fileNamy", fileNamy)
            editior.putString(Constants.Extracted, Extracted)

            val get_savedIntervals = myDownloadClass.getLong(Constants.getTimeDefined, 0)

            if (get_savedIntervals != 0L) {
                editior.putLong(Constants.getTimeDefined, get_savedIntervals)

            } else {
                editior.putLong(Constants.getTimeDefined, Constants.t_5min)

            }

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
            // editor.remove(Constants.SAVED_CN_TIME)  -- may be to be removed later
            editor.apply()


            val intent = Intent(applicationContext, DownlodPagger::class.java)
            intent.putExtra("baseUrl", url)
            intent.putExtra(Constants.getFolderClo, getFolderClo)
            intent.putExtra(Constants.getFolderSubpath, getFolderSubpath)
            intent.putExtra("Zip", Zip)
            intent.putExtra("fileName", fileNamy)
            intent.putExtra("Extracted", Extracted)

            intent.putExtra("threeFolderPath", threeFolderPath)
            intent.putExtra("baseUrl", url)
            startActivity(intent)
            finish()


            val editor222 = sharedBiometric.edit()
            //  editor222.putString(Constants.showDownloadSyncStatus, "showDownloadSyncStatus")
            editor222.apply()


            stopService(Intent(this@ReSyncActivity, SyncInterval::class.java))
            stopService(Intent(this@ReSyncActivity, OnChnageService::class.java))


        }, 200)


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


    override fun onItemClicked(photo: User) {


        val clo = photo.CLO.toString()
        val demo = photo.DEMO
        val editurl = photo.EditUrl

        if (!clo.isNullOrEmpty()) {
            binding.editTextCLOpath.setText(clo)
            fil_CLO = clo

            binding.textLauncheSaveDownload.visibility = View.VISIBLE

        }

        if (!demo.isNullOrEmpty()) {
            binding.editTextSubPathFolder.setText(demo)
            fil_DEMO = demo

            binding.textLauncheSaveDownload.visibility = View.VISIBLE

        }

        if (!editurl.isNullOrEmpty()) {
            binding.editTextInputSynUrlZip.setText(editurl)
            fil_baseUrl = editurl

            binding.textLauncheSaveDownload.visibility = View.VISIBLE
        }

        customSavedDownloadDialog.dismiss()
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopForLaunch_Oblin_offline() {
        val bindingCM: CustomSelectLauncOrOfflinePopLayoutBinding =
            CustomSelectLauncOrOfflinePopLayoutBinding.inflate(
                layoutInflater
            )
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingCM.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val textLaunchMyOnline: TextView = bindingCM.textLaunchMyOnline
        val textLaunchMyOffline: TextView = bindingCM.textLaunchMyOffline
        val imgCloseDialog: ImageView = bindingCM.imageCrossClose
        val close_bs: ImageView = bindingCM.closeBs

        val userPath = "Username-$fil_CLO\nlicense-$fil_DEMO"

        bindingCM.textDescription.text = userPath


        textLaunchMyOnline.setOnClickListener {

            binding.apply {
                val editor = myDownloadClass.edit()


                textLunchOnline.setText("Launch offline")

                if (fil_CLO.isNotEmpty() && fil_DEMO.isNotEmpty()) {

                    val url =
                        "https://cp.cloudappserver.co.uk/app_base/public/$fil_CLO/$fil_DEMO/App/index.html"

                    imagSwtichEnableLaucngOnline.isChecked = true


                    editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")

                    editor.putString(Constants.getFolderClo, fil_CLO)
                    editor.putString(Constants.getFolderSubpath, fil_DEMO)
                    editor.putString(Constants.syncUrl, url)
                    editor.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOnline
                    )


                    editor.apply()

                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    showToastMessage("Select Saved Download Path")
                }



                alertDialog.dismiss()
            }

        }



        textLaunchMyOffline.setOnClickListener {

            binding.apply {
                val editor = myDownloadClass.edit()

                if (fil_CLO.isNotEmpty() && fil_DEMO.isNotEmpty()) {

                    val url =
                        "https://cp.cloudappserver.co.uk/app_base/public/$fil_CLO/$fil_DEMO/App/index.html"

                    imagSwtichEnableLaucngOnline.isChecked = true

                    editor.putString(
                        Constants.imgAllowLunchFromOnline,
                        "imgAllowLunchFromOnline"
                    )

                    editor.putString(Constants.getFolderClo, fil_CLO)
                    editor.putString(Constants.getFolderSubpath, fil_DEMO)
                    editor.putString(Constants.syncUrl, url)
                    editor.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOffline
                    )

                    editor.apply()

                    textLunchOnline.setText("Launch online")

                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    showToastMessage("Select Saved Download Path")


                }

                alertDialog.dismiss()
            }
        }


        imgCloseDialog.setOnClickListener { alertDialog.dismiss() }

        close_bs.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }


    @SuppressLint("SetTextI18n")
    private fun show_API_Urls() {
        custom_ApI_Dialog = Dialog(this)
        val bindingCm = CustomApiUrlLayoutBinding.inflate(LayoutInflater.from(this))
        custom_ApI_Dialog.setContentView(bindingCm.root)
        custom_ApI_Dialog.setCancelable(true)
        custom_ApI_Dialog.setCanceledOnTouchOutside(true)
        custom_ApI_Dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (isNetworkAvailable()) {
            bindingCm.progressBar2.visibility = View.VISIBLE
            bindingCm.textTryAgin.visibility = View.GONE
            bindingCm.textErrorText.visibility = View.GONE
            mApiViewModel.fetchApiUrls()
        } else {
            bindingCm.apply {
                progressBar2.visibility = View.GONE
                textErrorText.visibility = View.VISIBLE
                textTryAgin.visibility = View.VISIBLE
                textErrorText.text = "No Internet Connection"
            }

        }

        bindingCm.apply {


            recyclerApi.adapter = adapterApi
            recyclerApi.layoutManager = LinearLayoutManager(applicationContext)

            mApiViewModel.apiUrls.observe(this@ReSyncActivity, Observer { apiUrls ->
                apiUrls?.let {
                    adapterApi.setData(it.DomainUrls)

                    if (it.DomainUrls.isNotEmpty()) {
                        textErrorText.visibility = View.GONE
                        progressBar2.visibility = View.GONE
                        textTryAgin.visibility = View.GONE

                    } else {
                        textErrorText.visibility = View.VISIBLE
                        textTryAgin.visibility = View.VISIBLE
                        textErrorText.text = "Opps! No Data Found"
                    }

                }
            })


            imageCrossClose.setOnClickListener {
                custom_ApI_Dialog.dismiss()
            }

            closeBs.setOnClickListener {
                custom_ApI_Dialog.dismiss()
            }


            textTryAgin.setOnClickListener {
                if (isNetworkAvailable()) {
                    bindingCm.progressBar2.visibility = View.VISIBLE
                    bindingCm.textTryAgin.visibility = View.GONE
                    bindingCm.textErrorText.visibility = View.GONE
                    mApiViewModel.fetchApiUrls()
                } else {
                    bindingCm.apply {
                        progressBar2.visibility = View.GONE
                        textErrorText.visibility = View.VISIBLE
                        textTryAgin.visibility = View.VISIBLE
                        textErrorText.text = "No Internet Connection"
                        showToastMessage("No Internet Connection")
                    }

                }
            }

        }


        custom_ApI_Dialog.show()

    }

    override fun onItemClicked(domainUrl: DomainUrl) {

        val name = domainUrl.name + ""
        val urls = domainUrl.url + ""
        if (name.isNotEmpty()) {
            binding.texturlsViews.text = name
        }

        // Note - later you can use the url as well , the  name is displayed on textview
        if (name.isNotEmpty() && urls.isNotEmpty()) {
            val editor = myDownloadClass.edit()
            editor.putString(Constants.Saved_Domains_Name, name)
            editor.putString(Constants.Saved_Domains_Urls, urls)
            editor.apply()
        }


        custom_ApI_Dialog.dismiss()
    }

    private fun second_cancel_download() {
        try {

            val download_ref: Long = myDownloadClass.getLong(Constants.downloadKey, -15)

            val getFolderClo = myDownloadClass.getString("getFolderClo", "")
            val getFolderSubpath = myDownloadClass.getString("getFolderSubpath", "")
            val Zip = myDownloadClass.getString("Zip", "")
            val fileName = myDownloadClass.getString("fileName", "")


            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip/$fileName"

            val directoryPath =
                Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPath

            val myFile = File(directoryPath, fileName.toString())

            delete(myFile)


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

