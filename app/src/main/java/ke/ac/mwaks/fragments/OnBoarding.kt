package ke.ac.mwaks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.LoginSignUpPagerAdapter

class OnBoarding : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var view2: View
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)

        viewPager2 = view.findViewById(R.id.loginSignUpPager)
        tabLayout = view.findViewById(R.id.loginSignUpTab)
        view2  = view.findViewById(R.id.spacer)


        val adapter = LoginSignUpPagerAdapter(requireActivity())

        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                else -> tab.text = "Register"
            }
        }.attach()

        return view
    }

}