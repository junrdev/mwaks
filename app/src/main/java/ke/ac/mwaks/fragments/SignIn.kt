package ke.ac.mwaks.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import ke.ac.mwaks.MainActivity
import ke.ac.mwaks.R
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.viewmodel.AuthScreensViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.log

private const val TAG = "SignIn"

class SignIn : Fragment() {

    private lateinit var fragmentButtonToActivityClickListener: FragmentButtonToActivityClickListener

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var mloginEmail: EditText
    private lateinit var mloginPassword: EditText
    private lateinit var msignInButton: CardView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        mloginEmail = view.findViewById(R.id.loginEmail)
        mloginPassword = view.findViewById(R.id.loginPassword)
        msignInButton = view.findViewById(R.id.signInButton)


        msignInButton.setOnClickListener {
            email = mloginEmail.text.toString()
            password = mloginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isComplete) {

                        if (it.isSuccessful) {
                            // move to main activity
                            fragmentButtonToActivityClickListener.onButtonClicked()
                        } else
                            Toast.makeText(
                                requireContext(),
                                "Failed to succeed with error : ${it.exception!!.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(
                            requireContext(),
                            "Failed to complete. Reason ${it.exception!!.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            } else
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Provide all required fields",
                    Toast.LENGTH_SHORT
                ).show()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentButtonToActivityClickListener)
            fragmentButtonToActivityClickListener = context
        else
            throw RuntimeException("${context} Lacks an implementations of FragmentButtonToActivityClickListener.")
    }


}