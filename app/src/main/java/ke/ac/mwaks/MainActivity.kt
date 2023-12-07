package ke.ac.mwaks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.widget.PopupMenu
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ke.ac.mwaks.adapter.ViewPagerAdapter
import ke.ac.mwaks.model.AppUser
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener
import ke.ac.mwaks.util.Methods
import ke.ac.mwaks.viewmodel.AuthScreensViewModel
import ke.ac.mwaks.views.LoginSignUp
import kotlinx.coroutines.Dispatchers
import java.net.URI

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), FragmentButtonToActivityClickListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageView: ImageView
    private lateinit var toolbar: Toolbar

    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser
    private lateinit var rtdb : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolbar)
        requirePermissions()

        imageView = findViewById(R.id.profilePic)


        if (auth.currentUser == null){
            imageView.setOnClickListener {
                showLockedPopupMenu(it)
            }
        }else{
            user = auth.currentUser!!
            rtdb = FirebaseDatabase.getInstance().getReference("user_metadata").child(user.uid)

            //load user metadata
            rtdb.get().addOnCompleteListener {
                // Load users profile picture
                Log.d(TAG, "onCreate: ${it.result}")
                Log.d(TAG, "onCreate: ${it.result.ref}")
                Log.d(TAG, "onCreate: ${it.result.childrenCount}")
                Log.d(TAG, "onCreate: ${it.result.children}")
                Log.d(TAG, "onCreate: ${it.result.key}")

//                it.result.children.forEach {
//                    Log.d(TAG, "onCreate: ${it.value.toString()}")
//                    Log.d(TAG, "onCreate: ${it.child("profilePic")}")
//                    Log.d(TAG, "onCreate: ${it.child("profilePic").value}")
//                }
//                if (it.isSuccessful && it.isComplete) {
//                    it.result.children.forEach {
//                        Log.d(TAG, "onCreate: ${it.value.toString()}")
//                        Log.d(TAG, "onCreate: ${it.child("profilePic")}")
//                        Log.d(TAG, "onCreate: ${it.child("profilePic").value}")
//                    }
////                    Glide.with(this)
////                        .load(it.result?.child("profilePic"))
////                        .placeholder(R.drawable.account_circle_24)
////                        .transform(CircleCrop())
////                        .into(imageView)
//                }
            }
        }


        imageView.setOnClickListener { showPopupMenu(it) }

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

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
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

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.logooutmenu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.logoutMenu) {
                auth.signOut()
                startActivity(Intent(this, LoginSignUp::class.java))
                finish()
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        // dismiss option
        popupMenu.setOnDismissListener { popupMenu.dismiss() }
        popupMenu.show()
    }
    private fun showLockedPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.logooutmenu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.logoutMenu) {
                it.isEnabled = false
            }else if (it.itemId == R.id.shareOption){

                val snackbar = Snackbar.make(view, "To share : ",Snackbar.LENGTH_SHORT)
                snackbar.setAction("Click here"){
//                    val intent = Intent(URI.create(""))
                    Log.d(TAG, "showLockedPopupMenu: snack clicked")
                }
                snackbar.setActionTextColor(resources.getColor(R.color.orange_tint))
                snackbar.show()
            }
            return@setOnMenuItemClickListener false
        }
        // dismiss option
        popupMenu.setOnDismissListener { popupMenu.dismiss() }
        popupMenu.show()
    }

}