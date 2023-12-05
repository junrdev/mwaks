package ke.ac.mwaks.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

object Methods {

    const val IMAGE_PICK_CODE = 111
    const val FILE_PICK_CODE = 123

    const val CAMERA_PERMISSION_CODE = 101
    const val FILES_PERMISSION_CODE = 123
    const val All_PERMISSION_CODE = 125
    const val REQUEST_IMAGE_CAPTURE = 102
    const val ADMIN_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mwaks-api.appspot.com/o/admin%2Fdefaults%2Fcf5e4aaf8dbbed0c485fa18eee5d7ad9.jpg?alt=media&token=4e14456b-a2b2-45f1-86ea-79dfdc6726ad"
    fun openImagePicker(fragmentActivity: FragmentActivity){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.setDataAndType( null,"image/*")
        fragmentActivity.startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFileName(uri: Uri, fragmentActivity: FragmentActivity) : String{
        var name = ""
        fragmentActivity.contentResolver.query(uri, null, null, null)?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            name = it.getString(nameIndex)
        }

        return name
    }

    fun openCamera(fragmentActivity: FragmentActivity){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(fragmentActivity.packageManager) != null)
            fragmentActivity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    fun getPathFromUri(uri: Uri, context: Context): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath
    }

}