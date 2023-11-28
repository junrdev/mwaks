package ke.ac.mwaks.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.UUID

data class FileModel @RequiresApi(Build.VERSION_CODES.O) constructor(
    val fileId: String,
    val fileType: String = FILE_TYPE.PORTABLE_DOCUMENT_FORMAT.extension,
    val fileDimensions: FileDimensions,
    val fileName: String = "${
        UUID.randomUUID().toString().trim().split("-").joinToString()
    }_${LocalDateTime.now()}",
    val fileSize: Double,
    val iat: LocalDateTime = LocalDateTime.now(),
    val userId: String,
    val downloads: Int = 0,
    val thumbNail: String,//link to thumbnail
    val numPages : Int = 1
)

data class FileDimensions(val length: Float, val width: Float) {
    override fun toString(): String {
        return "$length X $width"
    }
}
