package remotex.com.remotewebview.additionalSettings.HardwareModel

import com.google.gson.annotations.SerializedName

data class HardWareModel22(
    @SerializedName("DeviceName") val deviceName: String,
    @SerializedName("Model") val model: String,
    @SerializedName("Manufacturer") val manufacturer: String,
    @SerializedName("Brand") val brand: String,
    @SerializedName("OSVersion") val osVersion: String,
    @SerializedName("SDKVersion") val sdkVersion: String,
    @SerializedName("BuildNumber") val buildNumber: String
)
