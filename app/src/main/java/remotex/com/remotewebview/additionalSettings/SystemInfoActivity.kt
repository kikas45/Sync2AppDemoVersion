package remotex.com.remotewebview.additionalSettings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remotex.com.remotewebview.R
import remotex.com.remotewebview.additionalSettings.HardwareModel.HardWareModel22
import remotex.com.remotewebview.additionalSettings.HardwareModel.RetrofitInstance22
import remotex.com.remotewebview.databinding.ActivitySystemInfoBinding
import remotex.com.remotewebview.databinding.CustomHardwareApprovedBinding
import remotex.com.remotewebview.databinding.CustomHardwareFailedBinding
import retrofit2.HttpException

class SystemInfoActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySystemInfoBinding


    private val handler:Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySystemInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            closeBs.setOnClickListener {
                onBackPressed()
            }




//            val deviceInfoBuilder = StringBuilder()
//
//            deviceInfoBuilder.append("Device name: ").append(Build.DEVICE).append("\n")
//            deviceInfoBuilder.append("Model: ").append(Build.MODEL).append("\n")
//            deviceInfoBuilder.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
//            deviceInfoBuilder.append("Brand: ").append(Build.BRAND).append("\n")
//            deviceInfoBuilder.append("OS Version: ").append(Build.VERSION.RELEASE).append("\n")
//            deviceInfoBuilder.append("SDK Version: ").append(Build.VERSION.SDK_INT).append("\n")
//            deviceInfoBuilder.append("Build Number: ").append(Build.DISPLAY).append("\n")
//
//            val deviceInformation = deviceInfoBuilder.toString()
//
//            Log.d("DeviceInfo", deviceInformation)


            val deviceInfoBuilder = StringBuilder()

            val   Device_nameb = Build.DEVICE.toString() + ""
            val   Model = Build.MODEL.toString() + ""
            val   MANUFACTURER = Build.MANUFACTURER.toString() + ""
            val   BRAND = Build.BRAND.toString() + ""
            val   OS_Version = Build.VERSION.RELEASE.toString() + ""
            val   SDK_Version = Build.VERSION.SDK_INT.toString() + ""
            val   Build_Number = Build.DISPLAY.toString() + ""

            deviceInfoBuilder.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
            deviceInfoBuilder.append("Brand: ").append(Build.BRAND).append("\n")
            deviceInfoBuilder.append("OS Version: ").append(Build.VERSION.RELEASE).append("\n")
            deviceInfoBuilder.append("SDK Version: ").append(Build.VERSION.SDK_INT).append("\n")
            deviceInfoBuilder.append("Build Number: ").append(Build.DISPLAY).append("\n")

            val deviceInformation = deviceInfoBuilder.toString()

            Log.d("DeviceInfo", deviceInformation)


            binding.textHardWareStatus.setOnClickListener {
                if (isNetworkAvailable()){
                    fetchData()
                }else{
                    showToastMessage("No Internet Connection")
                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                    binding.textErrorText.visibility = View.GONE
                }

            }


            startNetWorkCall()


            textTextInfo.text = "Device name"
            textdeviceDescription.text = Build.DEVICE.toString()

            textModel.text = "Model"
            textModelDescription.text = Build.MODEL.toString()


            textMananufacturer.text = "Manufacturer"
            textManufacturerDescription.text = Build.MANUFACTURER.toString()

            textBrand.setText("Brand")
            textBrandDescription.text = Build.BRAND.toString()


            textAndroidTvOs.text = "OS Version"
            textAndroidTvOsDescription.text = Build.VERSION.RELEASE.toString()

            textAndroidSDK.text = "SDK Version"
            textSDKDescription.text = Build.VERSION.SDK_INT.toString()

            textTvOSBuildVersion.text = "Build Number"
            textTvOSBuildVersionDescription.text = Build.DISPLAY.toString()



        }

    }

    private fun startNetWorkCall() {
        binding.progressBar.visibility = View.VISIBLE
        binding.viewCover.visibility = View.VISIBLE
        binding.textErrorText.visibility = View.VISIBLE

        handler.postDelayed(Runnable {
            if (isNetworkAvailable()){
                fetchData()
            }else{
                showToastMessage("No Internet Connection")
                binding.progressBar.visibility = View.GONE
                binding.viewCover.visibility = View.GONE
                binding.textErrorText.visibility = View.GONE
            }

        }, 1000)
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopHardWFailed( deviceName:String, model:String, manufacturer:String, brand:String, osVersion:String, sdkVersion:String, buildNumber:String) {

        val binding: CustomHardwareFailedBinding =
            CustomHardwareFailedBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()
        // alertDialog.setCancelable(false)


        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.apply {

            val deviceInfoBuilder = StringBuilder()
            deviceInfoBuilder.append("Device name: ").append(deviceName).append("\n")
            deviceInfoBuilder.append("Model: ").append(model).append("\n")
            deviceInfoBuilder.append("Manufacturer: ").append(manufacturer).append("\n")
            deviceInfoBuilder.append("Brand: ").append(brand).append("\n")
            deviceInfoBuilder.append("OS Version: ").append(osVersion).append("\n")
            deviceInfoBuilder.append("SDK Version: ").append(sdkVersion).append("\n")
            deviceInfoBuilder.append("Build Number: ").append(buildNumber).append("\n")

            val deviceInformation = deviceInfoBuilder.toString()


            textDisplayResult.text = deviceInformation



            textContinuPassword2.setOnClickListener {
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }




    @SuppressLint("MissingInflatedId")
    private fun showPopHardWareApproved(deviceName:String, model:String, manufacturer:String, brand:String, osVersion:String, sdkVersion:String, buildNumber:String) {

        val binding: CustomHardwareApprovedBinding =
            CustomHardwareApprovedBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()
        // alertDialog.setCancelable(false)


        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))





        binding.apply {


            val deviceInfoBuilder = StringBuilder()
            deviceInfoBuilder.append("Device name: ").append(deviceName).append("\n")
            deviceInfoBuilder.append("Model: ").append(model).append("\n")
            deviceInfoBuilder.append("Manufacturer: ").append(manufacturer).append("\n")
            deviceInfoBuilder.append("Brand: ").append(brand).append("\n")
            deviceInfoBuilder.append("OS Version: ").append(osVersion).append("\n")
            deviceInfoBuilder.append("SDK Version: ").append(sdkVersion).append("\n")
            deviceInfoBuilder.append("Build Number: ").append(buildNumber).append("\n")

            val deviceInformation = deviceInfoBuilder.toString()

            Log.d("DeviceInfo", deviceInformation)

            textDisplayResult.text =  deviceInformation



            textContinuPassword2.setOnClickListener {
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchData() {

        binding.progressBar.visibility = View.VISIBLE
        binding.viewCover.visibility = View.VISIBLE
        binding.textErrorText.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance22.api.getAppConfig()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        binding.viewCover.visibility = View.GONE
                        binding.textErrorText.visibility = View.GONE

                        val deviceName = response.body()?.deviceName.toString()
                        val model = response.body()?.model.toString()
                        val manufacturer = response.body()?.manufacturer.toString()
                        val brand = response.body()?.brand.toString()
                        val osVersion = response.body()?.osVersion.toString()
                        val sdkVersion = response.body()?.sdkVersion.toString()
                        val buildNumber = response.body()?.buildNumber.toString()

                        logAndCompareDataOnTypes(deviceName, model, manufacturer, brand, osVersion, sdkVersion, buildNumber)


                 } else {
                        showToastMessage("Failed to fetch data")
                        binding.progressBar.visibility = View.GONE
                        binding.viewCover.visibility = View.GONE
                        binding.textErrorText.visibility = View.GONE
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showToastMessage("HTTP Exception: ${e.message()}")
                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                    binding.textErrorText.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToastMessage("Error: ${e.message}")
                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                    binding.textErrorText.visibility = View.GONE
                }
            }
        }
    }



    private fun logAndCompareDataOnTypes(deviceName: String,
                                         model:String,
                                         manufacturer:String,
                                         brand:String,
                                         osVersion:String,
                                         sdkVersion:String,
                                         buildNumber:String  ) {

            if (manufacturer.isNotEmpty() && manufacturer == Build.MANUFACTURER.toString()
                //  && deviceName.equals(Build.DEVICE.toString() )
              //  && model.isNotEmpty() && model  == Build.MODEL.toString()
                && brand.isNotEmpty() && brand == Build.BRAND.toString()
                && osVersion.isNotEmpty() && osVersion == Build.VERSION.RELEASE.toString()
                && sdkVersion.isNotEmpty()  && sdkVersion == Build.VERSION.SDK_INT.toString()
                && buildNumber.isNotEmpty()  && buildNumber== Build.DISPLAY.toString()

            ){
                showPopHardWareApproved( deviceName, model, manufacturer, brand, osVersion, sdkVersion, buildNumber)
                binding.textBtnhardWareAprroved.setBackgroundResource(R.drawable.round_edit_text_solid_green_design)
                binding.textBtnhardWareAprroved.text = "HARDWARE APPROVED"
                binding.imageNotApproved!!.visibility = View.INVISIBLE
                binding.imageApproved!!.visibility = View.VISIBLE
            }else{
                showPopHardWFailed( deviceName, model, manufacturer, brand, osVersion, sdkVersion, buildNumber)
                binding.textBtnhardWareAprroved.setBackgroundResource(R.drawable.round_edit_text_solid_red_design)
                binding.textBtnhardWareAprroved.text = "HARDWARE NOT APPROVED"
                binding.imageNotApproved!!.visibility = View.VISIBLE
                binding.imageApproved!!.visibility = View.INVISIBLE


            }

    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }





    /*
        private fun logAndDisplayData(hardwareModel: HardWareModel22?) {
            hardwareModel?.let {
                val logMessage = """
                    DeviceName: ${it.deviceName}
                    Model: ${it.model}
                    Manufacturer: ${it.manufacturer}
                    Brand: ${it.brand}
                    OSVersion: ${it.osVersion}
                    SDKVersion: ${it.sdkVersion}
                    BuildNumber: ${it.buildNumber}
                """.trimIndent()

                // Log the data

                Log.d("logAndDisplayData", "logAndDisplayData: $logMessage")

                // Display the data in the TextView
                //  binding.textDisplayText.text= logMessage
            }
        }
    */


}
