package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.UserManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.SettingsActivity
import sync2app.com.syncapplive.additionalSettings.devicelock.MyDeviceAdminReceiver
import sync2app.com.syncapplive.additionalSettings.scanutil.CustomShortcuts
import sync2app.com.syncapplive.additionalSettings.scanutil.DefaultCustomShortCut
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityAdditionalSettingsBinding
import sync2app.com.syncapplive.databinding.ActivityAppAdminBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AdditionalSettingsActivity : AppCompatActivity() {


    private fun isAdmin() = mDevicePolicyManager.isDeviceOwnerApp(packageName)

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager

    companion object {
        const val LOCK_ACTIVITY_KEY = "pl.mrugacz95.kiosk.MainActivity"
    }


    private lateinit var binding: ActivityAppAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()


        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        mDevicePolicyManager =
            getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager


        // we will handle this call back when we enable device owner later

        if (Build.VERSION.SDK_INT >= 30) {
            // not  device owner
        } else {
            mDevicePolicyManager.removeActiveAdmin(mAdminComponentName);
        }

        val isAdmin = isAdmin()

        if (isAdmin) {
            //   Snackbar.make(binding.content, "device_owner", Snackbar.LENGTH_SHORT).show()
        } else {
            //    Snackbar.make(binding.content, "not_device_owner", Snackbar.LENGTH_SHORT).show()

        }


        binding.textTitle.setOnClickListener {
            setKioskPolicies(true, isAdmin)
        }





        binding.apply {


            textSyncManager.setOnClickListener {

                val editor = sharedBiometric.edit()
                val intent = Intent(applicationContext, ReSyncActivity::class.java)
                startActivity(intent)
                finish()

                editor.putString(Constants.SAVE_NAVIGATION, Constants.AdditionNalPage)
                editor.apply()


            }






            textSystemInfo.setOnClickListener {
                val intent = Intent(applicationContext, SystemInfoActivity::class.java)
                startActivity(intent)
            }




            textDeviceSettings2.setOnClickListener {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)

            }





            textVolume.setOnClickListener {
                val audioManager =
                    applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

                // Adjust the volume (you can change AudioManager.STREAM_MUSIC to other types)
                audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
            }



            textAppSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }

            textWifiSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }


            textPassword.setOnClickListener {
                val intent = Intent(applicationContext, PasswordActivity::class.java)
                startActivity(intent)
            }



            textMaintencePage.setOnClickListener {
                val intent = Intent(applicationContext, MaintenanceActivity::class.java)
                startActivity(intent)
                finish()

                val editor = sharedBiometric.edit()
                editor.putString(Constants.SAVE_NAVIGATION, Constants.AdditionNalPage)
                editor.apply()

            }





            textShareApp.setOnClickListener {
                try {
                    shareMyApk()
                } catch (_: Exception) {
                }
            }


            textManageShortCuts.setOnClickListener {
                val shortCutBottomFragment = ShortCutBottomFragment()
                shortCutBottomFragment.show(supportFragmentManager, shortCutBottomFragment.tag)

            }


            closeBs.setOnClickListener {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                finish()

            }


        }


        initView()

    }


    private fun initView() {


        binding.apply {


            val editor = sharedBiometric.edit()

            // here will listen to every evenst
            val imagelock = sharedBiometric.getString("imgEnableLockScreen", "")
            val imagAutoBoot = sharedBiometric.getString("imgEnableAutoBoot", "")
            val imgFingerPrint = sharedBiometric.getString(Constants.imgAllowFingerPrint, "")
            // val imgLunchOnline = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "")


            imgEnableLockScreen.isChecked = imagelock.equals("imgEnableLockScreen")
            imgEnableAutoBoot.isChecked = imagAutoBoot.equals("imgEnableAutoBoot")
            imgAllowFingerPrint.isChecked = imgFingerPrint.equals(Constants.imgAllowFingerPrint)


            /// enable the lockscreen
            /// enable the lockscreen
            imgEnableLockScreen.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {
                    editor.putString("imgEnableLockScreen", "imgEnableLockScreen")
                    editor.apply()

                    setKioskPolicies(true, isAdmin())

                } else {

                    editor.remove("imgEnableLockScreen")
                    editor.apply()

                    setKioskPolicies(false, isAdmin())
                    val intent =
                        Intent(applicationContext, AdditionalSettingsActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                    intent.putExtra(LOCK_ACTIVITY_KEY, false)
                    startActivity(intent)

                }
            }

            /// enable the Auto Boot
            imgEnableAutoBoot.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {
                    editor.putString("imgEnableSafeMood", "imgEnableSafeMood")
                    editor.apply()
                    showSnackBar("App will auto-start on reboot")
                } else {

                    // stop lock screen
                    editor.remove("imgEnableSafeMood")
                    editor.apply()
                    showSnackBar("You have turned off the feature")
                }
            }



            imgAllowFingerPrint.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imgAllowFingerPrint, "imgAllowFingerPrint")
                    editor.apply()
                    showSnackBar("Fingerprint sign-in enabled")
                } else {

                    // stop lock screen
                    editor.remove(Constants.imgAllowFingerPrint)
                    editor.apply()
                    showSnackBar("Fingerprint sign-in disabled")
                }
            }


        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.content, message, Snackbar.LENGTH_LONG).show()

    }


    private fun shareMyApk() {
        try {
            val nameOfApk = "Syn2App.apk"
            val baseApkLocation =
                applicationContext.packageManager.getApplicationInfo(
                    applicationContext.packageName,
                    PackageManager.GET_META_DATA
                ).sourceDir

            val baseApk = File(baseApkLocation)

            val path = Environment.getExternalStorageDirectory()
                .toString() + "/Download/Syn2AppLive/APK/"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            // Copy the .apk file to downloads directory
            val destination = File(
                path, nameOfApk
            )
            if (destination.exists()) {
                destination.delete()
            }

            destination.createNewFile()
            val input = FileInputStream(baseApk)
            val output = FileOutputStream(destination)
            val buffer = ByteArray(1024)
            var length: Int = input.read(buffer)
            while (length > 0) {
                output.write(buffer, 0, length)
                length = input.read(buffer)
            }
            output.flush()
            output.close()
            input.close()


            val directoryPath =
                Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/APK/$nameOfApk"

            val nameOfpackage = this.packageName

            val fileUri = FileProvider.getUriForFile(
                applicationContext,
                "$nameOfpackage.fileprovider",
                File(directoryPath)
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/vnd.android.package-archive"
                putExtra(Intent.EXTRA_STREAM, fileUri)
            }

            startActivity(Intent.createChooser(shareIntent, "Share APK using"))


        } catch (e: Exception) {
            Log.d("TAG", "shareMyApk: \"Failed To Share The App${e.message}")
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {

        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.decrementRunningActivities()

    }


    private fun setKioskPolicies(enable: Boolean, isAdmin: Boolean) {
        if (isAdmin) {
            setRestrictions(enable)
            enableStayOnWhilePluggedIn(enable)
            setUpdatePolicy(enable)
            setAsHomeApp(enable)
            setKeyGuardEnabled(enable)
        }
        setLockTask(enable, isAdmin)
        setImmersiveMode(enable)
    }

    // region restrictions
    private fun setRestrictions(disallow: Boolean) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow)
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow)
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow)
        mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, disallow)
    }

    private fun setUserRestriction(restriction: String, disallow: Boolean) = if (disallow) {
        mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction)
    } else {
        mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction)
    }
    // endregion

    private fun enableStayOnWhilePluggedIn(active: Boolean) = if (active) {
        mDevicePolicyManager.setGlobalSetting(
            mAdminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            (BatteryManager.BATTERY_PLUGGED_AC
                    or BatteryManager.BATTERY_PLUGGED_USB
                    or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
        )
    } else {
        mDevicePolicyManager.setGlobalSetting(
            mAdminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            "0"
        )
    }

    private fun setLockTask(start: Boolean, isAdmin: Boolean) {
        if (isAdmin) {
            mDevicePolicyManager.setLockTaskPackages(
                mAdminComponentName, if (start) arrayOf(packageName) else arrayOf()
            )
        }
        if (start) {
            startLockTask()
        } else {
            stopLockTask()
        }
    }

    private fun setUpdatePolicy(enable: Boolean) {
        if (enable) {
            mDevicePolicyManager.setSystemUpdatePolicy(
                mAdminComponentName,
                SystemUpdatePolicy.createWindowedInstallPolicy(60, 120)
            )
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName, null)
        }
    }

    private fun setAsHomeApp(enable: Boolean) {
        if (enable) {
            val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            mDevicePolicyManager.addPersistentPreferredActivity(
                mAdminComponentName,
                intentFilter,
                ComponentName(packageName, AdditionalSettingsActivity::class.java.name)
            )
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                mAdminComponentName, packageName
            )
        }
    }

    private fun setKeyGuardEnabled(enable: Boolean) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable)
    }

    @Suppress("DEPRECATION")
    private fun setImmersiveMode(enable: Boolean) {
        if (enable) {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.decorView.systemUiVisibility = flags
        } else {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.decorView.systemUiVisibility = flags
        }
    }


}