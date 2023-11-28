package ke.ac.mwaks

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import ke.ac.mwaks.adapter.ViewPagerAdapter
import ke.ac.mwaks.util.Methods


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageView: ImageView
    private lateinit var toolbar: Toolbar

    //    private lateinit var
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolbar)

        imageView = findViewById(R.id.profilePic)

        Glide.with(this)
            .load("https://firebasestorage.googleapis.com/v0/b/mwaks-api.appspot.com/o/admin%2F2023-04-16-22-53-49-373.jpg?alt=media&token=61c130f4-aaac-4295-8ab8-bb58aaa5ab88")
            .transform(CircleCrop())
            .into(imageView)

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
}