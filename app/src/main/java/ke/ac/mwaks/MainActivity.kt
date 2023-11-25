package ke.ac.mwaks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import ke.ac.mwaks.adapter.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = ViewPagerAdapter(this)

        viewPager2 = findViewById(R.id.homePager)

        viewPager2.let {
            it.isHorizontalScrollBarEnabled = false
            it.isVerticalScrollBarEnabled = false
            it.adapter = pagerAdapter
        }


        bottomNavigationView = findViewById(R.id.homeBottomNavigation)


        bottomNavigationView.setOnNavigationItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.download -> viewPager2.currentItem = 0
                R.id.account -> viewPager2.currentItem = 1
                R.id.upload -> viewPager2.currentItem = 2
            }
            true
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }
}