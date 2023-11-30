package ke.ac.mwaks.fragments

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
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import ke.ac.mwaks.MainActivity
import ke.ac.mwaks.R
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.viewmodel.AuthScreensViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.log

class SignIn : Fragment() {

    private val TAG = "SignIn"
    private val authScreensViewModel = AuthScreensViewModel()
    private lateinit var appUser: AppUser
    private lateinit var user: FirebaseUser

    private lateinit var imageUrl: String
    private lateinit var imageUri: Uri
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var mloginEmail: EditText
    private lateinit var mloginPassword: EditText
    private lateinit var msignInButton: CardView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        if (auth.currentUser != null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.signInFragment, Account_activity())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        mloginEmail = view.findViewById(R.id.loginEmail)
        mloginPassword = view.findViewById(R.id.loginPassword)
        msignInButton = view.findViewById(R.id.signInButton)


        msignInButton.setOnClickListener {
            email = mloginEmail.text.toString()
            password = mloginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty())
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isComplete && it.isSuccessful) {

                        val result = it.result

                        Log.d(TAG, "onCreateView: ${it.result.user}")
                        //add user details to database
                        if (result.user != null) {
                            val ref = database.getReference("users").child(result.user!!.uid).push()
                                .setValue(
                                    AppUser(
                                        result.user!!.uid,
                                        email,
                                        "",
                                        LocalDateTime.now(),
                                        false,
                                        emptyList<String>(),
                                        emptyList<String>(),
                                        emptyList<String>(),
                                    )
                                ).addOnCompleteListener {

                                    //restart app
//                                    requireActivity().parent.startActivity(
//                                        Intent(
//                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        )
//                                    )

                                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.signInFragment, Account_activity())
                                    transaction.addToBackStack(null)
                                    transaction.commit()

                                    requireActivity().finish()
                                }

                        }
                    }
                }
        }



        return view
    }

}