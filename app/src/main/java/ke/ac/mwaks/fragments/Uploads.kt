package ke.ac.mwaks.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.RecyclerItemWithRemoveOption
import ke.ac.mwaks.model.ListItemWithRemoveOption
import ke.ac.mwaks.util.Methods
import java.security.Permissions

class Uploads : Fragment() {

    private val TAG = "Uploads"
    private lateinit var mselectedFilesRecycler: RecyclerView
    private lateinit var mopenCamera: CardView
    private lateinit var mfileUpload: CardView
    private lateinit var mimageUpload: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_uploads, container, false)
        mselectedFilesRecycler = view.findViewById(R.id.selectedFilesRecycler)
        mopenCamera = view.findViewById(R.id.openCamera)
        mfileUpload = view.findViewById(R.id.fileUpload)
        mimageUpload = view.findViewById(R.id.imageUpload)

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
                Methods.openFilePicker(requireActivity())
        }

        val adapter = RecyclerItemWithRemoveOption(
            items = mutableListOf(
                ListItemWithRemoveOption(1, "Hello You searched me recently"),
                ListItemWithRemoveOption(2, "Hello me"),
                ListItemWithRemoveOption(3, "Hello"),
                ListItemWithRemoveOption(4, "Hello")
            )
        )
        mselectedFilesRecycler.adapter = adapter
        mselectedFilesRecycler.layoutManager = LinearLayoutManager(
            requireActivity().applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )

        // Inflate the layout for this fragment
        return view
    }

    fun captureImage() {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: req : $requestCode, data : $data")

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
                Methods.openFilePicker(requireActivity())
        }
    }

}