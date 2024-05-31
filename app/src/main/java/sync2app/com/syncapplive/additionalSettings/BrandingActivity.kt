package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.additionalSettings.autostartAppOncrash.Methods
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityBrandingBinding
import java.io.File


class BrandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrandingBinding
    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()

        //add exception
        Methods.addExceptionHandler(this)


        val get_imgToggleImageBackground = sharedBiometric.getString(Constants.imgToggleImageBackground, "")
        val get_imageUseBranding = sharedBiometric.getString(Constants.imageUseBranding, "")
        if (get_imgToggleImageBackground.equals(Constants.imgToggleImageBackground) && get_imageUseBranding.equals(Constants.imageUseBranding) ){
            loadBackGroundImage()
        }




        // set toggle for branding which control the branding set up
        binding.apply {

            imageUseBranding.setOnCheckedChangeListener { compoundButton, isValued ->
                if (compoundButton.isChecked) {
                    val editor = sharedBiometric.edit()

                    editor.putString(Constants.imageUseBranding, Constants.imageUseBranding)
                    editor.apply()
                    binding.textUseBranding.text = "Use Branding"

                    val get_imgToggleImageBackground_n = sharedBiometric.getString(Constants.imgToggleImageBackground, "")
                    if (get_imgToggleImageBackground_n.equals(Constants.imgToggleImageBackground)){
                        loadBackGroundImage()
                    }

                } else {
                    val editor = sharedBiometric.edit()

                    editor.remove(Constants.imageUseBranding)
                    editor.apply()
                    binding.textUseBranding.text = "Do not use Branding"

                    Glide.with(applicationContext).load("").centerCrop().into(binding.backgroundImage)


                }
            }


            val get_imageUseBranding = sharedBiometric.getString(Constants.imageUseBranding, "")


            imageUseBranding.isChecked = get_imageUseBranding.equals(Constants.imageUseBranding)

            if (get_imageUseBranding.equals(Constants.imageUseBranding)) {

                binding.textUseBranding.text = "Use Branding"

            } else {

                binding.textUseBranding.text = "Do not use Branding"

            }



            closeBs.setOnClickListener {
                val intent = Intent(applicationContext, MaintenanceActivity::class.java)
                startActivity(intent)
                finish()
            }

        }




        // set on use branding image
        binding.apply {


            // Restart app on Tv or Mobile mode
            imgToggleImageBackground.setOnCheckedChangeListener { compoundButton, isValued ->
                if (compoundButton.isChecked) {
                    val editor = sharedBiometric.edit()

                    editor.putString(Constants.imgToggleImageBackground, Constants.imgToggleImageBackground)
                    editor.apply()
                    binding.textUseImageForBranding.text = "Use Image for Background"

                    val get_imageUseBranding = sharedBiometric.getString(Constants.imageUseBranding, "")
                    if (get_imageUseBranding.equals(Constants.imageUseBranding)){
                        loadBackGroundImage()
                    }

                } else {
                    val editor = sharedBiometric.edit()

                    editor.remove(Constants.imgToggleImageBackground)
                    editor.apply()
                    binding.textUseImageForBranding.text = "Do not use Image for Background"
                    Glide.with(applicationContext).load("").centerCrop().into(binding.backgroundImage)
                }
            }


            val get_iimgToggleImageBackground = sharedBiometric.getString(Constants.imgToggleImageBackground, "")


            imgToggleImageBackground.isChecked = get_iimgToggleImageBackground.equals(Constants.imgToggleImageBackground)

            if (get_iimgToggleImageBackground.equals(Constants.imgToggleImageBackground)) {

                binding.textUseImageForBranding.text = "Use Image Background"

            } else {

                binding.textUseImageForBranding.text = "Do not use Image Background"

            }


            closeBs.setOnClickListener {
                val intent = Intent(applicationContext, MaintenanceActivity::class.java)
                startActivity(intent)
                finish()
            }

        }




        /// set up Splash toggle for video or image Splash Screen
        binding.apply {


            // Restart app on Tv or Mobile mode
            imgToggleImageSplashOrVideoSplash.setOnCheckedChangeListener { compoundButton, isValued ->
                if (compoundButton.isChecked) {
                    val editor = sharedBiometric.edit()

                    editor.putString(Constants.imgToggleImageSplashOrVideoSplash, Constants.imgToggleImageSplashOrVideoSplash)
                    editor.apply()
                    binding.textUseImageOrVideoSplashScreen.text = "Use Video For Splash Screen"

                } else {
                    val editor = sharedBiometric.edit()

                    editor.remove(Constants.imgToggleImageSplashOrVideoSplash)
                    editor.apply()
                    binding.textUseImageOrVideoSplashScreen.text = "Use Image For Splash Screen"
                }
            }


            val get_imgStartAppRestartOnTvMode = sharedBiometric.getString(Constants.imgToggleImageSplashOrVideoSplash, "")


            imgToggleImageSplashOrVideoSplash.isChecked = get_imgStartAppRestartOnTvMode.equals(Constants.imgToggleImageSplashOrVideoSplash)

            if (get_imgStartAppRestartOnTvMode.equals(Constants.imgToggleImageSplashOrVideoSplash)) {

                binding.textUseImageOrVideoSplashScreen.text = "Use Video For Splash Screen"

            } else {

                binding.textUseImageOrVideoSplashScreen.text = "Use Image For Splash Screen"

            }


            closeBs.setOnClickListener {
                val intent = Intent(applicationContext, MaintenanceActivity::class.java)
                startActivity(intent)
                finish()
            }

        }




    }

    private fun loadBackGroundImage() {

        val fileTypes = "app_background.png"
        val getFolderClo = myDownloadClass.getString(Constants.getFolderClo, "").toString()
        val getFolderSubpath = myDownloadClass.getString(Constants.getFolderSubpath, "").toString()

        val pathFolder = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Constants.App + "/" + "Config"
        val folder =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/${Constants.Syn2AppLive}/" + pathFolder
        val file = File(folder, fileTypes)

        if (file.exists()) {
            Glide.with(this).load(file).centerCrop().into(binding.backgroundImage)

        }

    }




    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MaintenanceActivity::class.java))
        finish()
    }
}