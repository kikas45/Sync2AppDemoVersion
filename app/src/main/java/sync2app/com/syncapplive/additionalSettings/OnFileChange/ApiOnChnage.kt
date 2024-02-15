package sync2app.com.syncapplive.additionalSettings.OnFileChange

import retrofit2.Response
import retrofit2.http.GET

interface ApiOnChnage {
    @GET("timestamp.json")
    suspend fun getAppConfig(): Response<ModelOnChnage>
}
