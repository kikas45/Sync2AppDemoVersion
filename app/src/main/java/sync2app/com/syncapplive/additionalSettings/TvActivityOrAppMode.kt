package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.ApiUrls.ApiUrlViewModel
import sync2app.com.syncapplive.additionalSettings.ApiUrls.DomainUrl
import sync2app.com.syncapplive.additionalSettings.ApiUrls.SavedApiAdapter
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.SavedHistoryListAdapter
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.User
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkStoragePermission
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.urlchecks.getPermissionStatus
import sync2app.com.syncapplive.additionalSettings.urlchecks.requestStoragePermission
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.additionalSettings.utils.Utility
import sync2app.com.syncapplive.databinding.ActivityTvOrAppModeBinding
import sync2app.com.syncapplive.databinding.CustomApiHardCodedLayoutBinding
import sync2app.com.syncapplive.databinding.CustomApiUrlLayoutBinding
import sync2app.com.syncapplive.databinding.CustomGrantAccessPageBinding
import sync2app.com.syncapplive.databinding.ProgressValidateUserDialogLayoutBinding
import java.io.File
import java.util.Objects

class TvActivityOrAppMode : AppCompatActivity(), SavedApiAdapter.OnItemClickListener {
    private lateinit var binding: ActivityTvOrAppModeBinding
    private var hasPermission = false
    private var isReady = false

    private var getUrlBasedOnSpinnerText = ""
    private var API_Server = "API-Cloud App Server"
    private var CP_server = "CP-Cloud App Server"

    private val mApiViewModel by viewModels<ApiUrlViewModel>()


    private val adapterApi by lazy {
        SavedApiAdapter(this)
    }
    private lateinit var custom_ApI_Dialog: Dialog

    private lateinit var customProgressDialog: Dialog

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


