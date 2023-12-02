package ke.ac.mwaks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ke.ac.mwaks.model.SearchItemCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

@Dao
interface SearchItemCacheDao {

    @Query("SELECT * FROM SearchItemCache")
    fun getSearchItems() : Flow<List<SearchItemCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearch(searchItemCache: SearchItemCache)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSearchItem(searchItemCache: SearchItemCache)

    @Delete
    suspend fun deleteSearchItem(searchItemCache: SearchItemCache)

//    @Query("DELETE FROM SearchItemCache WHERE search_time <= :localdatetime")
//    suspend fun updateCache(localdatetime : LocalDateTime)
}
