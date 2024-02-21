package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkStoragePermission
import sync2app.com.syncapplive.additionalSettings.urlchecks.requestStoragePermission
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityTvOrAppModeBinding
import sync2app.com.syncapplive.databinding.CustomGrantAccessPageBinding
import sync2app.com.syncapplive.databinding.CustomSelectLauncOrOfflinePopLayoutBinding
import java.io.File
import java.util.Objects

class TvActivityOrAppMode : AppCompatActivity() {
    private lateinit var binding: ActivityTvOrAppModeBinding
    private var hasPermission = false
    private var isReady = false

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,

            )
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
        )
    }


    private var navigateTVMode = false;
    private var navigateAppMolde = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvOrAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SIMPLE PASSWORD



        @SuppressLint("CommitPrefEdits") val simpleSavedPassword =
            getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, MODE_PRIVATE)

        val CheckForPassword = simpleSavedPassword.getString(Constants.onCreatePasswordSaved, "")

        if (CheckForPassword!!.isEmpty()) {
            val editor = simpleSavedPassword.edit()
            editor.putString(Constants.onCreatePasswordSaved, "onCreatePasswordSaved")
            editor.putString(Constants.simpleSavedPassword, "1234")
            editor.apply()
        }

        // SIMPLE PASSWORD


        val sharedBiometric: SharedPreferences =
            applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)

        val get_useOfflineOrOnline =
            sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "")
        val get_TV_or_App_Mode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "")


        val editor = sharedBiometric.edit()
        if (get_useOfflineOrOnline.equals(Constants.imgAllowLunchFromOnline) || get_TV_or_App_Mode.equals(
                Constants.TV_Mode
            )
        ) {
            startActivity(Intent(applicationContext, WebActivity::class.java))
            editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
            editor.apply()
            finish()
        }



        binding.apply {


            textAppMode.setOnClickListener {
                navigateAppMolde = true
                if (checkMultiplePermission()) {
                    doOperation()
                }


            }

            textTvMode.setOnClickListener {
                navigateTVMode = true
                if (checkMultiplePermission()) {
                    doOperation()
                }

            }

        }


    }


    private fun doOperation() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (hasPermission == false){
                showPop_For_Grant_Permsiion()
            }
        }else{
            isReadToMoveForKower()
        }
    }

    private fun isReadToMoveForKower() {
        binding.apply {
            val sharedBiometric: SharedPreferences =
                applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            if (navigateAppMolde == true ) {

                val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")


            }


            if (navigateTVMode == true ) {

                val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

                //  startActivity(Intent(applicationContext, ReSyncActivity::class.java))
                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.TV_Mode)
                editor.putString(Constants.CALL_RE_SYNC_MANGER, Constants.CALL_RE_SYNC_MANGER)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")

            }

        }
    }




    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    // here all permission granted successfully
                    doOperation()
                    isReady = true
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }

                    if (someDenied) {
                        showPermissionDeniedDialog()

                    } else {
                        showPermissionDeniedDialog()
                    }


                }
            }
        }
    }


    private fun showPermissionDeniedDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Permission Required")
        builder.setMessage("Please grant the required permissions in the app settings to proceed.")
        builder.setPositiveButton("Go to Settings") { dialog: DialogInterface?, which: Int ->
            openAppSettings()
            dialog?.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
            showToastMessage("Permission Denied!")
        }
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    override fun onResume() {
        super.onResume()

        val sharedBiometric: SharedPreferences =
            applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)

        val first_time_app_start = sharedBiometric.getString(Constants.FIRST_TIME_APP_START, "")

        if (first_time_app_start.equals(Constants.FIRST_TIME_APP_START)) {
            startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
            finish()
        }

        hasPermission = checkStoragePermission(this)
        if (hasPermission) {
            isReadToMove()

        }


    }

    private fun isReadToMove() {
        binding.apply {
            val sharedBiometric: SharedPreferences =
                applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            if (navigateAppMolde == true &&  isReady == true) {

                val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")


            }


            if (navigateTVMode == true &&  isReady == true) {

                val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

              //  startActivity(Intent(applicationContext, ReSyncActivity::class.java))
                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.TV_Mode)
                editor.putString(Constants.CALL_RE_SYNC_MANGER, Constants.CALL_RE_SYNC_MANGER)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")

            }

        }
    }


    @SuppressLint("MissingInflatedId")
    private fun showPop_For_Grant_Permsiion() {
        val bindingCM: CustomGrantAccessPageBinding =
            CustomGrantAccessPageBinding.inflate(
                layoutInflater
            )
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingCM.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val permissionButton: TextView = bindingCM.permissionButton


        permissionButton.setOnClickListener {
            requestStoragePermission(this@TvActivityOrAppMode)
            alertDialog.dismiss()

        }

        alertDialog.show()
    }


    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        } else if (file.isDirectory) {
            for (subFile in Objects.requireNonNull(file.listFiles())) {
                if (!delete(subFile)) return false
            }
            return file.delete()
        }
        return false
    }



}