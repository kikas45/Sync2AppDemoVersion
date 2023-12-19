package remotex.com.remotewebview.additionalSettings

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import remotex.com.remotewebview.SettingsActivity
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityTvOrAppModeBinding
import java.io.File

class TvActivityOrAppMode : AppCompatActivity() {
    private lateinit var binding: ActivityTvOrAppModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvOrAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {



            val sharedBiometric: SharedPreferences = applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            textAppMode.setOnClickListener {
                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                finish()
                editor.putString(Constants.App_Mode, Constants.App_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
            }

            textTvMode.setOnClickListener {
                startActivity(Intent(applicationContext, ReSyncActivity::class.java))
                finish()
                editor.putString(Constants.TV_Mode, Constants.TV_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
            }


        }


    }


    override fun onResume() {
        super.onResume()

        val sharedBiometric: SharedPreferences = applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)

        val first_time_app_start = sharedBiometric.getString(Constants.FIRST_TIME_APP_START, "")

        if (first_time_app_start.equals(Constants.FIRST_TIME_APP_START)){
            startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
            finish()
        }

    }





/*    private fun loadOffkineWebviewPage() {
        binding.apply {
            val Syn2AppLive = "Syn2AppLive"
            val filename = "index.html"
            val unzipManual = "/CLO/DE_MO_2021000/Offline_app/"

            val folderToExtractTo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Syn2AppLive + unzipManual

            val filePath = "file://$folderToExtractTo$filename"

            val destinationFolder = File(folderToExtractTo)
            if (destinationFolder.exists()) {
                Log.d("PowellFilePath", "File Path: $filePath")

                myWebview.webViewClient = WebViewClient()
                myWebview.settings.javaScriptEnabled = true
                myWebview.settings.setSupportZoom(true)
                myWebview.settings.allowFileAccess = true
                myWebview.settings.allowContentAccess = true
                myWebview.loadUrl(filePath)
            } else {
                Toast.makeText(applicationContext, "No index,html file found", Toast.LENGTH_SHORT).show()
            }

        }
    }*/

}