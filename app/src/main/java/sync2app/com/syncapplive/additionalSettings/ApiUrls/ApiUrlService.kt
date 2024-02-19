package sync2app.com.syncapplive.additionalSettings.ApiUrls

import retrofit2.Response
import retrofit2.http.GET

interface ApiUrlService {
    @GET("Custom.Json")
    suspend fun getAppConfig(): Response<Api_Ur_lModels>
}
