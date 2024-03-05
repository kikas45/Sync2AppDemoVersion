package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.SettingsActivity
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.myService.NotificationService
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityMaintenanceBinding
import sync2app.com.syncapplive.databinding.CustomCrashReportBinding
import sync2app.com.syncapplive.databinding.CustomFailedLayoutBinding
import sync2app.com.syncapplive.databinding.CustomShowDownloadProgressBinding

class MaintenanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceBinding

    private lateinit var progressBarPref: ProgressBar
    private lateinit var downloadBytes: TextView



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
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()


        Handler(Looper.getMainLooper()).postDelayed({
            getCarshReports()
        }, 700)





        binding.apply {


            val editor = sharedBiometric.edit()


            /// enable the Auto Boot
            imagEnableDownloadStatus.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {

                    editor.putString(Constants.showDownloadSyncStatus, "showDownloadSyncStatus")
                    editor.apply()
                    binding.textCheckDownloadStatus2.text = "Show Download Status"

                } else {
                    editor.remove(Constants.showDownloadSyncStatus)
                    editor.apply()
                    binding.textCheckDownloadStatus2.text = "Hide Download Status"
                }
            }


            val get_imagEnableDownloadStatus = sharedBiometric.getString(Constants.showDownloadSyncStatus, "")


            imagEnableDownloadStatus.isChecked =
                get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)

            if (get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)) {

                binding.textCheckDownloadStatus2.text = "Show Download Status"

            } else {

                binding.textCheckDownloadStatus2.text = "Hide Download Status"

            }







            closeBs.setOnClickListener {

                val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")

                if (get_navigationS2222.equals(Constants.SettingsPage)) {
                    val intent = Intent(applicationContext, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                    val intent =
                        Intent(applicationContext, AdditionalSettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }

            textView42.setOnClickListener {
                //  val intent = Intent(applicationContext, TestScreenActivity::class.java)
                //  startActivity(intent)
            }


            /*
                        textTestCrash.setOnClickListener {
                            throw RuntimeException("Deliberate crash triggered")
                        }
            */


            textHardwarePage.setOnClickListener {
                val intent = Intent(applicationContext, SystemInfoActivity::class.java)
                startActivity(intent)


            }




            textFileManger.setOnClickListener {
                startActivity(Intent(applicationContext, FileExplorerActivity::class.java))
            }



            textCrashPage.setOnClickListener {

                val sharedCrashReport =
                    getSharedPreferences(Constants.SHARED_SAVED_CRASH_REPORT, MODE_PRIVATE)
                val crashInfo = sharedCrashReport.getString(Constants.crashInfo, "")
                if (!crashInfo.isNullOrEmpty()) {
                    showPopCrashReport(crashInfo.toString())
                } else {
                    showToastMessage("No crash report yet")
                }


            }

        }


    }

    private fun getCarshReports() {
        val sharedCrashReport =
            getSharedPreferences(Constants.SHARED_SAVED_CRASH_REPORT, MODE_PRIVATE)
        val crashInfo = sharedCrashReport.getString(Constants.crashInfo, "")
        val crashCalled = sharedCrashReport.getString(Constants.crashCalled, "")
        if (!crashCalled.isNullOrEmpty()) {
            showPopCrashReport(crashInfo + "")
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopCrashReport(message: String) {
        val binding: CustomCrashReportBinding =
            CustomCrashReportBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.getRoot())

        val alertDialog = alertDialogBuilder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        alertDialog.setCanceledOnTouchOutside(true)

        binding.textDisplayResults.setText(message)
        binding.textContinuPassword2.setOnClickListener { view ->
            val sharedCrashReport = getSharedPreferences(
                Constants.SHARED_SAVED_CRASH_REPORT,
                MODE_PRIVATE
            )
            val editor = sharedCrashReport.edit()
            editor.remove(Constants.crashCalled)
            editor.apply()
            sendCrashReport(message)
            alertDialog.dismiss()
        }

        // Show the AlertDialog
        alertDialog.show()
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }

    fun sendCrashReport(crashMessage: String?) {
        //  val email = "kola@moreadvice.co.uk"
        val email = "support@syn2app.com"
        val subject = "Crash Report"

        val uriText = "mailto:" + Uri.encode(email) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(crashMessage)

        val uri = Uri.parse(uriText)

        // Intent for sending text
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, crashMessage)
        sendIntent.type = "text/plain"

        // Intent for sending email
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = uri

        // Create a chooser with both intents
        val chooserIntent = Intent.createChooser(sendIntent, "Send message via:")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(emailIntent))

        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            // Handle case where no email app is available
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            MyApplication.decrementRunningActivities()
        } catch (ignored: java.lang.Exception) {
        }
    }


    override fun onBackPressed() {
        val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")

        if (get_navigationS2222.equals(Constants.SettingsPage)) {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
            val intent =
                Intent(applicationContext, AdditionalSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




}