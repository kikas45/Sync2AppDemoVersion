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
import sync2app.com.syncapplive.databinding.ActivityEmailBinding

class EmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailBinding


    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val emailTo: String = sharedBiometric.getString("emailTo", "").toString()
        val emailSubject: String = sharedBiometric.getString("emailSubject", "").toString()
        val emailBody: String = sharedBiometric.getString("emailBody", "").toString()


        binding.textSendMySms.setOnClickListener {
            sendEmail(emailTo, emailSubject, emailBody)
        }


        binding.apply {
            textToemail.text = "To:      $emailTo"
            textSubject.text = "Subject: $emailSubject"
            textBodyMessage.text = "Body:    $emailBody"
        }



        binding.closeBs.setOnClickListener {
            endMyActivity()
        }


        binding.textDoNothing.setOnClickListener {
            endMyActivity()
        }


       binding.closeBs.setOnClickListener {
            endMyActivity()
        }



    }



    fun sendEmail(emailTo: String?, emailSubject: String?, emailBody: String?) {
        val uriText = "mailto:" + Uri.encode(emailTo) +
                "?subject=" + Uri.encode(emailSubject) +
                "&body=" + Uri.encode(emailBody)
        val uri = Uri.parse(uriText)

        // Intent for sending email
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = uri
        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            // Handle case where no email app is available
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        endMyActivity()
    }

    private fun endMyActivity() {
        startActivity(Intent(applicationContext, WebActivity::class.java))
        finish()
        val editor = sharedBiometric.edit()
        editor.remove("emailTo")
        editor.remove("emailSubject")
        editor.remove("emailBody")
        editor.apply()
    }

}