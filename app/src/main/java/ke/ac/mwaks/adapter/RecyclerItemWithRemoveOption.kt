package ke.ac.mwaks.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.model.ListItemWithRemoveOption

class RecyclerItemWithRemoveOption :
    RecyclerView.Adapter<RecyclerItemWithRemoveOption.RecyclerItemWithRemoveViewHolder>() {

    private val TAG = "RecyclerItemWithRemoveO"

    val items = mutableListOf(
        ListItemWithRemoveOption(1, "Hello"),
        ListItemWithRemoveOption(2, "Hello"),
        ListItemWithRemoveOption(3, "Hello"),
        ListItemWithRemoveOption(4, "Hello")
    )

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
        holder.bind(items[position]) {
            Log.d(TAG, "onBindViewHolder: $position")
//            items.removeAt(position)
        }
    }

    class RecyclerItemWithRemoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemName: TextView = itemView.findViewById(R.id.listItemText)
        private var removeItemButton: ImageView = itemView.findViewById(R.id.listItemRemove)

        fun bind(
            listItemWithRemoveOption: ListItemWithRemoveOption,
            callBack: (id: Int) -> Unit
        ) {
            itemName.text = listItemWithRemoveOption.listText
            removeItemButton.setOnClickListener { callBack(listItemWithRemoveOption.id) }
        }
    }
}
