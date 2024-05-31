package sync2app.com.syncapplive.additionalSettings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityMaintenanceBinding


class MaintenanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceBinding



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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editor = sharedBiometric.edit()



        // show online Status

        binding.apply {


            imagShowOnlineStatus.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {
                    editor.remove(Constants.imagShowOnlineStatus)
                    editor.apply()
                    binding.textShowOnlineStatus.text = "Show Online Status Indicator"

                } else {
                    editor.putString(Constants.imagShowOnlineStatus, "imagShowOnlineStatus")
                    editor.apply()
                    binding.textShowOnlineStatus.text = "Hide Online Status Indicator"
                }
            }




            val get_indicator_satate = sharedBiometric.getString(Constants.img_Make_OnlineIndicator_Default_visible, "")

            if (get_indicator_satate.isNullOrEmpty()){
                editor.putString(Constants.img_Make_OnlineIndicator_Default_visible, "img_Make_OnlineIndicator_Default_visible")
                editor.remove(Constants.imagShowOnlineStatus)
                editor.apply()
                binding.textShowOnlineStatus.text = "Show Online Status Indicator"
                binding.imagShowOnlineStatus.isChecked = true

            }else{

                val get_imagShowOnlineStatus = sharedBiometric.getString(Constants.imagShowOnlineStatus, "")

                imagShowOnlineStatus.isChecked = get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)

                if (get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)) {

                    binding.textShowOnlineStatus.text = "Hide Online Status Indicator"
                    binding.imagShowOnlineStatus.isChecked = false

                } else {
                    binding.textShowOnlineStatus.text = "Show Online Status Indicator"
                    binding.imagShowOnlineStatus.isChecked = true


                }



            }


        }




        binding.apply {





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


            imagEnableDownloadStatus.isChecked = get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)

            if (get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)) {

                binding.textCheckDownloadStatus2.text = "Show Download Status"

            } else {

                binding.textCheckDownloadStatus2.text = "Hide Download Status"

            }




            closeBs.setOnClickListener {

                startActivity(Intent(applicationContext, WebActivity::class.java))
                finish()
            }




        }

        binding.apply {


            // Restart app on Tv or Mobile mode
            imgStartAppRestartOnTvMode.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {

                    editor.putString(Constants.imgStartAppRestartOnTvMode, Constants.imgStartAppRestartOnTvMode)
                    editor.apply()
                    binding.textShowAppRestartTvMode.text = "Auto Restart When Crashed"

                } else {
                    editor.remove(Constants.imgStartAppRestartOnTvMode)
                    editor.apply()
                    binding.textShowAppRestartTvMode.text = "Auto Restart When Crashed"
                }
            }


            val get_imgStartAppRestartOnTvMode = sharedBiometric.getString(Constants.imgStartAppRestartOnTvMode, "")


            imgStartAppRestartOnTvMode.isChecked = get_imgStartAppRestartOnTvMode.equals(Constants.imgStartAppRestartOnTvMode)

            if (get_imgStartAppRestartOnTvMode.equals(Constants.imgStartAppRestartOnTvMode)) {

                binding.textShowAppRestartTvMode.text = "Auto Restart When Crashed"

            } else {

                binding.textShowAppRestartTvMode.text = "Auto Restart When Crashed"

            }




            closeBs.setOnClickListener {


            }




        }


    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, WebActivity::class.java))
        finish()
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




}