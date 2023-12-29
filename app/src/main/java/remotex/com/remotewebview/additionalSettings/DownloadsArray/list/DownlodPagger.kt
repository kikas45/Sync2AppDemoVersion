package remotex.com.remotewebview.additionalSettings.DownloadsArray.list

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remotex.com.remotewebview.WebActivity
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityDownlodPaggerBinding
import remotex.com.remotewebview.databinding.ProgressDialogLayoutBinding
import java.io.File
import java.io.FileInputStream
import java.util.Objects
import java.util.zip.ZipInputStream

class DownlodPagger : AppCompatActivity() {
    private lateinit var binding: ActivityDownlodPaggerBinding

    private var isValid = false
    private var isreadyForWebview = false

    private var getToatlFilePath = ""
    private var unzipManual = ""
    private var fileName = ""
    private var patterThreePath = ""

   // private lateinit var customProgressDialog: Dialog


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



    private val savedDownloadPath: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SAVE_M_DOWNLOAD_PATH,
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







        binding.apply {
            closeBs.setOnClickListener {
                onBackPressed()
            }

            textTitle.setOnClickListener {
             //   showCustomProgressDialog("Please wait")
             //       funUnZipFile()
            }

            binding.textContinuPassword222222.setOnClickListener {
                showToastMessage("Please for Complete Download and Zip extraction")
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
            val unzipManual22 = intent.getStringExtra("unzipManual")
            val patterThreePath22 = intent.getStringExtra("patterThreePath")

            val baseUrl22 = intent.getStringExtra("baseUrl")
            val getFolderClo = intent.getStringExtra("getFolderClo")
            val getFolderSubpath = intent.getStringExtra("getFolderSubpath")
            val Zip = intent.getStringExtra("Zip")
            val fileName22 = intent.getStringExtra("fileName")
            val Extracted = intent.getStringExtra("Extracted")


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
                    download(baseUrl22, getFolderClo.toString(), getFolderSubpath.toString(), Zip.toString(), fileName22.toString(), Extracted.toString())
                }else{
                    onBackPressed()
                }
            }


        }



    }

    private val runnable: Runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            getDownloadStatus()
            myHandler!!.postDelayed(this, 500)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                // binding.textContinuPassword222222.isEnabled = false
                isValid = true
            }

            DownloadManager.STATUS_RUNNING -> {
                msg = "Downloading.."
                binding.imagePauseDownload.visibility = View.VISIBLE
                binding.imageResumeDownload.visibility = View.INVISIBLE
                //  binding.textContinuPassword222222.isEnabled = false

                isValid = true

            }

            DownloadManager.STATUS_PAUSED -> {
                msg = "Resume"
                binding.imagePauseDownload.visibility = View.INVISIBLE
                binding.imageResumeDownload.visibility = View.VISIBLE
                //  binding.textContinuPassword222222.isEnabled = false
                isValid = true
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

                        showCustomProgressDialog("Please wait")
                        handler.postDelayed(Runnable {
                            funUnZipFile()
                        }, 250)
                    } else {
                        showToastMessage("Download completed")
                    }
                }

            }

            DownloadManager.STATUS_FAILED -> {
                msg = "Failed!, Retry.."
                isValid = false
            }

            else -> {
                msg = "failed! , try again.. "

            }
        }
        return msg
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

            customProgressDialog.show()
        } catch (_: Exception) {
        }
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




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        try {

            val getFolderClo = savedDownloadPath.getString("getFolderClo", "")
            val getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "")
            val Zip = savedDownloadPath.getString("Zip", "")
            val fileNamy = savedDownloadPath.getString("fileNamy", "")
            val Extracted = savedDownloadPath.getString("Extracted", "")

            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
            val finalFolderPathDesired = "/$getFolderClo/$getFolderSubpath/$Extracted"



            val directoryPathString = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPath
            val destinationFolder = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPathDesired)

            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs()
            }

            val myFile = File(directoryPathString, fileNamy)
            if (myFile.exists()) {
                extractZip(myFile.toString(), destinationFolder.toString())
            }else{
                showToastMessage("Something went wrong, this file could not be found ")
            }

        }catch (_:Exception){}

    }





    fun extractZip(zipFilePath: String, destinationPath: String) {

            try {
                showToastMessage("Zip extraction started")

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
                }

                zipInputStream.close()

                stratMyACtivity()
                showToastMessage("Zip completed")

            } catch (e: Exception) {
                e.printStackTrace()
                showToastMessage("Error during zip extraction")
                stratMyACtivity()

            }
        }

    private fun stratMyACtivity() {
        try {
       handler.postDelayed(Runnable {
           val intent = Intent(applicationContext, WebActivity::class.java)
           intent.putExtra("unzipManual", unzipManual)

           intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
           startActivity(intent)
           finish()
       }, 3000)
        }catch (_:Exception){}
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

            val download_ref: Long = sharedP.getLong(Constants.downloadKey, -15)

            val getFolderClo = sharedP.getString("getFolderClo", "")
            val getFolderSubpath = sharedP.getString("getFolderSubpath", "")
            val Zip = sharedP.getString("Zip", "")
            val fileName = sharedP.getString("fileName", "")


            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"

            val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPath

            val myFile = File(directoryPath, fileName)

            if (myFile.exists()) {
                myFile.delete()
                showToastMessage("File deleted successfully")
            } else {
                showToastMessage("File does not exist")
            }


            val query = DownloadManager.Query()
            query.setFilterById(download_ref)
            val c =
                (applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).query(query)
            if (c.moveToFirst()) {
                manager!!.remove(download_ref)
                val editor: SharedPreferences.Editor = sharedP.edit()
                editor.remove(Constants.downloadKey)
                editor.apply()
                onBackPressed()
            }
        } catch (ignored: java.lang.Exception) {
        }
    }



    private fun download(url: String, getFolderClo:String,  getFolderSubpath:String, Zip: String, fileNamy: String, Extracted:String) {
        val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
        val Syn2AppLive = "Syn2AppLive"


        val editior = savedDownloadPath.edit()
        editior.putString("getFolderClo", getFolderClo)
        editior.putString("getFolderSubpath", getFolderSubpath)
        editior.putString("Zip", Zip)
        editior.putString("fileNamy", fileNamy)
        editior.putString("Extracted", Extracted)
        editior.apply()


        val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPath

        val myFile = File(directoryPath, fileNamy)

        if (myFile.exists()) {
            myFile.delete()
            //  showToastMessage("File deleted successfully")
        } else {
            //  showToastMessage("File does not exist")
        }


        val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val folder = File(Environment.DIRECTORY_DOWNLOADS.toString() + "/$Syn2AppLive/$finalFolderPath")

        if (!folder.exists()) {
            folder.mkdirs()
        }


        // Continue with the download logic
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileNamy)
        // request.setDescription("Downloading...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, "/$Syn2AppLive/$finalFolderPath/$fileNamy"
        )
        val downloadReferenceMain = managerDownload.enqueue(request)

        val sharedPreferences = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putLong(Constants.downloadKey, downloadReferenceMain)
        editor.apply()
    }




}