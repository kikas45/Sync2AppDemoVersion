package sync2app.com.syncapplive.additionalSettings


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesApi
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding

class TestScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScreenBinding


    private val mUserViewModel by viewModels<FilesViewModel>()


    private val simple_saved_passowrd: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SIMPLE_SAVED_PASSWORD,
            Context.MODE_PRIVATE
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnMakeCall.setOnClickListener {
            startActivity(Intent(applicationContext, MyTestDownloadAPI::class.java))
        }



        binding.textDisplayText.setOnClickListener {

            val getValues = binding.editTextText3.text.toString().trim()
            mUserViewModel.readAllData.observe(this@TestScreenActivity, Observer { user ->
                  binding.textDisplayText.text = user.get(getValues.toInt()).SN  + "  | " + user.get(getValues.toInt()).FolderName + "  | " + user.get(getValues.toInt()).FileName + "  | " + user.get(getValues.toInt()).Status

            })
        }




    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


}