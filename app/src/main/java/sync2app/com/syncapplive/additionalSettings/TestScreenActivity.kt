package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sync2app.com.syncapplive.additionalSettings.OnFileChange.Retro_On_Change
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding

class TestScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScreenBinding

    private var countdownTimerServerUpdater: CountDownTimer? = null

    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnRequest.setOnClickListener {


            }

            val currentTime = myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
            val severTime = myDownloadClass.getString(Constants.SeverTimeSaved, "") + ""

            countdownTimerServerUpdater?.cancel()
            attemptRequestAgain(1)

            if (currentTime.isEmpty() || severTime.isEmpty()) {
                getServerTimeFromJson()
                showToastMessage("First load")
            }

        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData() {

        val editor = myDownloadClass.edit()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Retro_On_Change.api.getAppConfig()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        showToastMessage("fetch data")
                        val getvalue = response.body()?.last_updated.toString()

                        binding.textSystem.text = "CV: $getvalue"


                        editor.putString(Constants.CurrentServerTime, getvalue)
                        editor.apply()


                        val currentTime = myDownloadClass.getString(Constants.CurrentServerTime, "") + ""
                        val severTime = myDownloadClass.getString(Constants.SeverTimeSaved, "") + ""

                        if (currentTime == severTime){
                            showToastMessage("This same ")

                        }else{
                            showToastMessage("Different")
                            getServerTimeFromJson()
                        }

                    } else {
                        showToastMessage("bad request")
                    }


                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")
                }
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getServerTimeFromJson() {

        val editor = myDownloadClass.edit()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Retro_On_Change.api.getAppConfig()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        showToastMessage("Server time")
                        val getvalue = response.body()?.last_updated.toString()



                        editor.putString(Constants.SeverTimeSaved, getvalue)
                        editor.putString(Constants.CurrentServerTime, getvalue)
                        editor.apply()

                        binding.textDisplay.text = "SV: $getvalue"
                        binding.textSystem.text = "CV: $getvalue"


                    } else {
                        showToastMessage("bad request")
                    }


                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")
                }
            }
        }
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun attemptRequestAgain(minutes: Long) {
        val milliseconds = minutes * 25 * 1000 // Convert minutes to


        countdownTimerServerUpdater = object : CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                try {
                    countdownTimerServerUpdater?.cancel()
                    attemptRequestAgain(minutes)

                    fetchData()

                } catch (ignored: java.lang.Exception) {
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                try {


                    val totalSecondsRemaining = millisUntilFinished / 1000
                    var minutesUntilFinished = totalSecondsRemaining / 60
                    var remainingSeconds = totalSecondsRemaining % 60

                    // Adjusting minutes if seconds are in the range of 0-59
                    if (remainingSeconds == 0L && minutesUntilFinished > 0) {
                        minutesUntilFinished--
                        remainingSeconds = 59
                    }
                    val displayText =
                        String.format("CD: %d:%02d", minutesUntilFinished, remainingSeconds)
                    //   showToastMessage(displayText)
                    binding.textCountDown.text = "Sever time $displayText"

                } catch (ignored: java.lang.Exception) {
                }
            }
        }
        countdownTimerServerUpdater?.start()
    }


}