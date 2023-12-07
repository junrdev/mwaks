package ke.ac.mwaks.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.model.ListItemWithRemoveOption
import ke.ac.mwaks.model.SearchItemCache

class RecyclerItemWithRemoveOption constructor(
    val items: MutableList<SearchItemCache>,
//    val onItemClickListener: (searchItem: SearchItemCache, index: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerItemWithRemoveOption.RecyclerItemWithRemoveViewHolder>() {

    private val TAG = "RecyclerItemWithRemoveO"

    inner class RecyclerItemWithRemoveViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var itemName: TextView = itemView.findViewById(R.id.listItemText)
        private var removeItemButton: ImageView = itemView.findViewById(R.id.listItemRemove)

        fun bind(
            searchItemCache: SearchItemCache,
            index: Int,
//            onItemClickListener: (searchItem: SearchItemCache, index: Int) -> Unit
        ) {
            itemName.text = searchItemCache.text
            removeItemButton.setOnClickListener {

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }

//                onItemClickListener(searchItemCache, index)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerItemWithRemoveViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recent_search_item, parent, false)
        return RecyclerItemWithRemoveViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerItemWithRemoveViewHolder, position: Int) {
        holder.bind(items[position], position)
//        { item, index ->
//            Log.d(TAG, "onBindViewHolder: $item @ $index")
//            onItemClickListener(items[position], position)
//        }
    }
}
