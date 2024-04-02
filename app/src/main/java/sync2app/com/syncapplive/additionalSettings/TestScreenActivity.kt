package sync2app.com.syncapplive.additionalSettings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.ApiUrls.ApiUrlViewModel
import sync2app.com.syncapplive.additionalSettings.ApiUrls.DomainUrl
import sync2app.com.syncapplive.additionalSettings.ApiUrls.SavedApiAdapter
import sync2app.com.syncapplive.additionalSettings.OnFileChange.Retro_On_Change
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding
import java.util.Calendar

class TestScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScreenBinding


    var hour = 0
    var min = 0


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

            val get_tMaster: String = simple_saved_passowrd.getString(Constants.get_editTextMaster, "").toString()
            val get_UserID: String = simple_saved_passowrd.getString(Constants.get_UserID, "").toString()
            val get_LicenseKey: String = simple_saved_passowrd.getString(Constants.get_LicenseKey, "").toString()

            val dynamicPart = "$get_UserID/$get_LicenseKey/PTime/"
            fetchData(get_tMaster, dynamicPart)

        }


    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


/*
    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData(Pathurl:String) {


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Retro_On_Change.api.getAppConfig(Pathurl)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        //    showToastMessage("fetch data")
                        val getvalue = response.body()?.last_updated.toString()

                        binding.textDisplayText.text = getvalue.toString()


                    } else {
                        showToastMessage("bad request")
                        binding.textDisplayText.text = "bad request"
                    }


                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")
                    binding.textDisplayText.text = "HTTP Exception: ${e.message()}"

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")

                    binding.textDisplayText.text = "Error: ${e.message}"

                    showToastMessage("No internet Connection")


                }
            }
        }
    }

*/


    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData(baseUrl: String, dynamicPart: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val api = Retro_On_Change.create(baseUrl)
                val response = api.getAppConfig(dynamicPart)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val getvalue = response.body()?.last_updated.toString()
                        binding.textDisplayText.text = getvalue
                    } else {
                        showToastMessage("bad request")
                        binding.textDisplayText.text = "bad request"
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")
                    binding.textDisplayText.text = "HTTP Exception: ${e.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")
                    binding.textDisplayText.text = "Error: ${e.message}"
                    showToastMessage("No internet Connection")
                }
            }
        }
    }

}