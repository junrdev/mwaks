package ke.ac.mwaks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ke.ac.mwaks.fragments.SignIn
import ke.ac.mwaks.fragments.SignUp

class LoginSignUpPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment = when(position){
        0 -> SignIn()
        1 -> SignUp()
        else -> throw IllegalStateException("Invalid Fragment Screen")
    }
}