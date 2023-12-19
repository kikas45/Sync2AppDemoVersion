package remotex.com.remotewebview.additionalSettings

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import remotex.com.remotewebview.databinding.ActivityTvOrAppModeBinding
import java.io.File

class TvActivityOrAppMode : AppCompatActivity() {
    private lateinit var binding: ActivityTvOrAppModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvOrAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            textAppMode.setOnClickListener {
                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
            }


            textTvMode.setOnClickListener {
                startActivity(Intent(applicationContext, ReSyncActivity::class.java))
            }


        //    loadOffkineWebviewPage()

        }
    }

/*    private fun loadOffkineWebviewPage() {
        binding.apply {
            val Syn2AppLive = "Syn2AppLive"
            val filename = "/index.html"
            val unzipManual = "/CLO/DE_MO_2021000/Offline_app/"

            val folderToExtractTo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Syn2AppLive + unzipManual

            val filePath = "file://$folderToExtractTo$filename"

            val destinationFolder = File(folderToExtractTo)
            if (destinationFolder.exists()) {
                Log.d("FilePath", "File Path: $filePath")

                myWebview.webViewClient = WebViewClient()
                myWebview.settings.javaScriptEnabled = true
                myWebview.settings.setSupportZoom(true)
                myWebview.loadUrl(filePath)
                myWebview.loadUrl(filePath)
            } else {
                Toast.makeText(applicationContext, "No", Toast.LENGTH_SHORT).show()
            }

        }
    }*/
}