package ke.ac.mwaks.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ke.ac.mwaks.R
import ke.ac.mwaks.data.remote.repo.FilesRepository
import ke.ac.mwaks.model.FileDimensions
import ke.ac.mwaks.model.FileModel
import ke.ac.mwaks.util.Methods
import java.util.UUID

class DownloadsRecyclerAdapter constructor(
    val filesRepository: FilesRepository,
    val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<DownloadsRecyclerAdapter.DownloadItemViewHolder>() {

    private val files: List<FileModel> = filesRepository.getDocuments()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _files = listOf(
        FileModel(
            fileId = UUID.randomUUID().toString(),
            fileType = "pdf",
            fileDimensions = FileDimensions(100F, 100F),
            fileName = "demo file",
            fileSize = 3.144,
            userId = UUID.randomUUID().toString(),
            thumbNail = "demo"
        ),
        FileModel(
            fileId = UUID.randomUUID().toString(),
            fileType = "pdf",
            fileDimensions = FileDimensions(100F, 100F),
            fileName = "demo file",
            fileSize = 3.144,
            userId = UUID.randomUUID().toString(),
            thumbNail = "demo"
        ),
        FileModel(
            fileId = UUID.randomUUID().toString(),
            fileType = "pdf",
            fileDimensions = FileDimensions(100F, 100F),
            fileName = "demo file",
            fileSize = 3.144,
            userId = UUID.randomUUID().toString(),
            thumbNail = "demo"
        ),
        FileModel(
            fileId = UUID.randomUUID().toString(),
            fileType = "pdf",
            fileDimensions = FileDimensions(100F, 100F),
            fileName = "demo file",
            fileSize = 3.144,
            userId = UUID.randomUUID().toString(),
            thumbNail = "demo"
        ),
    )

    class DownloadItemViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        private val thumbNail: ImageView = view.findViewById(R.id.fileModelImage)
        private val fileName: TextView = view.findViewById(R.id.fileName)
        private val fileSize: TextView = view.findViewById(R.id.fileSize)
        private val fileType: TextView = view.findViewById(R.id.fileType)
        private val fileUploadDate: TextView = view.findViewById(R.id.fileUploadDate)
        private val fileDownloadIcon: ImageView = view.findViewById(R.id.downloadIcon)

        fun bind(
            fileModel: FileModel,
            fragmentActivity: FragmentActivity,
            downloadListener: (uid: String) -> Unit
        ) {
            fileName.text = fileModel.fileName
            fileSize.text = "${fileModel.fileSize}"
            fileType.text = fileModel.fileType
            fileUploadDate.text = "Uploaded on : ${fileModel.iat.toString()}"
            fileDownloadIcon.setOnClickListener { downloadListener(fileModel.fileId) }

//            Glide.with(fragmentActivity)
//                .load(Methods.ADMIN_IMAGE_URL)
//                .placeholder(R.drawable.account_circle_24)
////                .centerCrop()
////                .transform(CircleCrop())
//                .into(thumbNail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item_result, parent, false)
        return DownloadItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        return _files.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DownloadItemViewHolder, position: Int) {
        holder.bind(_files[position], fragmentActivity) {
            Toast.makeText(
                fragmentActivity.applicationContext,
                "File ${_files[position].fileId}",
                Toast.LENGTH_SHORT
            ).show()

            // implement download functionality
        }
    }
}