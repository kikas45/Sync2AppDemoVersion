package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnApi
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DownloadHelper
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.SavedDownloads
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.ZipDownloader
import sync2app.com.syncapplive.additionalSettings.myService.CSVDownloader
import sync2app.com.syncapplive.additionalSettings.myService.IntervalApiServiceSync
import sync2app.com.syncapplive.additionalSettings.myService.OnChangeApiServiceSync
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService
import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils
import sync2app.com.syncapplive.databinding.ActivityDownloadTheApisBinding
import sync2app.com.syncapplive.databinding.ErroMessageDialogLayoutBinding
import sync2app.com.syncapplive.databinding.LaucnOnlineDonloadPaggerBinding
import sync2app.com.syncapplive.databinding.ProgressDialogLayoutBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownloadTheApisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadTheApisBinding
    private var powerManager: PowerManager? = null
    private var wakeLock: PowerManager.WakeLock? = null

    private val okClient = OkHttpClient()

    private var currentDownloadIndex = 0


    private val mUserViewModel by viewModels<FilesViewModel>()

    private val dnViewModel by viewModels<DnViewModel>()


    private var totalFiles: Int = 0

    private val adapter by lazy {
        SavedDownloads()
    }

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    private val sharedP: SharedPreferences by lazy {
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


    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    @SuppressLint("NotifyDataSetChanged", "WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadTheApisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()

        powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YourApp::MyWakelockTag")
        wakeLock!!.acquire()

        binding.closeBs.setOnClickListener {
            val intent = Intent(applicationContext, ReSyncActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }


        binding.textLaunchApplication.setOnClickListener {
            showPopLauchOnline()
        }




        binding.textRetryBtn.setOnClickListener {
            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()

            handler.postDelayed(Runnable {
                val intent = Intent(applicationContext, DownloadTheApisActivity::class.java)
                startActivity(intent)
                finishAffinity()

            }, 1000)

        }

        binding.textCancelBtn.setOnClickListener {
            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()

            handler.postDelayed(Runnable {
                val intent = Intent(applicationContext, ReSyncActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }, 1000)
        }



        binding.apply {
            handler.postDelayed(Runnable {
                recyclerApiDownloads.adapter = adapter
                recyclerApiDownloads.layoutManager = LinearLayoutManager(applicationContext)
                dnViewModel.readAllData.observe(this@DownloadTheApisActivity, Observer { filesApi ->
                    adapter.setDataApi(filesApi)

                })
            }, 500)
        }


        val connectivityManager22: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo22: NetworkInfo? = connectivityManager22.activeNetworkInfo

        if (networkInfo22 != null && networkInfo22.isConnected) {

            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()

            val imagUsemanualOrnotuseManual =
                sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

            if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {
                handler.postDelayed(Runnable {

                    val getSavedEditTextInputSynUrlZip = sharedP.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

                    if (getSavedEditTextInputSynUrlZip.contains("/Start/start1.csv")) {
                        getDownloadMyCSVManual()
                    } else if (getSavedEditTextInputSynUrlZip.contains("/Api/update1.csv")) {
                        getDownloadMyCSVManual()
                    }else{
                       // showToastMessage("Something went wrong, System Could not locate CSV from this Location")
                        binding.textCsvStatus.text = Constants.Error_CSv_Message

                        showCustomError(Constants.Error_CSv_Message)

                    }

                }, 1000)

            } else {

                handler.postDelayed(Runnable {
                    getDownloadMyCSV()
                   // showToastMessage("getDownloadMyCSV")
                }, 1000)
            }


        } else {
            Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }


    }


    private val runnable: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            dnViewModel.deleteAllFiles()
            mUserViewModel.readAllData.observe(this@DownloadTheApisActivity, Observer { files ->
                if (files.isNotEmpty()) {
                    downloadSequentially(files)
                    binding.textRemainging.text = files.size.toString() + " Files Downloaded"
                    totalFiles = files.size.toInt()
                    binding.textCsvStatus.visibility = View.GONE
                    binding.textPercentageCompleted.visibility = View.VISIBLE
                } else {
                    // showToastMessage("No files found")
                }
            })
        }

    }


    private val runnableManual: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            dnViewModel.deleteAllFiles()
            mUserViewModel.readAllData.observe(this@DownloadTheApisActivity, Observer { files ->
                if (files.isNotEmpty()) {
                    downloadSequentiallyManuall(files)
                    binding.textRemainging.text = files.size.toString() + " Files Downloaded"
                    totalFiles = files.size.toInt()
                    binding.textCsvStatus.visibility = View.GONE
                    binding.textPercentageCompleted.visibility = View.VISIBLE
                } else {
                    // showToastMessage("No files found")
                }
            })
        }

    }


    @SuppressLint("SetTextI18n")
    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]
            handler.postDelayed(Runnable {
                getZipDownloads(file.SN, file.FolderName, file.FileName)
            }, 500)

        } else {
            //  showToastMessage("All files Downloaded")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun downloadSequentiallyManuall(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]
            handler.postDelayed(Runnable {
                getZipDownloadsManually(file.SN, file.FolderName, file.FileName)

            }, 500)

        } else {
            //  showToastMessage("All files Downloaded")
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getDownloadMyCSV() {

        lifecycleScope.launch(Dispatchers.IO) {
            val imagUsemanualOrnotuseManual =
                sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

            val getFolderClo = sharedP.getString(Constants.getFolderClo, "").toString()
            val getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "").toString()
            val get_ModifiedUrl = sharedP.getString(Constants.get_ModifiedUrl, "").toString()
            val get_getSavedEditTextInputSynUrlZip =
                sharedP.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()


            showToastMessage("API Content Connection Successful!")
            binding.textCsvStatus.text = "API Content Connection Successful!"

            val lastEnd = "Start/start1.csv";
            val csvDownloader = CSVDownloader()
            val csvData =
                csvDownloader.downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath, lastEnd)
            saveURLPairs(csvData)
            myHandler.postDelayed(runnable, 500)


        }


    }


    @SuppressLint("SetTextI18n")
    private fun getDownloadMyCSVManual() {

        lifecycleScope.launch(Dispatchers.IO) {

            val get_getSavedEditTextInputSynUrlZip = sharedP.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()


            showToastMessage("API Content Connection Successful!")
            binding.textCsvStatus.text =  "API Content Connection Successful!"

            val csvDownloader = CSVDownloader()
            val csvData = csvDownloader.downloadCSV(get_getSavedEditTextInputSynUrlZip, "", "", "")
            saveURLPairs(csvData)

            myHandler.postDelayed(runnableManual, 500)


        }


    }


    private fun showToastMessage(s: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, "$s", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveURLPairs(csvData: String) {

        mUserViewModel.deleteAllFiles()
        dnViewModel.deleteAllFiles()

        val pairs = parseCSV(csvData)

        // Add files to Room Database
        lifecycleScope.launch(Dispatchers.IO) {
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


    private fun getZipDownloads(sN: String, folderName: String, fileName: String) {
        Thread {

            val getFolderClo = sharedP.getString(Constants.getFolderClo, "").toString()
            val getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "").toString()
            val get_ModifiedUrl = sharedP.getString(Constants.get_ModifiedUrl, "").toString()


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


            val request: Request = Request.Builder()
                .url(getFileUrl)
                .build()
            try {
                okClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        //get response body
                        val responseBody = response.body
                        val contentLength = responseBody!!.contentLength()

                        //get source and set destination
                        val source = responseBody.source()
                        val outputStream =
                            FileOutputStream(fileDestination.absolutePath)

                        //set file name
                        //  activity.setIndividualFileName(theFile.getFile_name());

                        //run pull algorithm
                        val buffer = ByteArray(4096)
                        var totalBytesRead: Long = 0
                        var bytesRead: Int
                        while (source.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead.toLong()

                            runOnUiThread {
                                try {

                                    ///  binding.textFileCounts.text = "$sN / "

                                    binding.textRemainging.visibility = View.VISIBLE

                                    val i = (totalBytesRead * 100 / contentLength).toInt()

                                    binding.progressBarPref.progress = i

                                    binding.textDownloadSieze.text = "$fileName : $i%"


                                } catch (_: Exception) {
                                }
                            }
                        }


                        //close IO stream
                        outputStream.flush()
                        outputStream.close()


                        runOnUiThread {
                            try {

                                // update the UI

                                binding.textFileCounts.text = "$sN / "

                                Thread {
                                    val dnApi = DnApi(
                                        SN = sN,
                                        FolderName = folderName,
                                        FileName = fileName,
                                        Status = "true"
                                    )
                                    dnViewModel.addFiles(dnApi)

                                }.start()

                                //   adapter.notifyDataSetChanged()

                                // set total download percentage
                                val totalPercentage =
                                    ((sN.toDouble() / totalFiles.toDouble()) * 100).toInt()
                                binding.textPercentageCompleted.text = "$totalPercentage% Complete"



                                if (sN == totalFiles.toString()) {
                                    binding.progressBarPref.progress = 100
                                    binding.textDownloadSieze.text = "Completed"
                                    mUserViewModel.deleteAllFiles()
                                    showCustomProgressDialog("Please wait!")

                                    myHandler.postDelayed(Runnable {
                                        stratMyACtivity()
                                    }, 1000)

                                }


                                /// call the next download
                                currentDownloadIndex++
                                downloadSequentially(
                                    mUserViewModel.readAllData.value ?: emptyList()
                                )


                            } catch (e: Exception) {

                            }
                        }


                    } else {

                        // bad url or failed download
                        runOnUiThread {
                            Thread {
                                val dnApi = DnApi(
                                    SN = sN,
                                    FolderName = folderName,
                                    FileName = fileName,
                                    Status = "false"
                                )
                                dnViewModel.addFiles(dnApi)

                            }.start()

                        }

                    }
                }
            } catch (e: IOException) {
                // Handle exception in download

                Log.d("Download", fileName + " Failed. Error: " + e.message.toString());

                // bad url or failed download
                runOnUiThread {
                    Thread {
                        val dnApi = DnApi(
                            SN = sN,
                            FolderName = folderName,
                            FileName = fileName,
                            Status = "false"
                        )
                        dnViewModel.addFiles(dnApi)

                    }.start()

                    if (sN == totalFiles.toString()) {
                        binding.progressBarPref.progress = 100
                        binding.textDownloadSieze.text = "Completed"
                        mUserViewModel.deleteAllFiles()
                        showCustomProgressDialog("Please wait!")

                        myHandler.postDelayed(Runnable {
                            stratMyACtivity()
                        }, 1000)

                    }

                    showToastMessage(e.message.toString())
                }

            }


        }.start()
    }

    private fun getZipDownloadsManually(sN: String, folderName: String, fileName: String) {
        Thread {

            val Syn2AppLive = Constants.Syn2AppLive
            val saveMyFileToStorage = "/$Syn2AppLive/CLO/MANUAL/DEMO/$folderName"

            val getSavedEditTextInputSynUrlZip = sharedP.getString(Constants.getSavedEditTextInputSynUrlZip, "").toString()

            var replacedUrl = getSavedEditTextInputSynUrlZip // Initialize it with original value


            if (getSavedEditTextInputSynUrlZip.contains("/Start/start1.csv")) {
                 replacedUrl = getSavedEditTextInputSynUrlZip.replace("/Start/start1.csv", "/$folderName/$fileName")

            } else if (getSavedEditTextInputSynUrlZip.contains("/Api/update1.csv")) {
                 replacedUrl = getSavedEditTextInputSynUrlZip.replace("/Api/update1.csv", "/$folderName/$fileName"
                )
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


            val request: Request = Request.Builder()
                .url(replacedUrl)
                .build()
            try {
                okClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        //get response body

                        Log.d("getZipDownloadsManually", replacedUrl)
                        Log.d("getZipDownloadsManually", saveMyFileToStorage)


                        val responseBody = response.body
                        val contentLength = responseBody!!.contentLength()

                        //get source and set destination
                        val source = responseBody.source()
                        val outputStream =
                            FileOutputStream(fileDestination.absolutePath)

                        //set file name
                        //  activity.setIndividualFileName(theFile.getFile_name());

                        //run pull algorithm
                        val buffer = ByteArray(4096)
                        var totalBytesRead: Long = 0
                        var bytesRead: Int
                        while (source.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead.toLong()

                            runOnUiThread {
                                try {

                                    ///  binding.textFileCounts.text = "$sN / "

                                    binding.textRemainging.visibility = View.VISIBLE

                                    val i = (totalBytesRead * 100 / contentLength).toInt()

                                    binding.progressBarPref.progress = i

                                    binding.textDownloadSieze.text = "$fileName : $i%"


                                } catch (_: Exception) {
                                }
                            }
                        }


                        //close IO stream
                        outputStream.flush()
                        outputStream.close()


                        runOnUiThread {
                            try {

                                // update the UI

                                binding.textFileCounts.text = "$sN / "

                                Thread {
                                    val dnApi = DnApi(
                                        SN = sN,
                                        FolderName = folderName,
                                        FileName = fileName,
                                        Status = "true"
                                    )
                                    dnViewModel.addFiles(dnApi)

                                }.start()

                                //   adapter.notifyDataSetChanged()

                                // set total download percentage
                                val totalPercentage =
                                    ((sN.toDouble() / totalFiles.toDouble()) * 100).toInt()
                                binding.textPercentageCompleted.text = "$totalPercentage% Complete"



                                if (sN == totalFiles.toString()) {
                                    binding.progressBarPref.progress = 100
                                    binding.textDownloadSieze.text = "Completed"
                                    mUserViewModel.deleteAllFiles()
                                    showCustomProgressDialog("Please wait!")

                                    myHandler.postDelayed(Runnable {
                                        stratMyACtivity()
                                    }, 1000)

                                }


                                /// call the next download
                                currentDownloadIndex++
                                downloadSequentiallyManuall(
                                    mUserViewModel.readAllData.value ?: emptyList()
                                )


                            } catch (e: Exception) {

                            }
                        }


                    } else {

                        // bad url or failed download
                        runOnUiThread {
                            Thread {
                                val dnApi = DnApi(
                                    SN = sN,
                                    FolderName = folderName,
                                    FileName = fileName,
                                    Status = "false"
                                )
                                dnViewModel.addFiles(dnApi)

                            }.start()

                        }

                    }
                }
            } catch (e: IOException) {
                // Handle exception in download

                Log.d("Download", fileName + " Failed. Error: " + e.message.toString());

                // bad url or failed download
                runOnUiThread {
                    Thread {
                        val dnApi = DnApi(
                            SN = sN,
                            FolderName = folderName,
                            FileName = fileName,
                            Status = "false"
                        )
                        dnViewModel.addFiles(dnApi)

                    }.start()

                    if (sN == totalFiles.toString()) {
                        binding.progressBarPref.progress = 100
                        binding.textDownloadSieze.text = "Completed"
                        mUserViewModel.deleteAllFiles()
                        showCustomProgressDialog("Please wait!")

                        myHandler.postDelayed(Runnable {
                            stratMyACtivity()
                        }, 1000)

                    }

                  //  showToastMessage(e.message.toString())
                }

            }


        }.start()


    }


    override fun onDestroy() {
        super.onDestroy()
        try {

            MyApplication.decrementRunningActivities()

            if (wakeLock != null && wakeLock!!.isHeld) {
                wakeLock!!.release()
            }

        } catch (ignored: java.lang.Exception) {
        }
    }


    override fun onBackPressed() {
        val intent = Intent(applicationContext, ReSyncActivity::class.java)
        startActivity(intent)
        finish()

        super.onBackPressed()
    }


    @SuppressLint("WakelockTimeout")
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


    private fun stratMyACtivity() {
        try {
            handler.postDelayed(Runnable {

                val getFolderClo = sharedP.getString("getFolderClo", "")
                val getFolderSubpath = sharedP.getString("getFolderSubpath", "")
                val Extracted = sharedP.getString("Extracted", "")

                val getUnzipFileFrom = "/$getFolderClo/$getFolderSubpath/$Extracted"
                val intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("getUnzipFileFrom", getUnzipFileFrom)

                startActivity(intent)
                finishAffinity()


                val get_intervals =
                    sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "")

                if (get_intervals != null && get_intervals == Constants.imagSwtichEnableSyncOnFilecahnge) {
                    stopService(Intent(applicationContext, OnChnageService::class.java))
                    stopService(Intent(applicationContext, IntervalApiServiceSync::class.java))
                    stopService(Intent(applicationContext, OnChangeApiServiceSync::class.java))
                    stopService(Intent(applicationContext, SyncInterval::class.java))
                    if (!ServiceUtils.foregroundServiceMyAPiSyncInterval(applicationContext)) {
                        startService(Intent(applicationContext, IntervalApiServiceSync::class.java))
                    }
                } else {
                    stopService(Intent(applicationContext, SyncInterval::class.java))
                    stopService(Intent(applicationContext, IntervalApiServiceSync::class.java))
                    stopService(Intent(applicationContext, OnChangeApiServiceSync::class.java))
                    stopService(Intent(applicationContext, OnChnageService::class.java))
                    if (!ServiceUtils.foregroundServiceMyAPiSyncOnChange(applicationContext)) {
                        startService(Intent(applicationContext, OnChangeApiServiceSync::class.java))
                    }
                }


            }, 6000)


        } catch (_: Exception) {
        }
    }

    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun showPopLauchOnline() {
        val bindingCm: LaucnOnlineDonloadPaggerBinding =
            LaucnOnlineDonloadPaggerBinding.inflate(
                layoutInflater
            )
        val builder = AlertDialog.Builder(this@DownloadTheApisActivity)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {

            textCancel.setOnClickListener {
                alertDialog.dismiss()
            }


            textYesButton.setOnClickListener {
                val editor = sharedP.edit()
                editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")
                editor.apply()
                stratLauncOnline()
                alertDialog.dismiss()
            }


        }

        alertDialog.show()
    }


    private fun showCustomProgressDialog(message: String) {
        try {
            val customProgressDialog = Dialog(this)
            val binding = ProgressDialogLayoutBinding.inflate(LayoutInflater.from(this))
            customProgressDialog.setContentView(binding.root)
            customProgressDialog.setCancelable(true)
            customProgressDialog.setCanceledOnTouchOutside(false)
            customProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.textLoading.text = "$message"

            binding.imgCloseDialog.visibility = View.GONE

            if (message.equals(Constants.Error_CSv_Message)){

            }


            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }
    private fun showCustomError(message: String) {
        try {
            val customProgressDialog = Dialog(this)
            val bindingErr = ErroMessageDialogLayoutBinding.inflate(LayoutInflater.from(this))
            customProgressDialog.setContentView(bindingErr.root)
            customProgressDialog.setCancelable(true)
            customProgressDialog.setCanceledOnTouchOutside(false)
            customProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            bindingErr.textLoading.text = "$message"

            bindingErr.textCancel.setOnClickListener {
                val intent = Intent(applicationContext, ReSyncActivity::class.java)
                startActivity(intent)
                finishAffinity()
                customProgressDialog.dismiss()
            }


            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }


    private fun stratLauncOnline() {
        try {

            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()


            val get_intervals =
                sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "")

            if (get_intervals != null && get_intervals == Constants.imagSwtichEnableSyncOnFilecahnge) {
                stopService(Intent(applicationContext, OnChnageService::class.java))
                stopService(Intent(applicationContext, SyncInterval::class.java))
                stopService(Intent(applicationContext, OnChangeApiServiceSync::class.java))
                if (!ServiceUtils.foregroundServiceMyAPiSyncInterval(applicationContext)) {
                    startService(Intent(applicationContext, IntervalApiServiceSync::class.java))
                }
            } else {
                stopService(Intent(applicationContext, SyncInterval::class.java))
                stopService(Intent(applicationContext, IntervalApiServiceSync::class.java))
                stopService(Intent(applicationContext, OnChnageService::class.java))
                if (!ServiceUtils.foregroundServiceMyAPiSyncOnChange(applicationContext)) {
                    //  startService(Intent(applicationContext, OnChangeApiServiceSync::class.java))
                }
            }


            val getFolderClo = sharedP.getString(Constants.getFolderClo, "")
            val getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "")

            val editor = sharedP.edit()

            val url =
                "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"

            //  editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")
            editor.putString(Constants.getFolderClo, getFolderClo)
            editor.putString(Constants.getFolderSubpath, getFolderSubpath)
            editor.putString(Constants.syncUrl, url)
            editor.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline)
            editor.apply()


            val intent = Intent(applicationContext, WebActivity::class.java)
            startActivity(intent)
            finish()


        } catch (_: Exception) {
        }
    }


}




