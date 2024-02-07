package sync2app.com.syncapplive.additionalSettings.DownloadsArray.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.additionalSettings.DownloadsArray.data.DownloadDatabase
import sync2app.com.syncapplive.additionalSettings.DownloadsArray.data.model.DownloadModel
import sync2app.com.syncapplive.additionalSettings.DownloadsArray.data.repository.DownloadRepository

class DownloadViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<DownloadModel>>
    private val repository: DownloadRepository

    init {
        val userDao = DownloadDatabase.getDatabase(
            application
        ).userDao()
        repository = DownloadRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: DownloadModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: DownloadModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: DownloadModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun deleteAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

    fun updateDownloadProgress(id: Long, downloadedBytes: Long, totalSize: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDownloadProgress(id, downloadedBytes, totalSize)
        }
    }
}