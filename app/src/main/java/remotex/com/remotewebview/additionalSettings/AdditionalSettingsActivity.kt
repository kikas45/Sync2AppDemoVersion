package remotex.com.remotewebview.additionalSettings
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.Uri
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import remotex.com.remotewebview.R
import remotex.com.remotewebview.additionalSettings.scanutil.CustomShortcuts
import remotex.com.remotewebview.additionalSettings.scanutil.DefaultCustomShortCut
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityAdditionalSettingsBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AdditionalSettingsActivity : AppCompatActivity() {

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_IMAGES

        )
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }



    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val Admin_Password = "1234"
    private val App_Name = "Syn2app"

    // setting for shortCut icons
    private val SELECT_PICTURE = 200
    private var isImagePicked = false
    var imagePicked: Uri? = null
    private lateinit var custImageView: ImageView
    private lateinit var customimageRadipoButton: RadioButton



    private lateinit var binding: ActivityAdditionalSettingsBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {


            textAllowLunchOffline.setOnClickListener {
                val intent = Intent(applicationContext, ReSyncActivity::class.java)
                startActivity(intent)
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




            textShareApp.setOnClickListener {
                if (checkMultiplePermission()) {
                    doOperation()
                }
            }

        }


        initView()


        binding.apply {

            closeBs.setOnClickListener {
                onBackPressed()
            }
            textManageShortCuts.setOnClickListener {

                val shortCutBottomFragment = ShortCutBottomFragment()
                shortCutBottomFragment.show(supportFragmentManager, shortCutBottomFragment.tag)


            }


        }

    }




    /// class for creating short cut icons
    /// class for creating short cut icons
    /// class for creating short cut icons


    @RequiresApi(Build.VERSION_CODES.O)
    private fun shortcutPin(context: Context, shortcut_id: String, requestCode: Int) {

        val shortcutManager = getSystemService(ShortcutManager::class.java)

        if (shortcutManager!!.isRequestPinShortcutSupported) {
            val pinShortcutInfo =
                ShortcutInfo.Builder(context, shortcut_id).build()

            val pinnedShortcutCallbackIntent =
                shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            val successCallback = PendingIntent.getBroadcast(
                context, /* request code */ requestCode,
                pinnedShortcutCallbackIntent, /* flags */ PendingIntent.FLAG_MUTABLE
            )

            shortcutManager.requestPinShortcut(
                pinShortcutInfo,
                successCallback.intentSender
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    private fun showMyAlerDialog() {

        var defaultradio = false
        var customradio = false

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.custom_alert_download, null)

        builder.setView(dialogView)

        val alertDialog = builder.create()

        // Set the background of the AlertDialog to be transparent
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textLogin = dialogView.findViewById<View>(R.id.textLogin) as TextView
        val editTextText = dialogView.findViewById<View>(R.id.editTextText) as EditText
        custImageView = dialogView.findViewById<View>(R.id.custImageView) as ImageView
        val defaultImageFaltImage =
            dialogView.findViewById<View>(R.id.defaultImageFaltImage) as ImageView
        val defaultImageRadioButton =
            dialogView.findViewById<View>(R.id.defaultImageRadioButton) as RadioButton
        customimageRadipoButton =
            dialogView.findViewById<View>(R.id.customimageRadipoButton) as RadioButton

        custImageView.setOnClickListener {
            imageChooser()
        }


        customimageRadipoButton.setOnClickListener {
            imageChooser()
        }

        defaultImageFaltImage.setOnClickListener {
            defaultImageRadioButton.isChecked = true
            customimageRadipoButton.isChecked = false
        }



        textLogin.setOnClickListener {
            hideKeyBoard(editTextText)

            val getEditString = editTextText.text.toString().trim()

            if (defaultradio) {
                if (getEditString.isNotEmpty()) {
                    //  val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePicked)
                    if (Build.VERSION.SDK_INT >= 25) {
                        DefaultCustomShortCut.setUp(applicationContext, getEditString)
                    }
                    if (Build.VERSION.SDK_INT >= 28) {
                        shortcutPin(applicationContext, Constants.shortcut_website_id, 0)
                    }
                    alertDialog.dismiss()
                } else {
                    showToastMessage("Add name")
                }
            } else {
                //  showToastMessage("Image and name required")
            }



            if (customradio) {
                if (getEditString.isNotEmpty() && isImagePicked) {
                    if (Build.VERSION.SDK_INT >= 25) {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePicked)
                        CustomShortcuts.setUp(applicationContext, getEditString, bitmap)
                    }
                    if (Build.VERSION.SDK_INT >= 28) {
                        shortcutPin(applicationContext, Constants.shortcut_messages_id, 1)
                    }
                    alertDialog.dismiss()
                } else {
                    showToastMessage("Image and name required")
                }
            } else {
                // showToastMessage("Image and name required")
            }
        }


        alertDialog.show()
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }

    private fun hideKeyBoard(editText: EditText) {
        try {
            editText.clearFocus()
            val imm =
                applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (ignored: java.lang.Exception) {
        }
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    custImageView.setImageURI(selectedImageUri)
                    customimageRadipoButton.isChecked = true
                    isImagePicked = true

                    imagePicked = selectedImageUri

                } else {
                    customimageRadipoButton.isChecked = false
                    isImagePicked = false
                }
            }
        }
    }


    // classes for device managing

    @RequiresApi(Build.VERSION_CODES.M)
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

                } else {

                    editor.remove("imgEnableLockScreen")
                    editor.apply()

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
                    showSnackBar("You have turned off the fixture")
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





    private fun doOperation() {
        shareMyApk()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, ) {
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


                    /// use pop dialog telling the user to grant permisoon

                    if (someDenied) {
                        // here app Setting open because all permission is not granted
                        // and permanent denied
                        //  appSettingOpen(this)

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
            //  activity.finish()
        }
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }



    private fun shareMyApk() {
        try {

            val baseApkLocation =
                applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA).sourceDir

            val baseApk = File(baseApkLocation)

            val path = Environment.getExternalStorageDirectory().toString() + "/Download/Syn2AppLive/APK/"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            // Copy the .apk file to downloads directory
            val destination = File(
                path + "Syn2App.apk"
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



            val nameOfApk =  "Syn2App.apk"
            val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/APK/$nameOfApk"

            val  nameOfpackage = this.packageName

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





}