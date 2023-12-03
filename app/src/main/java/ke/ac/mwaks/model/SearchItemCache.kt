package ke.ac.mwaks.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ke.ac.mwaks.util.DateConverter
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@Entity
@TypeConverters(value = [DateConverter::class])
data class SearchItemCache @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey val id: String = UUID.randomUUID().toString().split("-").toString(),
    @ColumnInfo(name = "search_text") val text: String,
    @ColumnInfo(name = "search_time") val searchTime: Date = Date(System.currentTimeMillis())
)
//{
//    fun getSearchTime(long: Long) = Date(long)
//    fun setSearchTime(localDateTime: Date): Long = localDateTime.time
//}
