package thomas.park.altube;


import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thomas.park.altube.youtube.Videos

class ContentsAdapter(private val context: Context, private val mainLayout: ConstraintLayout) :
    RecyclerView.Adapter<ContentsAdapter.YouTubeDemoViewHolder>() {

    companion object {
        val TAG = ContentsAdapter::class.java.simpleName
        var reverse = false
        var height = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeDemoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.item_row -> YouTubeDemoViewHolder.FishRowViewHolder(
                itemView
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: YouTubeDemoViewHolder, position: Int) {
        when (holder) {
            is YouTubeDemoViewHolder.FishRowViewHolder -> {
                val imagePosition = position - 2
                holder.textView.text =
                    holder.textView.resources.getString(R.string.fish_n, imagePosition)
                Glide.with(context)
                    .load(Videos.thumbnail[imagePosition])
                    .into(holder.imageView)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_header
            1 -> R.layout.item_description
            else -> R.layout.item_row
        }
    }

    override fun getItemCount() = Videos.thumbnail.size + 2

    sealed

    class YouTubeDemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class FishRowViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView) {
            val imageView = itemView.findViewById(R.id.image_row) as ImageView
            val textView = itemView.findViewById(R.id.text_row) as TextView
        }

    }
}