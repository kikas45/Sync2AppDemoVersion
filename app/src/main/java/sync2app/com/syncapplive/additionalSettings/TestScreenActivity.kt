package sync2app.com.syncapplive.additionalSettings


import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnApi
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DnViewModel
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.DownloadHelper
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.SavedDownloads
import sync2app.com.syncapplive.additionalSettings.myCompleteDownload.ZipDownloader
import sync2app.com.syncapplive.additionalSettings.myService.MyApiService
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TestScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScreenBinding
    private var powerManager: PowerManager? = null
    private var wakeLock: PowerManager.WakeLock? = null


    private var currentDownloadIndex = 0

    private lateinit var receiver: BroadcastReceiver

    private val mUserViewModel by viewModels<FilesViewModel>()

    private val dnViewModel by viewModels<DnViewModel>()


    private var totalFiles: Int = 0

    private val adapter by lazy {
        SavedDownloads()
    }



    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    @SuppressLint("NotifyDataSetChanged", "WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.textDsiplayDownloadSieze.setOnClickListener {
            stopService(Intent(applicationContext, MyApiService::class.java))
            if (!ServiceUtils.foregroundServiceMyAPi(applicationContext)) {
                startService(Intent(applicationContext, MyApiService::class.java))
                showToastMessage("Calling Servuice")
                getDownloadMyCSV()
                mUserViewModel.deleteAllFiles()

            }else{
                showToastMessage("No Servuice")
            }
        }










        powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager!!.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YourApp::MyWakelockTag")
        wakeLock!!.acquire()



        binding.progressBarPref.secondaryProgress = 0
        dnViewModel.deleteAllFiles()
        mUserViewModel.deleteAllFiles()

        binding.apply {

            myHandler.postDelayed(Runnable {
                recyclerApiDownloads.adapter = adapter
                recyclerApiDownloads.layoutManager = LinearLayoutManager(applicationContext)

                dnViewModel.readAllData.observe(this@TestScreenActivity, Observer { filesApi ->
                    adapter.setDataApi(filesApi)

                })
            }, 500)
        }



        binding.btnDownload.setOnClickListener {
            dnViewModel.deleteAllFiles()
            mUserViewModel.deleteAllFiles()
            adapter.notifyDataSetChanged()
            stopService(Intent(applicationContext, MyApiService::class.java))
            currentDownloadIndex  = 0
            myHandler.postDelayed(Runnable {
                getDownloadMyCSV()
            }, 500)



        }


    }

    private fun downloadSequentially(files: List<FilesApi>) {

        if (currentDownloadIndex < files.size) {
            val file = files[currentDownloadIndex]

            getZipDownloads(file.SN, file.FolderName, file.FileName)

        } else {


        }
    }


    private fun showToastMessage(messages: String) {
        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }



    private fun getDownloadMyCSV() {

        lifecycleScope.launch(Dispatchers.IO) {
            val csvData = downloadCSV("CLO", "DE_MO_2021001")
            saveURLPairs(csvData)

            runOnUiThread {

                mUserViewModel.readAllData.observe(this@TestScreenActivity, Observer { files ->
                    if (files.isNotEmpty()) {
                        downloadSequentially(files)
                        binding.textFilecount.text = files.size.toString()

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

                val fileName = folderAndFile.lastOrNull() ?: continue // Skip lines with missing file name
                val status = "" // Set your status here

                val files = FilesApi(SN = sn.toString(), FolderName = folderName, FileName = fileName, Status = status)
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


    private fun downloadCSV(clo: String, demo: String): String {
        val stringBuilder = StringBuilder()
        try {

            val downloadUrl = "https://cloudappserver.co.uk/cp/app_base/public/$clo/$demo/Start/start1.csv"

            Log.d("downloadUrl", "downloadUrl: $downloadUrl")

            val url = URL(downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            val code = connection.responseCode

            if (code == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
            } else {
                Log.e("CSVReadError", "Response code: $code")
            }
        } catch (e: Exception) {
            Log.e("CSVReadError", "Error reading CSV: ${e.message}")
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


    private fun getZipDownloads(sN:String, folderName:String, fileName:String) {

        //  totalFiles++

        //fetch item
        Thread {
            val saveMyFileToStorage = "/MyAPiDownloads/$folderName"

            // Adjusting the file path to save the downloaded file
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), saveMyFileToStorage)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val getFile_name = fileName.toString()
            val getFileUrl = "https://cloudappserver.co.uk/cp/app_base/public/CLO/DE_MO_2021000/$folderName/$fileName"
            val fileDestination = File(dir.absolutePath, getFile_name)



            //start download
            ZipDownloader(object : DownloadHelper {
                @SuppressLint("SetTextI18n")
                override fun afterExecutionIsComplete() {

                    Thread {
                        runOnUiThread {
                            val dnApi = DnApi(SN = sN,  FolderName = folderName, FileName = fileName, Status = "")
                            dnViewModel.addFiles(dnApi)


                            currentDownloadIndex++
                            downloadSequentially(mUserViewModel.readAllData.value ?: emptyList())

                            // set total download percentage
                            val totalPercentage = ((sN.toDouble() / totalFiles.toDouble()) * 100).toInt()
                            binding.textTotalFiles.text = "$totalPercentage% "

                            if (sN == totalFiles.toString()){
                                binding.progressBarPref.progress = 100
                                binding.textDownloadSieze.text = "Completed"
                                mUserViewModel.deleteAllFiles()
                            }

                        }

                    }.start()


                }

                override fun whenExecutionStarts() {

                }

                @SuppressLint("SetTextI18n")
                override fun whileInProgress(i: Int) {
                    //set individual progress
                    binding.textDsiplayDownloadSieze.text = "$sN    Downloading..  $fileName"
                    runOnUiThread {
                        binding.progressBarPref.progress = i

                        val progressPercentage = ((i.toDouble() / i.toFloat()) * 100).toInt()

                        if (i<0){
                            binding.textDownloadSieze.text = "$progressPercentage% Downloaded"
                        }else{
                            binding.textDownloadSieze.text = "$i% Downloaded"

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

}






