package remotex.com.remotewebview.additionalSettings.DownloadsArray.repository

import androidx.lifecycle.LiveData
import remotex.com.remotewebview.additionalSettings.DownloadsArray.data.DownloadDao
import remotex.com.remotewebview.additionalSettings.DownloadsArray.model.DownloadModel


class DownloadRepository(private val userDao: DownloadDao) {

    val readAllData: LiveData<List<DownloadModel>> = userDao.readAllData()

    suspend fun addUser(user: DownloadModel) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: DownloadModel) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: DownloadModel) {
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    suspend fun updateDownloadProgress(id: Long, downloadedBytes: Long, totalSize: Long) {
        userDao.updateDownloadProgress(id, downloadedBytes, totalSize)
    }
}
