package ke.ac.mwaks.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import ke.ac.mwaks.R
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SignUp : Fragment() {

    private val TAG = "SignUp"
    private lateinit var msignUpSelectImage: ImageView
    private lateinit var mselectedImage: ImageView
    private val auth = FirebaseAuth.getInstance()

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var msignUpButton: CardView

    private val database = FirebaseDatabase.getInstance()

    private lateinit var fragmentButtonToActivityClickListener: FragmentButtonToActivityClickListener

    private lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        msignUpSelectImage = view.findViewById(R.id.signUpSelectImage)
        mselectedImage = view.findViewById(R.id.selectedImage)
        msignUpButton = view.findViewById(R.id.signUpButton)
        email = view.findViewById(R.id.signUpEmail)
        password = view.findViewById(R.id.signUpPassword)
        progress = ProgressDialog(this.requireContext())
        progress.setMessage("Creating account.Please wait.....")
        progress.setCancelable(false)

        msignUpSelectImage.setOnClickListener { v ->
            Methods.openImagePicker(requireActivity())
        }

        msignUpButton.setOnClickListener {

            val mail = email.text.trim().toString()
            val pass = password.text.trim().toString()

            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                progress.show()
                auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener { it ->
                    if (it.isComplete) {
                        if (it.isSuccessful) {
                            val result = it.result
                            val user = AppUser(result.user!!.uid, mail, "", LocalDateTime.now(), false, emptyList(), emptyList(), emptyList())
                            Log.d(TAG, "onCreateView: $result")

                            // saving user metadata to database
                            database.getReference("user_metadata").push().setValue(user)
                                .addOnCompleteListener {

                                    progress.hide()
                                    if (it.isComplete) {
                                        if (!it.isSuccessful) {
                                            fragmentButtonToActivityClickListener.onButtonClicked()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed to succeed with error : ${it.exception!!.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else
                                        Toast.makeText(
                                            requireContext(),
                                            "Failed to complete with error : ${it.exception!!.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    if (it.isCanceled)
                                        Toast.makeText(
                                            requireContext(),
                                            "Cancelled with error : ${it.exception!!.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                }

                        }else{
                            auth.signOut()
                        }
                    }
                }
            }else
                Toast.makeText(requireContext(), "Provide all required fields.", Toast.LENGTH_SHORT).show()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentButtonToActivityClickListener)
            fragmentButtonToActivityClickListener = context
        else
            throw RuntimeException("Please provide a implementation of FragmentButtonToActivityClickListener.")
    }


}