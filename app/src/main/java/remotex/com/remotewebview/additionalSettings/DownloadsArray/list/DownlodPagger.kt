package remotex.com.remotewebview.additionalSettings.DownloadsArray.list

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import remotex.com.remotewebview.additionalSettings.scanutil.ZipWorker
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityDownlodPaggerBinding
import java.io.File
import java.util.Objects

class DownlodPagger : AppCompatActivity() {
    private lateinit var binding: ActivityDownlodPaggerBinding

    private var isValid = false

    private var getToatlFilePath = ""
    private var unzipManual = ""
    private var fileName = ""
    private var patterThreePath = ""

    var manager: DownloadManager? = null

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    private val sharedP: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownlodPaggerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = getApplicationContext()
            .getSystemService(DOWNLOAD_SERVICE) as DownloadManager
      //  isValid = true



        binding.textTitle.setOnClickListener {
            val textSynfromApiZip222 = sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")
            val imagUsemanualOrnotuseManual = sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

            showToastMessage("$imagUsemanualOrnotuseManual ")

        }

        binding.apply {
            closeBs.setOnClickListener {
                onBackPressed()
            }

            imagePauseDownload.setOnClickListener {
                pauseDownload()
               showToastMessage("Paused")
            }


            imageResumeDownload.setOnClickListener {
               resumeDownload()
                showToastMessage("Resuming..")

            }


            textContinuPassword2.setOnClickListener {
                second_cancel_download()
            }

            val getToatlFilePath22 = intent.getStringExtra("getToatlFilePath")
            val baseUrl22 = intent.getStringExtra("baseUrl")
            val unzipManual22 = intent.getStringExtra("unzipManual")
            val fileName22 = intent.getStringExtra("fileName")
            val patterThreePath22 = intent.getStringExtra("patterThreePath")

            if (getToatlFilePath22 !=null &&baseUrl22 !=null){
                textTitleFileName.text = fileName22.toString()
                textPathFolderName.text = patterThreePath22.toString()


                getToatlFilePath = getToatlFilePath22.toString()
                patterThreePath = patterThreePath22.toString()
                unzipManual = unzipManual22.toString()
                fileName = fileName22.toString()
            }

            textContinuPassword222.setOnClickListener {
                showToastMessage("Please wait..")
                if (baseUrl22 != null && fileName22 != null){
                    download(baseUrl22.toString(), patterThreePath22.toString(), fileName22.toString())
                }else{
                    onBackPressed()
                }
            }


        }



    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            getDownloadStatus()
            myHandler!!.postDelayed(this, 500)
        }
    }


    @SuppressLint("SetTextI18n")
    fun getDownloadStatus() {
        try {

            val download_ref = sharedP.getLong(Constants.downloadKey, -15)
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
                binding.progressBarPref.setProgress(dl_progress)
                binding.downloadBytes.setText(
                    bytesIntoHumanReadable(
                        bytes_downloaded.toString().toLong()
                    ) + "/" + bytesIntoHumanReadable(bytes_total.toString().toLong())
                )


                if (c == null) {
                    binding.textView10.setText(statusMessage(c))
                } else {
                    c.moveToFirst()
                    binding.textView10.setText(statusMessage(c))
                }
            }
        } catch (ignored: java.lang.Exception) {
        }
    }


    @SuppressLint("Range", "SetTextI18n")
    private fun statusMessage(c: Cursor): String? {
        var msg: String
        when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            DownloadManager.STATUS_PENDING -> {
                msg = "Pending.."
              binding.imagePauseDownload.visibility = View.VISIBLE
                binding.imageResumeDownload.visibility = View.INVISIBLE

            }

            DownloadManager.STATUS_RUNNING -> {
                msg = "Downloading.."
                binding.imagePauseDownload.visibility = View.VISIBLE
                binding.imageResumeDownload.visibility = View.INVISIBLE

                isValid = true
            }

            DownloadManager.STATUS_PAUSED -> {
                msg = "Resume"
                binding.imagePauseDownload.visibility = View.INVISIBLE
                binding.imageResumeDownload.visibility = View.VISIBLE

            }

            DownloadManager.STATUS_SUCCESSFUL -> {
                msg = "Completed"
                binding.imagePauseDownload.visibility = View.INVISIBLE
                binding.imageResumeDownload.visibility = View.VISIBLE
                binding.imagePauseDownload.isEnabled = false
                binding.imageResumeDownload.isEnabled = false


                val textSynfromApiZip222 = sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")
                val imagUsemanualOrnotuseManual = sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")

                if (isValid == true) {
                    isValid = false
                    if (textSynfromApiZip222.equals(Constants.imagSwtichEnableSyncFromAPI) && !imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {
                        funUnZipFile()
                    } else {
                        showToastMessage("Download completed")
                    }
                }

            }

            DownloadManager.STATUS_FAILED -> {
                msg = "Failed!, Retry.."

            }

            else -> {
                msg = "failed! , try again.. "

            }
        }
        return msg
    }

    private fun bytesIntoHumanReadable(bytes: Long): String? {
        val kilobyte: Long = 1024
        val megabyte = kilobyte * 1024
        val gigabyte = megabyte * 1024
        val terabyte = gigabyte * 1024
        return if (bytes >= 0 && bytes < kilobyte) {
            "$bytes B"
        } else if (bytes >= kilobyte && bytes < megabyte) {
            (bytes / kilobyte).toString() + " KB"
        } else if (bytes >= megabyte && bytes < gigabyte) {
            (bytes / megabyte).toString() + " MB"
        } else if (bytes >= gigabyte && bytes < terabyte) {
            (bytes / gigabyte).toString() + " GB"
        } else if (bytes >= terabyte) {
            (bytes / terabyte).toString() + " TB"
        } else {
            bytes.toString() + ""
        }
    }



    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        getDownloadStatus()

        try {
            if (myHandler != null) {
                myHandler!!.removeCallbacks(runnable)
            }
        } catch (ignored: java.lang.Exception) {
        }
        if (myHandler != null) {
            myHandler!!.postDelayed(runnable, 500)
        }
    }


    override fun onPause() {
        super.onPause()
        try {
            if (myHandler != null) {
                myHandler!!.removeCallbacks(runnable)
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (myHandler != null) {
                myHandler!!.removeCallbacks(runnable)
            }
            // Unregister the BroadcastReceiver to avoid memory leaks
            // unregisterReceiver(downloadReceiver)

        } catch (ignored: java.lang.Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (myHandler != null) {
                myHandler!!.removeCallbacks(runnable)
            }
        } catch (ignored: java.lang.Exception) {
        }
    }


    private fun funUnZipFile() {

        val Syn2AppLive = "Syn2AppLive"
        val CLO = "CLO"
        val MANUAL = "MANUAL"
        val Zip = "Zip"
        val App = "App.zip"


        //  showToastMessage(getToatlFilePath)


        // This is the path we are extracting to
        val folderToExtractTo = "/$Syn2AppLive" + unzipManual

        val destinationFolder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),   folderToExtractTo)
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs()
        }



        // This is where we are extracting from
        // val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$Syn2AppLive/$CLO/$MANUAL/$Zip/$App"
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$Syn2AppLive" +  getToatlFilePath



        val downloadedFile = File(filePath)

        if (downloadedFile.exists()) {
            //   showToastMessage("File Name: ${downloadedFile.name}")

            startZipExtractionWork(filePath, destinationFolder.toString())

        } else {
            // showToastMessage("File does not exist in the folder.")
        }



    }


    private fun startZipExtractionWork(zipFilePath: String, destinationPath: String) {
        showToastMessage("Zip extraction started")
        val workRequest: WorkRequest =
            OneTimeWorkRequestBuilder<ZipWorker>()
                .setInputData(
                    workDataOf(
                        "zipFilePath" to zipFilePath,
                        "destinationPath" to destinationPath
                    )
                )
                .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }




    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun pauseDownload(): Boolean {
        var updatedRow = 0
        val contentValues = ContentValues()
        contentValues.put("control", 1)
        try {
            updatedRow = Objects.requireNonNull<Context>(applicationContext).contentResolver.update(
                Uri.parse("content://downloads/my_downloads"),
                contentValues,
                "title=?",
                arrayOf<String>(fileName)
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0 < updatedRow
    }


    private fun resumeDownload(): Boolean {
        var updatedRow = 0
        val contentValues = ContentValues()
        contentValues.put("control", 0)
        try {
            updatedRow = Objects.requireNonNull<Context>(applicationContext).contentResolver.update(
                Uri.parse("content://downloads/my_downloads"),
                contentValues,
                "title=?",
                arrayOf<String>(fileName)
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0 < updatedRow
    }


    private fun second_cancel_download() {
        try {
            val Syn2AppLive = "Syn2AppLive"

            val download_ref: Long = sharedP.getLong(Constants.downloadKey, -15)
          //  Toast.makeText(applicationContext,  "" + download_ref, Toast.LENGTH_SHORT).show()
            val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$Syn2AppLive" +  getToatlFilePath

            val downloadedFile = File(filePath)

            if (downloadedFile.exists()) {
               //   showToastMessage("File Name: ${downloadedFile.name}")
            }

            val query = DownloadManager.Query()
            query.setFilterById(download_ref)
            val c =
                (applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).query(query)
            if (c.moveToFirst()) {
                manager!!.remove(download_ref)
             //   binding.downloadBytes.visibility = View.INVISIBLE
              //  binding.progressBarPref.progress = 0
                val editor: SharedPreferences.Editor = sharedP.edit()
                editor.remove(Constants.downloadKey)
                editor.apply()
                onBackPressed()
            }
        } catch (ignored: java.lang.Exception) {
        }
    }


    private fun download(url: String, finalFolderPath: String, fileName: String) {
        val managerDownload =  getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val Syn2AppLive = "Syn2AppLive"
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Download/$Syn2AppLive/$finalFolderPath"
        )

        if (!folder.exists()) {
            folder.mkdirs()
        }

        // binding.textTitle.text = fileName.toString()

        val destinationFile = File(folder, fileName)
        if (destinationFile.exists()) {
            destinationFile.delete()
        }

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileName)
        // request.setDescription("Downloading...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$Syn2AppLive/$finalFolderPath/$fileName")
        val downloadReferenceMain = managerDownload.enqueue(request)

        val sharedPreferences = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putLong(Constants.downloadKey, downloadReferenceMain)
        editor.apply()

    }


}