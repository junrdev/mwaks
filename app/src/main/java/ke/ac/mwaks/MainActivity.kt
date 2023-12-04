package ke.ac.mwaks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import ke.ac.mwaks.adapter.ViewPagerAdapter
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import ke.ac.mwaks.viewmodel.AuthScreensViewModel
import ke.ac.mwaks.views.LoginSignUp
import kotlinx.coroutines.Dispatchers


class MainActivity : AppCompatActivity(), FragmentButtonToActivityClickListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageView: ImageView
    private lateinit var toolbar: Toolbar

    private val authScreensViewModel = AuthScreensViewModel()

    private val TAG = "MainActivity"
    private lateinit var user: FirebaseUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolbar)
        requirePermissions()


        imageView = findViewById(R.id.profilePic)

        imageView.setOnClickListener {
        }
        runOnUiThread { authScreensViewModel.updateLoginStatus() }

        if (authScreensViewModel.uiState.value.isLoggedIn) {
            user = authScreensViewModel.uiState.value.auth.currentUser!!
        }


        Glide.with(this)
            .load(Methods.ADMIN_IMAGE_URL)
            .transform(CircleCrop())
            .into(imageView)

        Log.d(TAG, "onCreate: ${authScreensViewModel.TAG}")

        //transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        val pagerAdapter = ViewPagerAdapter(this)

        viewPager2 = findViewById(R.id.homePager)
        viewPager2.adapter = pagerAdapter


        bottomNavigationView = findViewById(R.id.homeBottomNavigation)


        bottomNavigationView.setOnNavigationItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.download -> viewPager2.currentItem = 0
                R.id.account -> viewPager2.currentItem = 1
                R.id.upload -> viewPager2.currentItem = 2
            }
        }

        runOnUiThread {
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bottomNavigationView.menu.getItem(position).isChecked = true
                }
            })
        }
    }

    fun checkPermissions(): Boolean {

        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requirePermissions() {

        if (!checkPermissions())
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                Methods.All_PERMISSION_CODE
            )
        else if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                Methods.CAMERA_PERMISSION_CODE
            )
        else if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                Methods.FILES_PERMISSION_CODE
            )
    }

    override fun onButtonClicked() {
        val intent = Intent(this, LoginSignUp::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: req : $requestCode, data : $data")

    }
}