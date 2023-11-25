package ke.ac.mwaks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ke.ac.mwaks.fragments.Account_activity
import ke.ac.mwaks.fragments.Downloads
import ke.ac.mwaks.fragments.Uploads

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment = when (position){
        0 -> Downloads()
        1 -> Account_activity()
        else -> Uploads()
    }
}