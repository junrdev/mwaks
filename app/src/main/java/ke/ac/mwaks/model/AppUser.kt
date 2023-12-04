package ke.ac.mwaks.model

import android.os.Build
import androidx.annotation.RequiresApi
import ke.ac.mwaks.fragments.Downloads
import ke.ac.mwaks.fragments.Uploads
import java.time.LocalDateTime

data class AppUser @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String,
    val email: String,
    val profilePic: String,
    val joinedAt: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true,
    val uploads: List<String>,
    val downloads: List<String>,
    val favourites: List<String>
)
