package ke.ac.mwaks.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.RecyclerItemWithImageAndRemove
import ke.ac.mwaks.adapter.RecyclerItemWithRemoveOption
import ke.ac.mwaks.model.SelectedItem
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "Uploads"

class Uploads : Fragment() {

    private lateinit var mselectedFilesRecycler: RecyclerView
    private lateinit var mselectedImagesRecycler: RecyclerView
    private lateinit var mopenCamera: CardView
    private lateinit var mfileUpload: CardView
    private lateinit var mimageUpload: CardView
    private lateinit var muploadButtonTxt: TextView
    private var selectedFiles = mutableListOf<SelectedItem>()
    private var allItems = mutableListOf<SelectedItem>()
    private lateinit var filesAdapter: RecyclerItemWithRemoveOption

    private lateinit var imagesAdapter: RecyclerItemWithImageAndRemove
    private var selectedImages = mutableListOf<SelectedItem>()

    private var fileList = mutableListOf<File>()
    private lateinit var muploadButton: CardView
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var fragmentButtonToActivityClickListener: FragmentButtonToActivityClickListener
    private var uploadedFiles = hashMapOf<String, String>()

    private var fileUploadsPath = Firebase.storage.reference.child("uploads/files/")
    private lateinit var userFiles: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_uploads, container, false)
        mselectedFilesRecycler = view.findViewById(R.id.selectedFilesRecycler)
        mselectedImagesRecycler = view.findViewById(R.id.selectedImagesRecycler)
        mopenCamera = view.findViewById(R.id.openCamera)
        mfileUpload = view.findViewById(R.id.fileUpload)
        mimageUpload = view.findViewById(R.id.imageUpload)
        muploadButton = view.findViewById(R.id.uploadButton)
        muploadButtonTxt = view.findViewById(R.id.uploadButtonTxt)

        mopenCamera.setOnClickListener {

            if (checkCameraPermissions() && checkFilePermissions())
                ImagePicker.with(this)
                    .cameraOnly()
                    .compress(2048)
                    .crop()
                    .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_ALARMS)!!)
                    .start()
            else if (!checkCameraPermissions() && checkFilePermissions())
                ActivityCompat.requestPermissions(
                    requireActivity().parent,
                    arrayOf(Manifest.permission.CAMERA),
                    Methods.CAMERA_PERMISSION_CODE
                )
            else if (checkCameraPermissions() && !checkFilePermissions())
                ActivityCompat.requestPermissions(
                    requireActivity().parent,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Methods.FILES_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    requireActivity().parent,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    Methods.All_PERMISSION_CODE
                )

        }

        mimageUpload.setOnClickListener {
            if (checkFilePermissions())
                ImagePicker.with(this)
                    .galleryOnly()
                    .galleryMimeTypes(/*Exclude gif images*/ mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/webp",
                        "image/jpeg"
                    )
                    )
                    .compress(2048)//set final maximum size to 2Mb
                    .crop()
                    .start()

        }
        mfileUpload.setOnClickListener {
            if (checkFilePermissions())
                openFilePicker()
        }

        //setting the images recycler
        imagesAdapter = RecyclerItemWithImageAndRemove(requireContext(), selectedImages)
        mselectedImagesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mselectedImagesRecycler.adapter = imagesAdapter


//        filesAdapter = RecyclerItemWithRemoveOption(items = allItems.filter { item -> item.type == "file" }.toMutableList())

//        mselectedFilesRecycler.adapter = filesAdapter
//        mselectedFilesRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (firebaseAuth.currentUser == null)
            muploadButtonTxt.compoundDrawables[0] =
                resources.getDrawable(R.drawable.baseline_lock_person_24)
        else {
            //change upload path
            val numFiles = allItems.size
            userFiles =
                FirebaseDatabase.getInstance().reference.child(firebaseAuth.currentUser!!.uid)
                    .child("uploads")
            currentUser = firebaseAuth.currentUser!!

            muploadButton.setOnClickListener {
                Log.d(TAG, "onCreateView: ${fileList.toString()}")
                if (firebaseAuth.currentUser == null) {
                    fragmentButtonToActivityClickListener.onButtonClicked()
                } else {
                    // do uploading
                    CoroutineScope(Dispatchers.Main).launch {
                        uploadAllFiles(this)
                    }.invokeOnCompletion {
                        if (it?.message != null)
                            Log.d(TAG, "onCreateView: ${it.message!!}")
                        else
                            Toast.makeText(
                                requireContext(),
                                "Finished uploading $numFiles files.",
                                LENGTH_SHORT
                            ).show()
                    }

                }
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun uploadAllFiles(context: CoroutineScope) {

        val tasks = hashMapOf<String?, Uri?>()

        context.launch {

            // create tasks
            for (file in allItems) {
                val path = file.uri?.let {
                    context?.let {
                        Methods.getPathFromUri(file.uri, requireContext())
                    }
                }

                tasks.put(
                    path,
                    Uri.fromFile(File(path))
                )

            }

            if (!tasks.isEmpty())
                for (t in tasks.keys) {
                    tasks.get(t)?.let { uri ->
                        fileUploadsPath.putFile(uri).continueWithTask {
                            if (!it.isSuccessful)
                                it.exception?.let {
                                    throw it
                                }
                            fileUploadsPath.downloadUrl
                        }.addOnCompleteListener {
                            if (it.isSuccessful && it.isComplete) {
                                val image_or_file_url = it.result

                                if (t != null) {
                                    uploadedFiles.put(t, image_or_file_url.toString())
                                }
                            }
                        }
                    }
                }
        }.invokeOnCompletion {
            userFiles.updateChildren(uploadedFiles.toMutableMap() as Map<String, Any>)
                .addOnCompleteListener {
                    selectedFiles.clear()
                    selectedImages.clear()
                    allItems.clear()
                    filesAdapter.notifyDataSetChanged()
                }
        }
    }

    fun openImageSelector() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Methods.IMAGE_PICK_CODE)
    }

    fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        startActivityForResult(intent, Methods.FILE_PICK_CODE)
    }

    fun checkCameraPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkFilePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext().applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: $resultCode data : $data")

        var fileName: String
        var result: Uri

        when (requestCode) {

            Methods.FILE_PICK_CODE -> {
                data?.data?.let { uri ->
                    result = uri
                    fileName = Methods.getFileName(result, requireActivity())
                }
            }

            //insert images
            ImagePicker.REQUEST_CODE -> {
                data?.data?.let { uri ->
                    result = uri
                    fileName = Methods.getFileName(result, requireActivity())

                    selectedImages.add(SelectedItem(uri = uri, fileName = fileName))
                    imagesAdapter.notifyItemInserted(selectedImages.size - 1)

                }

            }

            else -> throw IllegalStateException("Failed to handle requestcode : $requestCode")

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size > 1) {
            Log.d(TAG, "onRequestPermissionsResult: $grantResults")
        }

        if (requestCode == Methods.All_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                Methods.openCamera(requireActivity());
        } else if (requestCode == Methods.FILES_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openFilePicker()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentButtonToActivityClickListener)
            fragmentButtonToActivityClickListener = context
    }

}
