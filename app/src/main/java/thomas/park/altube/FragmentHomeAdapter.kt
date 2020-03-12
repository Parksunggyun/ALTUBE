package thomas.park.altube

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.lang.IllegalStateException

class FragmentHomeAdapter(private val context: Context, private val fishes: Fishes) :
    RecyclerView.Adapter<FragmentHomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)
        return when(viewType) {
            R.layout.item_toolbar -> HomeViewHolder.HomeToolbarViewHolder(itemView)
            R.layout.item_video -> HomeViewHolder.HomeContentsViewHolder(itemView)
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        when(holder) {
            is HomeViewHolder.HomeToolbarViewHolder -> {

            }

            is HomeViewHolder.HomeContentsViewHolder -> {
                Glide.with(context).load(fishes.fishImages[position - 1]).into(holder.thumbnail)
                Glide.with(context).load(fishes.fishImages[position - 1]).into(holder.uploaderImg)
                holder.description.text = fishes.fishNames[position - 1]
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> R.layout.item_toolbar
            else -> R.layout.item_video
        }
    }

    override fun getItemCount() = fishes.fishNames.size + 1



    sealed class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class HomeToolbarViewHolder(itemView: View) : HomeViewHolder(itemView) {

        }

        class HomeContentsViewHolder(itemView: View) : HomeViewHolder(itemView) {

            val thumbnail = itemView.findViewById(R.id.videoThumbnailImageView) as AppCompatImageView
            val description = itemView.findViewById(R.id.videoDescriptionTxtView) as AppCompatTextView
            val uploaderImg = itemView.findViewById(R.id.uploaderImgView) as AppCompatImageView
        }

    }
}