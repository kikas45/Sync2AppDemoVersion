package remotex.com.remotewebview.additionalSettings.DownloadsArray.data


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import remotex.com.remotewebview.additionalSettings.DownloadsArray.model.DownloadModel

@Dao
interface DownloadDao {

    @Query("UPDATE user_table SET downloadedBytes = :downloadedBytes, totalSize = :totalSize WHERE id = :id")
    suspend fun updateDownloadProgress(id: Long, downloadedBytes: Long, totalSize: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: DownloadModel)

    @Update
    suspend fun updateUser(user: DownloadModel)

    @Delete
    suspend fun deleteUser(user: DownloadModel)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM user_table ORDER BY id DESC")
    fun readAllData(): LiveData<List<DownloadModel>>

}