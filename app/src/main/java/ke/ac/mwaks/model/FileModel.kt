package ke.ac.mwaks.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.UUID

data class FileModel @RequiresApi(Build.VERSION_CODES.O) constructor(
    val fileId: String,
    val fileType : String,
    val fileName: String,
    val fileSize: Double,
    val userId: String,
    val downloads: Int = 0,
    val numPages : Int = 1
)

data class FileDimensions(val length: Float, val width: Float) {
    override fun toString(): String {
        return "$length X $width"
    }
}
