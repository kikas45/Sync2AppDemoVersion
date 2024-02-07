package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.SavedHistoryListAdapter
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.UserViewModel
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding
import java.io.File

class TestScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScreenBinding

    private val mUserViewModel by viewModels<UserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



/*
            val adapter = SavedHistoryListAdapter(applicationContext, this)

            binding.apply {
                recyclerSavedDownload.adapter = adapter
                recyclerSavedDownload.layoutManager = LinearLayoutManager(applicationContext)
            }

            mUserViewModel.readAllData.observe(this, Observer { user ->
                adapter.setData(user)
            })

*/

    }


   /* @SuppressLint("SetJavaScriptEnabled")
    private fun loadOfflineWebviewPage() {
        try {

            binding.apply {
                binding.progressBar3.visibility = View.GONE
                val filename = "/index.html"
                val savedDownloadPath =
                    getSharedPreferences(Constants.SAVE_M_DOWNLOAD_PATH, MODE_PRIVATE)
                val getFolderClo = savedDownloadPath.getString("getFolderClo", "")
                val getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "")
                val Extracted = savedDownloadPath.getString("Extracted", "")
                val finalFolderPathDesired = "/$getFolderClo/$getFolderSubpath/$Extracted"
                val destinationFolder =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/" + finalFolderPathDesired
                val filePath = "file://$destinationFolder$filename"
                val myFile = File(destinationFolder, filename)
                if (myFile.exists()) {

                    webview.setWebViewClient(WebViewClient())
                    val webSettings: WebSettings = webview.getSettings()
                    webSettings.javaScriptEnabled = true
                    webSettings.allowFileAccess = true
                    webSettings.allowContentAccess = true
                    webSettings.domStorageEnabled = true
                    webSettings.mediaPlaybackRequiresUserGesture = false // Set this to false for autoplaying media
                    webSettings.javaScriptCanOpenWindowsAutomatically = true
                    webSettings.loadWithOverviewMode = true
                    webSettings.useWideViewPort = true
                    webSettings.loadsImagesAutomatically = true

                    webview.loadUrl(filePath)
                }
            }
        } catch (e: Exception) {
        }
    }*/


}