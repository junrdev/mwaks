package ke.ac.mwaks.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CaptureRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.RecyclerItemWithRemoveOption
import ke.ac.mwaks.model.FileModel
import ke.ac.mwaks.model.ListItemWithRemoveOption
import ke.ac.mwaks.model.SearchItemCache
import ke.ac.mwaks.util.Methods
import java.io.File
import java.security.Permissions

private const val TAG = "Uploads"

class Uploads : Fragment() {

    private lateinit var mselectedFilesRecycler: RecyclerView
    private lateinit var mopenCamera: CardView
    private lateinit var mfileUpload: CardView
    private lateinit var mimageUpload: CardView
    private var selectedFiles = mutableListOf<SearchItemCache>()
    private lateinit var adapter: RecyclerItemWithRemoveOption
    private var fileList = mutableListOf<File>()
    private lateinit var muploadButton: CardView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_uploads, container, false)
        mselectedFilesRecycler = view.findViewById(R.id.selectedFilesRecycler)
        mopenCamera = view.findViewById(R.id.openCamera)
        mfileUpload = view.findViewById(R.id.fileUpload)
        mimageUpload = view.findViewById(R.id.imageUpload)
        muploadButton = view.findViewById(R.id.uploadButton)
        mopenCamera.setOnClickListener {

            if (checkCameraPermissions() && checkFilePermissions())
                Methods.openCamera(requireActivity())
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
                Methods.openImagePicker(requireActivity())
        }
        mfileUpload.setOnClickListener {
            if (checkFilePermissions())
                openFilePicker()
        }


        adapter = RecyclerItemWithRemoveOption(
            items = selectedFiles
        ) {
            Log.d(TAG, "onCreateView: $it")
        }

        mselectedFilesRecycler.adapter = adapter
        mselectedFilesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        muploadButton.setOnClickListener {
            Log.d(TAG, "onCreateView: ${fileList.toString()}")
        }
        // Inflate the layout for this fragment
        return view
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
        if (requestCode == Methods.FILE_PICK_CODE && resultCode == Activity.RESULT_OK)
            data?.data?.let {
                val result = it
                val fname = Methods.getFileName(result, requireActivity())
                selectedFiles.add(SearchItemCache(text = fname))
                adapter.notifyItemInserted(selectedFiles.size - 1)
                if (it != null)
                    fileList.add(File(Methods.getPathFromUri(it, requireContext())))
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
}