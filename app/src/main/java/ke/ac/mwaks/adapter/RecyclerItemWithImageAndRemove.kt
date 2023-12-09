package ke.ac.mwaks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ke.ac.mwaks.R
import ke.ac.mwaks.model.SelectedItem

class RecyclerItemWithImageAndRemove(
    val context: Context,
    val selectedImages: MutableList<SelectedItem>
) : RecyclerView.Adapter<RecyclerItemWithImageAndRemove.RecyclerItemWithImageAndRemoveVH>() {


    inner class RecyclerItemWithImageAndRemoveVH(view: View) : RecyclerView.ViewHolder(view) {

        private val mselectedImageItem: ImageView = view.findViewById(R.id.selectedImageItem)
        private val mselectedImageItemRemoveButton: ImageView =
            view.findViewById(R.id.selectedImageItemRemoveButton)


        fun bind(selectedItem: SelectedItem) {

            //load image with Uri
            Glide.with(context)
                .load(selectedItem.uri)
                .centerCrop()
                .placeholder(R.drawable.img1)
                .into(mselectedImageItem)

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                mselectedImageItemRemoveButton.setOnClickListener {
                    selectedImages.remove(selectedItem)
                    notifyItemRemoved(position)
                }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerItemWithImageAndRemoveVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.picked_file_item, parent, false)
        return RecyclerItemWithImageAndRemoveVH((view))
    }

    override fun getItemCount(): Int {
        return selectedImages.size
    }

    override fun onBindViewHolder(holder: RecyclerItemWithImageAndRemoveVH, position: Int) {
        holder.bind(selectedImages[position])
    }
}