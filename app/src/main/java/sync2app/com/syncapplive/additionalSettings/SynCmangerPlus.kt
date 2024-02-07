package sync2app.com.syncapplive.additionalSettings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivitySynCmangerPlusBinding

class SynCmangerPlus : AppCompatActivity() {
    private lateinit var binding: ActivitySynCmangerPlusBinding


    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynCmangerPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            closeBs.setOnClickListener {
                onBackPressed()
            }
        }

        initViewTooggle()
    }


    private fun initViewTooggle() {
        binding.apply {
            val imgLunchOnline = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "")
            imagSwtichEnableLaucngOnline.isChecked =
                imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)


            if (imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)) {
                textLunchOnline.setText("Launch offline")
            } else {
                textLunchOnline.setText("Launch online")
            }



            imagSwtichEnableLaucngOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")
                    editor.apply()
                    textLunchOnline.setText("Launch offline")

                } else {

                    editor.remove("imgAllowLunchFromOnline")
                    editor.apply()
                    textLunchOnline.setText("Launch online")

                }
            }


            // enable config

            val imgLCongigFile =
                sharedBiometric.getString(Constants.imagSwtichEnableConfigFileOnline, "")
            imagSwtichEnableConfigFileOnline.isChecked =
                imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)


            if (imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)) {
                textConfigfileOnline.setText("Config File Offline")
                editTextInputConfigUrl.visibility = View.VISIBLE
                divider27.visibility = View.VISIBLE
            } else {
                textConfigfileOnline.setText("Config File Online")
                editTextInputConfigUrl.visibility = View.GONE
                divider27.visibility = View.GONE
            }


            imagSwtichEnableConfigFileOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableConfigFileOnline,
                        "imagSwtichEnableConfigFileOnline"
                    )
                    editor.apply()
                    textConfigfileOnline.setText("Config File Offline")
                    editTextInputConfigUrl.hint = "Input your config offline url"

                    editTextInputConfigUrl.visibility = View.VISIBLE
                    divider27.visibility = View.VISIBLE

                } else {

                    editor.remove("imagSwtichEnableConfigFileOnline")
                    editor.apply()

                    textConfigfileOnline.setText("Config File Online")
                    editTextInputConfigUrl.hint = "Input your con online url"

                    editTextInputConfigUrl.visibility = View.GONE
                    divider27.visibility = View.GONE

                }
            }


            // enable satrt file for first synct

            val startFileFirstSync = sharedBiometric.getString(Constants.imgStartFileFirstSync, "")
            imgStartFileFirstSync.isChecked =
                startFileFirstSync.equals(Constants.imgStartFileFirstSync)


            if (startFileFirstSync.equals(Constants.imgStartFileFirstSync)) {
                textUseStartFile.setText("Use start file for first sync")
                editTextInputSYnCfirstStartUrl.hint = "Input url for first sync"
                editTextInputSYnCfirstStartUrl.visibility = View.VISIBLE
                divider28.visibility = View.VISIBLE
            } else {
                textUseStartFile.setText("Don't use start file for first sync")
                editTextInputSYnCfirstStartUrl.hint = "Input a different url "
                editTextInputSYnCfirstStartUrl.visibility = View.GONE
                divider28.visibility = View.GONE

            }


            imgStartFileFirstSync.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imgStartFileFirstSync, Constants.imgStartFileFirstSync
                    )
                    editor.apply()
                    textUseStartFile.setText("Use start file for first sync")
                    editTextInputSYnCfirstStartUrl.hint = "Input url for first sync"
                    editTextInputSYnCfirstStartUrl.visibility = View.VISIBLE
                    divider28.visibility = View.VISIBLE
                } else {

                    editor.remove(Constants.imgStartFileFirstSync)
                    editor.apply()

                    textUseStartFile.setText("Don't use start file for first sync")
                    // editTextInputSYnCfirstStartUrl.hint = "Input a different url "
                    editTextInputSYnCfirstStartUrl.visibility = View.GONE
                    divider28.visibility = View.GONE


                }
            }


            // enable satrt file for first synct

            val textSynfromApiZip222 =
                sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")
            imagSwtichEnableSyncFromAPI.isChecked =
                textSynfromApiZip222.equals(Constants.imagSwtichEnableSyncFromAPI)


            if (textSynfromApiZip222.equals(Constants.imagSwtichEnableSyncFromAPI)) {
                textSynfromApiZip.setText("Use ZIP Sync")
                textDownloadZipSyncOrApiSync.setText("Download ZIP Sync")
            } else {
                textSynfromApiZip.setText("Use API Sync")
                textDownloadZipSyncOrApiSync.setText("Download ZIP Sync")
            }


            imagSwtichEnableSyncFromAPI.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableSyncFromAPI, Constants.imagSwtichEnableSyncFromAPI
                    )
                    editor.apply()
                    textSynfromApiZip.setText("Use ZIP Sync")
                    editTextInputSynUrlZip.setHint("Input url  ZIP Sync")
                    textDownloadZipSyncOrApiSync.setText("Download ZIP Sync")
                } else {

                    editor.remove(Constants.imagSwtichEnableSyncFromAPI)
                    editor.apply()

                    textSynfromApiZip.setText("Use API Sync")
                    textDownloadZipSyncOrApiSync.setText("Download API Sync")
                    editTextInputSynUrlZip.setHint("Input url  API Sync")


                }
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.decrementRunningActivities()

    }

    override fun onStart() {
        super.onStart()
        MyApplication.incrementRunningActivities()
    }



}