package sync2app.com.syncapplive.additionalSettings.savedDownloadHistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", primaryKeys = ["CLO", "DEMO", "EditUrl"])
data class User(
    var id: Long = System.currentTimeMillis(),
    var CLO: String,
    var DEMO: String,
    var EditUrl: String,
)