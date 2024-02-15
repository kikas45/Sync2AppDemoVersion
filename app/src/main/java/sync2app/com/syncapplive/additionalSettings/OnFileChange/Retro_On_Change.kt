package sync2app.com.syncapplive.additionalSettings.OnFileChange

import sync2app.com.syncapplive.additionalSettings.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sync2app.com.syncapplive.additionalSettings.utils.Constants.Companion.BASE_URL_ON_CHANGE

object Retro_On_Change {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ON_CHANGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiOnChnage by lazy {
        retrofit.create(ApiOnChnage::class.java)
    }
}
