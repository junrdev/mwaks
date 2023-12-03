package ke.ac.mwaks.data.remote.repo

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ke.ac.mwaks.model.FileModel
import java.io.File

class FilesRepository {

    private val FILESREF = "files"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getDocuments(): List<FileModel> {

        val files: List<FileModel> = emptyList()

        database.getReference("files").get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.isComplete) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    snapshot.children.forEach { snapshot ->
                        val _data = snapshot.getValue(FileModel::class.java)
                        // insert data to files
                    }
                }
            }
        }
        return files
    }

    /***
     * Insert single document to storage documents
     */
    fun uploadDocument(fileModel: FileModel) {
        database.getReference(FILESREF).push().setValue(fileModel).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful);
        }
    }

    fun uploadDocuments(fileModels: List<FileModel>) {
        fileModels.forEach {model -> uploadDocument(model)}
    }
}