    private val simpleSavedPassword: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SIMPLE_SAVED_PASSWORD,
            Context.MODE_PRIVATE
        )
    }


    @SuppressLint("CommitPrefEdits", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvOrAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SIMPLE PASSWORD


        val CheckForPassword = simpleSavedPassword.getString(Constants.onCreatePasswordSaved, "")

        if (CheckForPassword!!.isEmpty()) {
            val editor = simpleSavedPassword.edit()
            editor.putString(Constants.onCreatePasswordSaved, "onCreatePasswordSaved")
            editor.putString(Constants.simpleSavedPassword, "00000")
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


        val editoreee = simpleSavedPassword.edit()
        getUrlBasedOnSpinnerText = ""
        editoreee.remove(Constants.Saved_Domains_Name)
        editoreee.remove(Constants.Saved_Domains_Urls)
        editoreee.apply()


        binding.imgUserMasterDomainORCustom.setOnCheckedChangeListener { compoundButton, isValued ->

            hideKeyBoard(binding.editTextUserID)
            if (compoundButton.isChecked) {

                binding.texturlsSavedDownload.text = "Select Partner Url"
                binding.textPartnerUrlLunch.text = "Select Partner Url"

                editoreee.remove(Constants.Saved_Domains_Name)
                editoreee.remove(Constants.Saved_Domains_Urls)
                editoreee.apply()

            } else {

                binding.textPartnerUrlLunch.text = "Select Custom Domain"
                binding.texturlsSavedDownload.text = "Select Custom Domain"

                getUrlBasedOnSpinnerText = ""

            }
        }


        binding.constraintLayoutSlectDomain.setOnClickListener {
            if (binding.imgUserMasterDomainORCustom.isChecked) {
                serVerOptionDialog()
            } else {
                show_API_Urls()
            }
        }


        binding.apply {
            editTextUserID.setText("CLO")
            editTextLicenseKey.setText("DE_MO_2021001")

            textAppMode.setOnClickListener {
                handleFormVerification()
                navigateAppMolde = true
                navigateTVMode = false

            }

            textTvMode.setOnClickListener {
                handleFormVerification()
                navigateTVMode = true
                navigateAppMolde = false
            }
        }


    }

    private fun handleFormVerification() {
        val get_UserID = binding.editTextUserID.text.toString()
        val get_LicenseKey = binding.editTextLicenseKey.text.toString()

        hideKeyBoard(binding.editTextUserID)

        if (Utility.isNetworkAvailable(this)) {


            val get_Saved_Domains_Urls =
                simpleSavedPassword.getString(Constants.Saved_Domains_Urls, "").toString()

            if (binding.imgUserMasterDomainORCustom.isChecked) {

                if (getUrlBasedOnSpinnerText.isNotEmpty()) {
                    when (getUrlBasedOnSpinnerText) {
                        CP_server -> {

                            val get_editTextMaster = Constants.customDomainUrl
                            checkMyConnection(get_UserID, get_LicenseKey, get_editTextMaster)
                        }

                        API_Server -> {
                            showToastMessage("No Logic For API Server Yet")
                        }

                    }


                } else {
                    showToastMessage("Select Partner Url")
                }

            }else if (get_Saved_Domains_Urls.isNotEmpty()) {
                checkMyConnection(get_UserID, get_LicenseKey, get_Saved_Domains_Urls)
            } else {
                showToastMessage("Select Custom Domain")
            }

        } else {
            showToastMessage("No Internet Connection")
        }
    }


    private fun checkMyConnection(
        get_UserID: String,
        get_LicenseKey: String,
        get_editTextMaster: String,
    ) {
        if (get_UserID.isNotEmpty() && get_LicenseKey.isNotEmpty() && get_editTextMaster.isNotEmpty()) {

            val baseUrl = "$get_editTextMaster/$get_UserID/$get_LicenseKey/App/Config/appConfig.json"

            if (get_editTextMaster.startsWith("https://") || get_editTextMaster.startsWith("http://")) {
                showCustomProgressDialog()

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            if (checkMultiplePermission()) {
                                doOperation()
                            }

                            val editorValue = simpleSavedPassword.edit()
                            editorValue.putString(Constants.get_masterDomain, baseUrl)
                            editorValue.putString(Constants.get_UserID, get_UserID)
                            editorValue.putString(Constants.get_LicenseKey, get_LicenseKey)
                            editorValue.putString(Constants.get_editTextMaster, get_editTextMaster)
                            editorValue.apply()

                            val getpermit = getPermissionStatus(this@TvActivityOrAppMode)
                            if (getpermit.equals("true") && checkMultiplePermission()){
                                isReadToMove_All_Permission()
                            }

                        } else {

                            showToastMessage("Invalid User")
                        }
                    } finally {
                        customProgressDialog.dismiss()

                    }
                }


            } else {

                showToastMessage("Invalid Master Url Format")

            }


        } else {
            if (get_UserID.isEmpty()) {
                binding.editTextUserID.error = "Input User Id"
            }

            if (get_LicenseKey.isEmpty()) {
                binding.editTextLicenseKey.error = "Input LicenseKey"
            }
        }
    }


    private fun doOperation() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (hasPermission == false) {
                showPop_For_Grant_Permsiion()
            }
        } else {
            isReadToMoveForKower()
        }
    }

    private fun isReadToMoveForKower() {
        binding.apply {
            val sharedBiometric: SharedPreferences =
                applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            if (navigateAppMolde == true) {

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")


            }


            if (navigateTVMode == true) {

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
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

        val get_savedDominName = simpleSavedPassword.getString(Constants.Saved_Domains_Name, "")
        if (!get_savedDominName.isNullOrEmpty()) {
            binding.texturlsSavedDownload.text = get_savedDominName
        }

    }

    private fun isReadToMove() {
        binding.apply {
            val sharedBiometric: SharedPreferences =
                applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            if (navigateAppMolde == true && isReady == true) {

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
                val file = File(directoryPath)
                delete(file)

                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode)
                editor.putString(Constants.FIRST_TIME_APP_START, Constants.FIRST_TIME_APP_START)
                editor.apply()
                finish()

                showToastMessage("Please wait")


            }


            if (navigateTVMode == true && isReady == true) {

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
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



    private fun isReadToMove_All_Permission() {
        binding.apply {
            val sharedBiometric: SharedPreferences =
                applicationContext.getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE)
            val editor = sharedBiometric.edit()

            if (navigateAppMolde == true ) {

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
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

                val directoryPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
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

    private fun showCustomProgressDialog() {
        try {
            customProgressDialog = Dialog(this)
            val binding = ProgressValidateUserDialogLayoutBinding.inflate(LayoutInflater.from(this))
            customProgressDialog.setContentView(binding.root)
            customProgressDialog.setCancelable(true)
            customProgressDialog.setCanceledOnTouchOutside(false)
            customProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }


    private fun hideKeyBoard(editText: EditText) {
        try {
            Utility.hideKeyBoard(this, editText)
        } catch (ignored: java.lang.Exception) {
        }
    }

    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun serVerOptionDialog() {
        val bindingCm: CustomApiHardCodedLayoutBinding =
            CustomApiHardCodedLayoutBinding.inflate(layoutInflater)
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {

            textApiServer.setOnClickListener {
                binding.texturlsSavedDownload.text = CP_server
                getUrlBasedOnSpinnerText = CP_server

                alertDialog.dismiss()
            }


            textCloudServer.setOnClickListener {
                binding.texturlsSavedDownload.text = API_Server
                getUrlBasedOnSpinnerText = API_Server

                alertDialog.dismiss()
            }


            imageCrossClose.setOnClickListener {
                alertDialog.dismiss()
            }
            closeBs.setOnClickListener {
                alertDialog.dismiss()
            }


        }


        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun show_API_Urls() {
        custom_ApI_Dialog = Dialog(this)
        val bindingCm = CustomApiUrlLayoutBinding.inflate(LayoutInflater.from(this))
        custom_ApI_Dialog.setContentView(bindingCm.root)
        custom_ApI_Dialog.setCancelable(true)
        custom_ApI_Dialog.setCanceledOnTouchOutside(true)
        custom_ApI_Dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (Utility.isNetworkAvailable(this@TvActivityOrAppMode)) {
            bindingCm.progressBar2.visibility = View.VISIBLE
            bindingCm.textTryAgin.visibility = View.GONE
            bindingCm.textErrorText.visibility = View.GONE
            mApiViewModel.fetchApiUrls()
        } else {
            bindingCm.apply {
                progressBar2.visibility = View.GONE
                textErrorText.visibility = View.VISIBLE
                textTryAgin.visibility = View.VISIBLE
                textErrorText.text = "No Internet Connection"
            }

        }

        bindingCm.apply {


            recyclerApi.adapter = adapterApi
            recyclerApi.layoutManager = LinearLayoutManager(applicationContext)

            mApiViewModel.apiUrls.observe(this@TvActivityOrAppMode, Observer { apiUrls ->
                apiUrls?.let {
                    adapterApi.setData(it.DomainUrls)

                    if (it.DomainUrls.isNotEmpty()) {
                        textErrorText.visibility = View.GONE
                        progressBar2.visibility = View.GONE
                        textTryAgin.visibility = View.GONE

                    } else {
                        textErrorText.visibility = View.VISIBLE
                        textTryAgin.visibility = View.VISIBLE
                        textErrorText.text = "Opps! No Data Found"
                    }

                }
            })


            imageCrossClose.setOnClickListener {
                custom_ApI_Dialog.dismiss()
            }

            closeBs.setOnClickListener {
                custom_ApI_Dialog.dismiss()
            }


            textTryAgin.setOnClickListener {
                if (Utility.isNetworkAvailable(this@TvActivityOrAppMode)) {
                    bindingCm.progressBar2.visibility = View.VISIBLE
                    bindingCm.textTryAgin.visibility = View.GONE
                    bindingCm.textErrorText.visibility = View.GONE
                    mApiViewModel.fetchApiUrls()
                } else {
                    bindingCm.apply {
                        progressBar2.visibility = View.GONE
                        textErrorText.visibility = View.VISIBLE
                        textTryAgin.visibility = View.VISIBLE
                        textErrorText.text = "No Internet Connection"
                        showToastMessage("No Internet Connection")
                    }

                }
            }

        }


        custom_ApI_Dialog.show()

    }

    override fun onItemClicked(domainUrl: DomainUrl) {

        val name = domainUrl.name + ""
        val urls = domainUrl.url + ""
        if (name.isNotEmpty()) {
            binding.texturlsSavedDownload.text = name
        }

        // Note - later you can use the url as well , the  name is displayed on textview
        if (name.isNotEmpty() && urls.isNotEmpty()) {
            val editor = simpleSavedPassword.edit()
            editor.putString(Constants.Saved_Domains_Name, name)
            editor.putString(Constants.Saved_Domains_Urls, urls)
            editor.apply()
        }


        custom_ApI_Dialog.dismiss()
    }


}