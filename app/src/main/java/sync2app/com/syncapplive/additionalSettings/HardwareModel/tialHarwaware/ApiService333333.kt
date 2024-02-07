package sync2app.com.syncapplive.additionalSettings.HardwareModel.tialHarwaware

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*
interface ApiService333333 {
    @GET("posts")
    suspend fun getAppConfig(): Response<DeviceList22222>
}
*/

/*
interface ApiService333333 {
    @GET("posts")
    suspend fun getAppConfig(
        @Query("userId") userId: Int): Response<List<DeviceList22222>>
}*/

/*
interface ApiService333333 {
    @GET("posts")
    suspend fun getAppConfig(
      //  @Query("userId") userId: Int): Response<List<DeviceList22222Item>>
        @Query("userId") userId: Int): Response<List<DeviceList22222Item>>
}
*/


interface ApiService333333 {
    @GET("posts")
    suspend fun getAppConfig(
        //  @Query("userId") userId: Int): Response<List<DeviceList22222Item>>
        @Query("title") userId: String): Response<List<DeviceList22222Item>>
}
