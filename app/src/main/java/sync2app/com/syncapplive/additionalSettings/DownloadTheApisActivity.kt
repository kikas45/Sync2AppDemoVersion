package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnApi
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DownloadHelper
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.SavedDownloads
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.ZipDownloader
import sync2app.com.syncapplive.additionalSettings.myService.CSVDownloader
import sync2app.com.syncapplive.additionalSettings.myService.MyApiService
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService
import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils
import sync2app.com.syncapplive.databinding.ActivityDownloadTheApisBinding
import sync2app.com.syncapplive.databinding.LaucnOnlineDonloadPaggerBinding
import sync2app.com.syncapplive.databinding.ProgressDialogLayoutBinding
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadTheApisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadTheApisBinding
    private var powerManager: PowerManager? = null
    private var wakeLock: PowerManager.WakeLock? = null


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


        powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YourApp::MyWakelockTag")
        wakeLock!!.acquire()

        binding.closeBs.setOnClickListener {
            val intent = Intent(applicationContext, ReSyncActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.textLaunchApplication.setOnClickListener {

            showPopLauchOnline()
        }


        binding.textRetryBtn.setOnClickListener {
            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()
            val intent = Intent(applicationContext, DownloadTheApisActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.textCancelBtn.setOnClickListener {
            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()
            finish()
            val intent = Intent(applicationContext, ReSyncActivity::class.java)
            startActivity(intent)

        }






        binding.apply {

            myHandler.postDelayed(Runnable {
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
            getDownloadMyCSV()

        } else {
            Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, ReSyncActivity::class.java)
        startActivity(intent)
        finish()

        super.onBackPressed()
    }

    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]

            getZipDownloads(file.SN, file.FolderName, file.FileName)

        } else {


        }
    }


    @SuppressLint("SetTextI18n")
    private fun getDownloadMyCSV() {

        lifecycleScope.launch(Dispatchers.IO) {

            val getFolderClo = sharedP.getString(Constants.getFolderClo, "").toString()
            val getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "").toString()
            val get_ModifiedUrl = sharedP.getString(Constants.get_ModifiedUrl, "").toString()

            //  val csvData = downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath)
            //  saveURLPairs(csvData)

            val lastEnd = "Start/start1.csv";
            val csvDownloader = CSVDownloader()
            val csvData =
                csvDownloader.downloadCSV(get_ModifiedUrl, getFolderClo, getFolderSubpath, lastEnd)
            saveURLPairs(csvData)

            runOnUiThread {

                mUserViewModel.readAllData.observe(this@DownloadTheApisActivity, Observer { files ->
                    if (files.isNotEmpty()) {
                        downloadSequentially(files)
                        binding.textRemainging.text = files.size.toString() + " Files Downloaded"

                        totalFiles = files.size.toInt()

                    } else {
                        // showToastMessage("No files found to download")
                    }
                })

            }
        }


    }


    private fun saveURLPairs(csvData: String) {
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


            //start download
            ZipDownloader(object : DownloadHelper {
                @SuppressLint("SetTextI18n")
                override fun afterExecutionIsComplete() {

                    val dnApi =
                        DnApi(SN = sN, FolderName = folderName, FileName = fileName, Status = "")
                    dnViewModel.addFiles(dnApi)


                    currentDownloadIndex++
                    downloadSequentially(mUserViewModel.readAllData.value ?: emptyList())

                    // set total download percentage
                    val totalPercentage = ((sN.toDouble() / totalFiles.toDouble()) * 100).toInt()
                    binding.textPercentageCompleted.text = "$totalPercentage% Complete"
                    // binding.progressBarPref.progress = 100

                    if (sN == totalFiles.toString()) {
                        binding.progressBarPref.progress = 100
                        binding.textDownloadSieze.text = "Completed"
                        mUserViewModel.deleteAllFiles()
                        showCustomProgressDialog("Please wait!")

                        myHandler.postDelayed(Runnable {
                            stratMyACtivity()
                        }, 1000)

                    }


                }

                override fun whenExecutionStarts() {

                }

                @SuppressLint("SetTextI18n")
                override fun whileInProgress(i: Int) {
                    //set individual progress
                    binding.textFileCounts.text = "$sN / "

                    binding.textRemainging.visibility = View.VISIBLE

                    runOnUiThread {
                        binding.progressBarPref.progress = i

                        val progressPercentage = ((i.toDouble() / i.toFloat()) * 100).toInt()

                        if (i < 0) {
                            binding.textDownloadSieze.text = "$fileName : $progressPercentage%"
                        } else {
                            binding.textDownloadSieze.text = "$fileName : $i%"

                        }


                    }
                }
            }).execute(getFileUrl, fileDestination.absolutePath)
        }.start()
    }

    override fun onStop() {
        super.onStop()


        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        if (wakeLock != null && wakeLock!!.isHeld) {
            wakeLock!!.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock != null && wakeLock!!.isHeld) {
            wakeLock!!.release()
        }

    }

    @SuppressLint("WakelockTimeout")
    override fun onResume() {
        super.onResume()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                finish()



                stopService(Intent(applicationContext, MyApiService::class.java))
                stopService(Intent(applicationContext, SyncInterval::class.java))
                stopService(Intent(applicationContext, OnChnageService::class.java))
                if (!ServiceUtils.foregroundServiceMyAPi(applicationContext)) {
                    startService(Intent(applicationContext, MyApiService::class.java))
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


    private fun stratLauncOnline() {
        try {

            val getFolderClo = sharedP.getString("getFolderClo", "")
            val getFolderSubpath = sharedP.getString("getFolderSubpath", "")

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

            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }


}




