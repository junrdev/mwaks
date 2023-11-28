package ke.ac.mwaks.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import ke.ac.mwaks.R
import ke.ac.mwaks.util.Methods

class SignUp : Fragment() {

    private val TAG = "SignUp"
    private lateinit var msignUpSelectImage: ImageView
    private lateinit var mselectedImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        msignUpSelectImage = view.findViewById(R.id.signUpSelectImage)
        mselectedImage = view.findViewById(R.id.selectedImage)

        msignUpSelectImage.setOnClickListener { v ->
            Methods.openImagePicker(requireActivity())
        }



        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Methods.IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            val selected = data.data
            Log.d(TAG, "onActivityResult: $data")

            if (selected != null)
                Log.d(TAG, "onActivityResult: ${Methods.getFileName(selected, requireActivity())}")
            mselectedImage.setImageURI(selected)

        }
    }
}