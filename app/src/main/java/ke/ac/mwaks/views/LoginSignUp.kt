package ke.ac.mwaks.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ke.ac.mwaks.MainActivity
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.LoginSignUpPagerAdapter
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener

class LoginSignUp : AppCompatActivity(), FragmentButtonToActivityClickListener{

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        viewPager2 = findViewById(R.id.loginSignUpPager)
        tabLayout = findViewById(R.id.loginSignUpTab)


        val adapter = LoginSignUpPagerAdapter(this)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                else -> tab.text = "Register"
            }
        }.attach()


    }

    override fun onButtonClicked() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}