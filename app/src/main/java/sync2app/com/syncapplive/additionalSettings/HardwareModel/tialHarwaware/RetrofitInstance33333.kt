package sync2app.com.syncapplive.additionalSettings.HardwareModel.tialHarwaware

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sync2app.com.syncapplive.additionalSettings.utils.Constants.Companion.BASE_URL_UR

object RetrofitInstance33333 {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_UR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService333333 by lazy {
        retrofit.create(ApiService333333::class.java)
    }
}
