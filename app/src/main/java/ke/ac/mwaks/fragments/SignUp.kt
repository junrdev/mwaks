package ke.ac.mwaks.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import ke.ac.mwaks.R
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import java.io.File
import java.time.LocalDateTime

private const val TAG = "SignUp"

class SignUp : Fragment() {

    private lateinit var msignUpSelectImage: ImageView
    private lateinit var mselectedImage: ShapeableImageView
    private var auth = FirebaseAuth.getInstance()
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var msignUpButton: CardView
    private val database = FirebaseDatabase.getInstance()
    private lateinit var fragmentButtonToActivityClickListener: FragmentButtonToActivityClickListener
    private lateinit var progress: ProgressDialog
    private lateinit var imageUri: Uri
    private val storageRef = Firebase.storage.reference
    private val usersImagesRef = storageRef.child("userblob/")

    private var filepath: String = "no_file_selected"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        msignUpSelectImage = view.findViewById(R.id.signUpSelectImage)
        mselectedImage = view.findViewById(R.id.selectedImage)
        msignUpButton = view.findViewById(R.id.signUpButton)
        email = view.findViewById(R.id.signUpEmail)
        password = view.findViewById(R.id.signUpPassword)

        if (auth == null)
            auth = FirebaseAuth.getInstance()

        progress = ProgressDialog(this.requireContext())
        progress.setMessage("Creating account.Please wait.....")
        progress.setCancelable(false)

        msignUpSelectImage.setOnClickListener { v -> openImage() }

        msignUpButton.setOnClickListener {

            val mail = email.text.trim().toString()
            val pass = password.text.trim().toString()

            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener { it ->
                    if (it.isComplete) {
                        if (it.isSuccessful) {
                            val result = it.result
                            if (!filepath.equals("no_file_selected")) {
                                val uploadFile = Uri.fromFile(File(filepath))
                                val uploadTask = usersImagesRef.child("profile_pics")
                                    .child(auth.currentUser!!.uid).putFile(uploadFile)
                                createUserWithProfilePic(uploadTask, result.user)
                            } else {
                                createUserWithoutProfilePic(result.user)
                            }
                        } else {
                            auth.signOut()
                            Log.d(TAG, "onCreateView: ${it.exception!!.message}")
                            progress.hide()
                        }
                    }
                }
            } else
                Toast.makeText(requireContext(), "Provide all required fields.", Toast.LENGTH_SHORT)
                    .show()

        }

//        view.on
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUserWithProfilePic(uploadTask: UploadTask, user: FirebaseUser?) {
        uploadTask.continueWithTask {
            if (!it.isSuccessful)
                it.exception?.let {
                    throw it
                }
            usersImagesRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {

                val dl_url = it.result
                Log.d(TAG, "createUserWithProfilePic: $dl_url")

                usersImagesRef.metadata.addOnSuccessListener {
                    val appUser = AppUser(
                        user!!.uid,
                        email.text.toString(),
                        dl_url.toString(),
                        LocalDateTime.now(),
                        true,
                        emptyList(),
                        emptyList(),
                        emptyList()
                    )
                    // saving user metadata to database
                    database.getReference("user_metadata").child(user.uid)
                        .setValue(appUser)
                        .addOnCompleteListener {

                            if (!it.isSuccessful) {
                                fragmentButtonToActivityClickListener.onButtonClicked()
                            }
//                            progress.hide()
                            if (it.isComplete) {
                                if (it.isSuccessful) {

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
                        .addOnFailureListener { e ->
                            auth.signOut()
                            Log.d(TAG, "onCreateView: Failed to save user : ${e.message}")
                        }
                }


            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Failed to complete with error : ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUserWithoutProfilePic(user: FirebaseUser?) {
        val user = AppUser(
            user!!.uid,
            email.text.toString(),
            Methods.ADMIN_IMAGE_URL,
            LocalDateTime.now(),
            true,
            emptyList(),
            emptyList(),
            emptyList()
        )
        // saving user metadata to database
        database.getReference("user_metadata").push()
            .setValue(user)
            .addOnCompleteListener {

                progress.hide()
                if (it.isComplete) {
                    if (!it.isSuccessful) {
                        fragmentButtonToActivityClickListener.onButtonClicked()
                    } else {
                        auth.signOut()
                        Toast.makeText(
                            requireContext(),
                            "Failed to succeed with error : ${it.exception!!.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    auth.signOut()
                    Toast.makeText(
                        requireContext(),
                        "Failed to complete with error : ${it.exception!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (it.isCanceled) {
                    auth.signOut()

                    Toast.makeText(
                        requireContext(),
                        "Cancelled with error : ${it.exception!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "onCreateView: Failed to save user : ${e.message}")
            }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Methods.IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            data?.data?.let {
                Log.d(TAG, "onActivityResult: $it")
                imageUri = it
                mselectedImage.setImageURI(it)
                filepath = it.let {
                    requireContext()?.let { _context ->
                        Methods.getPathFromUri(imageUri, _context)
                    }
                }.toString()

                Log.d(TAG, "onActivityResult: $filepath")

            }
        }
    }

    fun openImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Methods.IMAGE_PICK_CODE)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentButtonToActivityClickListener)
            fragmentButtonToActivityClickListener = context
        else
            throw RuntimeException("${context} Lacks an implementations of FragmentButtonToActivityClickListener.")
    }
}