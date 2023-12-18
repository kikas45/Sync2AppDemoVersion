package remotex.com.remotewebview.additionalSettings.DownloadsArray.list

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import remotex.com.remotewebview.additionalSettings.DownloadsArray.model.DownloadModel
import remotex.com.remotewebview.additionalSettings.DownloadsArray.viewmodel.DownloadViewModel
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityDownLoadPageBinding

class DownLoadPage : AppCompatActivity(), DownloadListAdapter.OnItemClickListener,
    DownloadListAdapter.OnItemLongClickListener {

    private val mUserViewModel by viewModels<DownloadViewModel>()

    private lateinit var binding: ActivityDownLoadPageBinding
  //  private val baseUrl = "https://firebasestorage.googleapis.com/v0/b/vector-news-b5fcf.appspot.com/o/testAPKs%2FMyZip.zip?alt=media&token=5f890c03-d2d5-4f97-95c7-e39c8dc49c57"

    // val baseUrl = "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/Api/update.csv"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownLoadPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val baseUrl = intent.getStringExtra("baseUrl")
        val getToatlFilePath = intent.getStringExtra("getToatlFilePath")

        if (baseUrl !=null){
            insertDataToDatabase(baseUrl.toString(), getToatlFilePath.toString() )
        }





        DisplaySearchHistory()

        binding.apply {
            closeBs.setOnClickListener {
                onBackPressed()
            }

            textContinuPassword2.setOnClickListener {
              mUserViewModel.deleteAllUsers()
            }

            textContinuPassword222.setOnClickListener {
             showToastMessage("Attempting reconnection")
            }


            textContinuPassword222222.setOnClickListener {
             showToastMessage("logic not yet ready")
            }


        }
    }

    private fun insertDataToDatabase(baseUrl: String, getToatlFilePath:String) {

        val lastString = baseUrl.substringAfterLast("/")
        val fileNameWithoutExtension = lastString.substringBeforeLast(".")


       // val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
      //  val request = DownloadManager.Request(Uri.parse(baseUrl))
          //  .setTitle("Your download title")
         //   .setDescription("Downloading...")

       // val downloadId = downloadManager.enqueue(request)

        val sharedPreferences = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE)
        val download_ref = sharedPreferences.getLong(Constants.downloadKey, -15)

        val user = DownloadModel(url = baseUrl, status = "", title = fileNameWithoutExtension, pathName = getToatlFilePath,  downloadId = download_ref)
        mUserViewModel.addUser(user)
    }




    private fun DisplaySearchHistory() {
        val adapter = DownloadListAdapter(this, this, mUserViewModel, this)

        binding.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        }

        mUserViewModel.readAllData.observe(this, Observer { user ->
            adapter.setData(user)
        })
    }



    override fun onItemClicked(photo: DownloadModel) {

        showToastMessage("Retrying..")
    }



    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }




    override fun onItemLongClicked(photo: DownloadModel) {
        mUserViewModel.deleteUser(photo)
    }

}