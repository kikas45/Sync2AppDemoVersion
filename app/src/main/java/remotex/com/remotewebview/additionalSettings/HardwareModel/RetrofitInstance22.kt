package remotex.com.remotewebview.additionalSettings.HardwareModel

import remotex.com.remotewebview.additionalSettings.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance22 {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService22 by lazy {
        retrofit.create(ApiService22::class.java)
    }

}