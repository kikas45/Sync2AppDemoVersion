package remotex.com.remotewebview.additionalSettings.HardwareModel

import retrofit2.Response
import retrofit2.http.GET

interface ApiService22 {
    @GET("hdw_check.json")
    suspend fun getAppConfig(): Response<HardWareModel22>
}
