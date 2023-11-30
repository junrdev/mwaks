package ke.ac.mwaks.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayout
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.RecyclerItemWithRemoveOption
import ke.ac.mwaks.model.ListItemWithRemoveOption

class Downloads : Fragment() {

    private lateinit var mrecentSearches : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_downloads, container, false)
        mrecentSearches = view.findViewById(R.id.recentSearches)

        val items = mutableListOf(
            ListItemWithRemoveOption(1, "Hello You searched me recently"),
            ListItemWithRemoveOption(2, "Hello me too"),
            ListItemWithRemoveOption(3, "Hello, what you guys too"),
            ListItemWithRemoveOption(4, "Hello, err wtf")
        )

        val adapter= RecyclerItemWithRemoveOption(items)
        mrecentSearches.layoutManager = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL, false)
        mrecentSearches.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

}