package sync2app.com.syncapplive.additionalSettings.DownloadsArray.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sync2app.com.syncapplive.additionalSettings.DownloadsArray.data.model.DownloadModel


@Database(entities = [DownloadModel::class], version = 1, exportSchema = false)
abstract class DownloadDatabase : RoomDatabase() {

    abstract fun userDao(): DownloadDao

    companion object {
        @Volatile
        private var INSTANCE: DownloadDatabase? = null

        fun getDatabase(context: Context): DownloadDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DownloadDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}