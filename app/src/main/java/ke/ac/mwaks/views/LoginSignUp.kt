package ke.ac.mwaks.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import ke.ac.mwaks.MainActivity
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.LoginSignUpPagerAdapter
import ke.ac.mwaks.fragments.SignIn
import ke.ac.mwaks.fragments.SignUp
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LoginSignUp"
private lateinit var imageUri: Uri
class LoginSignUp : AppCompatActivity(), FragmentButtonToActivityClickListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var demoMode: CardView
    private lateinit var sharedPreferences: SharedPreferences
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        viewPager2 = findViewById(R.id.loginSignUpPager)
        tabLayout = findViewById(R.id.loginSignUpTab)
        demoMode = findViewById(R.id.showDemo)
        sharedPreferences = getSharedPreferences("appmode", MODE_PRIVATE)

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        demoMode.setOnClickListener {
            sharedPreferences.edit {
                this.putString("demomode", "true")
            }
            onButtonClicked()
        }


        val adapter = LoginSignUpPagerAdapter(this)
        viewPager2.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            viewPager2.beginFakeDrag()
            delay(1000)
            viewPager2.endFakeDrag()
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                else -> tab.text = "Register"
            }
        }.attach()

    }

    override fun onButtonClicked() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}