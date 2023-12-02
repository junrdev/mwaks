package ke.ac.mwaks.data.local.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ke.ac.mwaks.data.local.dao.SearchItemCacheDao
import ke.ac.mwaks.model.SearchItemCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

private const val TAG = "SearchItemCacheReposito"

class SearchItemCacheRepository constructor(
    private val searchItemCacheDao: SearchItemCacheDao
) {

    private val _searches = MutableStateFlow<List<SearchItemCache>>(emptyList())

    val  searches = _searches.asStateFlow()

    init {
        searchItemCacheDao.getSearchItems().distinctUntilChanged().onEach {
            if (it.isNotEmpty())
                _searches.value = it
        }
    }

    suspend fun getSearches() = searches

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertSearchItem(query: String) {
        searchItemCacheDao.addSearch(SearchItemCache(text = query))
    }

    suspend fun updateSearches(localDateTime: LocalDateTime) {
//        searchItemCacheDao.updateCache(localDateTime)
    }

    suspend fun deleteSearchItem(searchItemCache: SearchItemCache) {
        searchItemCacheDao.deleteSearchItem(searchItemCache)
    }

}
