package sync2app.com.syncapplive.additionalSettings.ApiUrls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sync2app.com.syncapplive.additionalSettings.utils.Constants.Companion.BASE_URL_API

object RetrofitInstance_Ap_iUrls {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiUrlService by lazy {
        retrofit.create(ApiUrlService::class.java)
    }
}
