package sync2app.com.syncapplive.additionalSettings.OnFileChange

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/*
interface ApiOnChnage {
    @GET("timestamp.json")
    suspend fun getAppConfig(): Response<ModelOnChnage>
}
*/


/*

interface ApiOnChnage {
    @GET("{dynamicPart}/timestamp.json")
    suspend fun getAppConfig(@Path("dynamicPart") dynamicPart: String): Response<ModelOnChnage>
}
*/


interface ApiOnChnage {
    @GET("timestamp.json")
    suspend fun getAppConfig(): Response<ModelOnChnage>

    // If you want to pass dynamic part in URL
    @GET("{dynamicPart}/timestamp.json")
    suspend fun getAppConfig(@Path("dynamicPart") dynamicPart: String): Response<ModelOnChnage>
}