package ke.ac.mwaks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ke.ac.mwaks.fragments.Account_activity
import ke.ac.mwaks.fragments.Downloads
import ke.ac.mwaks.fragments.OnBoarding
import ke.ac.mwaks.fragments.Uploads

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment = when (position){
        0 -> Account_activity()

        //check if user is logged in or not
        //if not load on boarding
        1 -> Downloads()

        //when user clicks any item and not logged in// prompt for login
        2 -> Uploads()
        else -> throw IllegalStateException("Unable to parse route.")
    }
}