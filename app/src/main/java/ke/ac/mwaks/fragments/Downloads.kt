package ke.ac.mwaks.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import ke.ac.mwaks.R
import ke.ac.mwaks.adapter.DownloadsRecyclerAdapter
import ke.ac.mwaks.adapter.RecyclerItemWithRemoveOption
import ke.ac.mwaks.data.local.AppDatabase
import ke.ac.mwaks.data.local.repo.SearchItemCacheRepository
import ke.ac.mwaks.data.remote.repo.FilesRepository
import ke.ac.mwaks.model.FileModel
import ke.ac.mwaks.model.ListItemWithRemoveOption
import ke.ac.mwaks.model.SelectedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Downloads"

class Downloads : Fragment() {

    private lateinit var mrecentSearches: RecyclerView
    private lateinit var search: TextInputEditText
    private lateinit var files: List<FileModel>
    private lateinit var filesRepository: FilesRepository
    private lateinit var mFilesView: RecyclerView
    private lateinit var adapter : RecyclerItemWithRemoveOption
    @SuppressLint("MissingInflatedId", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filesRepository = FilesRepository()

        val view = inflater.inflate(R.layout.fragment_downloads, container, false)
        mrecentSearches = view.findViewById(R.id.recentSearches)
        search = view.findViewById(R.id.search)
        mFilesView = view.findViewById(R.id.filesView)
        mFilesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        mFilesView.adapter = DownloadsRecyclerAdapter(filesRepository, requireActivity())


        val repo =
            SearchItemCacheRepository(AppDatabase.getDB(requireContext()).searchItemCacheDao())
        var searchItems = mutableListOf<SelectedItem>()
        var searches = repo.searches.value.forEach {

            searchItems.add(SelectedItem(Uri.EMPTY, it.text))
        }
        adapter = RecyclerItemWithRemoveOption(searchItems)

        // add search to recent
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener true
            }
            false
        }
        mrecentSearches.layoutManager = LinearLayoutManager(
            requireActivity().applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        mrecentSearches.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    /***
     * Updates the recycler status after an item(s) is removed
     * from the list
     */
    private fun onRemoveAction(index : Int){
        adapter.notifyItemRemoved(index)
    }

}