package sync2app.com.syncapplive.QrPages

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivitySmsactivityBinding


class SMSActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsactivityBinding

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber: String = sharedBiometric.getString("phoneNumber", "").toString()
        val message: String = sharedBiometric.getString("message", "").toString()


        binding.textSendMySms.setOnClickListener {
            sendSMS(phoneNumber, message)
        }


        binding.apply {
            textPgoneNumbers.text = "To: $phoneNumber"
            textDisplayText.text = message
        }


        binding.textCopySms.setOnClickListener {
            copyWifiPasswordRawData(message)
        }



        binding.closeBs.setOnClickListener {
            endMyActivity()
        }


        binding.textDoNothing.setOnClickListener {
            endMyActivity()
        }

    }




    private fun sendSMS(phoneNumber: String?, message: String?) {
        val smsIntent = Intent(Intent.ACTION_SENDTO)
        smsIntent.data = Uri.parse("smsto:" + Uri.encode(phoneNumber))
        smsIntent.putExtra("sms_body", message)
        try {
            startActivity(smsIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show()
        }
    }



    private fun copyWifiPasswordRawData(messageState: String) {
        val clipboard = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = messageState
    }


    override fun onBackPressed() {
        endMyActivity()
    }

    private fun endMyActivity() {
        startActivity(Intent(applicationContext, WebActivity::class.java))
        finish()
        val editor = sharedBiometric.edit()
        editor.remove("phoneNumber")
        editor.remove("message")
        editor.apply()
    }

}