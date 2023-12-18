package remotex.com.remotewebview.additionalSettings.DownloadsArray.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class DownloadModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    var status: String,
    var title: String,
    var pathName: String,
    var downloadId: Long = -1,
    var downloadedBytes: Long = 0,
    var totalSize: Long = 0
) {
    // Calculate progress based on downloaded bytes and total size
    fun calculateProgress(): Int {
        return if (totalSize > 0) ((downloadedBytes.toDouble() / totalSize.toDouble()) * 100).toInt() else 0
    }
}
