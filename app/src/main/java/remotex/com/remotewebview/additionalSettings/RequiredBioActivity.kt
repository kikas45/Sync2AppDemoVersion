package remotex.com.remotewebview.additionalSettings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import remotex.com.remotewebview.Splash
import remotex.com.remotewebview.additionalSettings.utils.Constants

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

import remotex.com.remotewebview.databinding.ActivityRequiredBioBinding


class RequiredBioActivity : AppCompatActivity() {

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    private lateinit var binding: ActivityRequiredBioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequiredBioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                displayMessage("No biometric credentials are enrolled")
        }

        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    displayMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    displayMessage("Authentication succeeded!")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    displayMessage("Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        binding.imageView3.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun displayMessage(message: String) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (message.equals("Authentication succeeded!")) {
            val intent = Intent(this, Splash::class.java)
            startActivity(intent)
            finish()
        }

    }


    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        val getValue = sharedBiometric.getString(Constants.imgAllowFingerPrint, "")
        if (!getValue.equals(Constants.imgAllowFingerPrint)) {
            startActivity(Intent(applicationContext, Splash::class.java))
            finish()
        }else{
          //  biometricPrompt.authenticate(promptInfo)
        }
    }


}