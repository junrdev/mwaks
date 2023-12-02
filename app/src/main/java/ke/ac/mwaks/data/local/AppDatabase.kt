package ke.ac.mwaks.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ke.ac.mwaks.data.local.dao.SearchItemCacheDao
import ke.ac.mwaks.model.SearchItemCache

@Database(
    entities = [SearchItemCache::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun searchItemCacheDao() : SearchItemCacheDao

    companion object{
        fun getDB(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "com.mwaks.localizedcache").build()
        }
    }
}