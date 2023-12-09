package ke.ac.mwaks.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.model.ListItemWithRemoveOption
import ke.ac.mwaks.model.SearchItemCache
import ke.ac.mwaks.model.SelectedItem

private const val TAG = "RecyclerItemWithRemoveO"
class RecyclerItemWithRemoveOption constructor(
    val items: MutableList<SelectedItem>,
) :
    RecyclerView.Adapter<RecyclerItemWithRemoveOption.RecyclerItemWithRemoveViewHolder>() {


    inner class RecyclerItemWithRemoveViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var itemName: EditText = itemView.findViewById(R.id.listItemText)
        private var removeItemButton: ImageView = itemView.findViewById(R.id.listItemRemove)

        fun bind(
            selectedItem: SelectedItem,
            index: Int,
        ) {
            itemName.setText(selectedItem.fileName)


            itemName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val newText = itemName.text.toString()
                    selectedItem.fileName = newText
                    items[adapterPosition] = selectedItem
                }
            }

            removeItemButton.setOnClickListener {

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }

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
    }
}
