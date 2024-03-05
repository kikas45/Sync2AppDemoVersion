package sync2app.com.syncapplive.QrPages

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
import sync2app.com.syncapplive.databinding.ActivityPhoneBinding

class PhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneBinding

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber: String = sharedBiometric.getString("phoneNumber", "").toString()

        binding.textBodyMessage.text = phoneNumber


        binding.closeBs.setOnClickListener {
            endMyActivity()
        }


        binding.textDoNothing.setOnClickListener {
            endMyActivity()
        }

        binding.textCallNumber.setOnClickListener {
            makeCall(phoneNumber)
        }



    }

  private  fun makeCall(phoneNumber: String?) {


        // Create intent to dial the phone number
        val callIntent = Intent(Intent.ACTION_DIAL)

        // Set the phone number in the data field of the intent
        callIntent.data = Uri.parse("tel:" + Uri.encode(phoneNumber))
        try {
            // Start the activity to initiate the call
            startActivity(callIntent)
            finish()
        } catch (e: ActivityNotFoundException) {
            // Handle case where no dialer app is available
            Toast.makeText(this, "No dialer app found", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        endMyActivity()
    }

    private fun endMyActivity() {
        startActivity(Intent(applicationContext, WebActivity::class.java))
        finish()
        val editor = sharedBiometric.edit()
        editor.remove("phoneNumber")
        editor.apply()
    }
}