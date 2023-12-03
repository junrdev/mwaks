package ke.ac.mwaks.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.sql.Time
import java.time.LocalDateTime
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class DateConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun getSavableDate(date: Date) : Long{
        return date.time
    }

    @TypeConverter
    fun fetchReadableDate(long: Long) : Date ?{
        return Date(long)
    }
}