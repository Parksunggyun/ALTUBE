package thomas.park.altube

import android.content.Context
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class PhotosAdapter(private val context : Context, private val mainLayout : ConstraintLayout) : RecyclerView.Adapter<PhotosAdapter.YouTubeDemoViewHolder>() {

    companion object {
        val TAG = PhotosAdapter::class.java.simpleName
        var reverse = false
        var height = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeDemoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)

        return when(viewType) {
            R.layout.motion_24_recyclerview_expanded_text_header -> YouTubeDemoViewHolder.TextHeaderViewHolder(itemView)
            R.layout.motion_24_recyclerview_expanded_text_description -> YouTubeDemoViewHolder.TextDescriptionViewHolder(itemView)
            R.layout.motion_24_recyclerview_expanded_row -> YouTubeDemoViewHolder.FishRowViewHolder(itemView)
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: YouTubeDemoViewHolder, position: Int) {
        when(holder) {
            is YouTubeDemoViewHolder.TextHeaderViewHolder -> {
                Log.e("TextHeaderViewholder", "on bind view holder")
                if(height <= 1) {
                    holder.contentDescriptionTextView.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                holder.contentDescriptionTextView.viewTreeObserver.removeOnGlobalLayoutListener(
                                    this
                                )
                                height = holder.contentDescriptionTextView.height
                                height = holder.contentDescriptionTextView.measuredHeight
                                Log.e("TextHeaderViewholder", "$height")
                                holder.contentDescriptionTextView.visibility = View.GONE
                            }
                        })
                }
                holder.textDescriptionTextView.apply {
                    text = "입력하는데로 제목을 입력할 수 있습니다."
                }

                holder.layoutDescriptionConstraintLayout.setOnClickListener {
                    reverse = !reverse
                    val animation : Animation
                    if(!reverse) {
                        animation = AnimationUtils.loadAnimation(context, R.anim.rotation_arrow_reverse)
                        collapse(holder.contentDescriptionTextView)
                        //changeVisibility(holder.contentDescriptionLayout, false)
                    } else {
                        animation = AnimationUtils.loadAnimation(context, R.anim.rotation_arrow)
                        expand(holder.contentDescriptionTextView)
                        //changeVisibility(holder.contentDescriptionLayout, true)
                    }
                    holder.imageDescriptionImageView.animation = animation
                    holder.imageDescriptionImageView.startAnimation(animation)
                }


                holder.headerNav.apply {
                    setOnNavigationItemSelectedListener(onClick)
                    Log.e(TAG, "headerNav apply")
                    val thumbUp = menu.findItem(R.id.header_thumb_up)
                    val thumbDown = menu.findItem(R.id.header_thumb_down)
                    val share = menu.findItem(R.id.header_share)
                    val fileDownload = menu.findItem(R.id.header_file_download)
                    val fileDownload2 = menu.findItem(R.id.header_file_download2)


                    thumbUp.title = context.getString(R.string.text_header_thumb_up, "15")
                    thumbDown.title = context.getString(R.string.text_header_thumb_down, "15")

                }

            }
            is YouTubeDemoViewHolder.TextDescriptionViewHolder -> {}
            is YouTubeDemoViewHolder.FishRowViewHolder -> {
                val imagePosition = position - 2
                holder.textView.text =
                    holder.textView.resources.getString(R.string.fish_n, imagePosition)
                Glide.with(context)
                    .load(Fishes.fishImages[imagePosition])
                    .into(holder.imageView)
            }
        }
    }

    private fun expand(view: View) {
        val targetHeight = height
        Log.e(TAG, "targetHeight = $targetHeight")

        view.layoutParams.height = 1
        view.visibility = View.VISIBLE
        val animation = object : Animation() {

            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.layoutParams.height = if(interpolatedTime == 1.0f) ConstraintLayout.LayoutParams.WRAP_CONTENT
                                            else ((targetHeight * interpolatedTime)).toInt()
                Log.e(TAG, "expand = ${((targetHeight * interpolatedTime)).toInt()}")
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration = 250
        view.animation = animation
        view.startAnimation(animation)
    }

    private fun collapse(view: View) {
        val initialHeight = view.measuredHeight
        Log.e(TAG, "initialHeight = $initialHeight")

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if(interpolatedTime.toInt() == 1) view.visibility = View.GONE
                else {
                    view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        height = initialHeight
        animation.duration = 250
        view.animation = animation
        view.startAnimation(animation)

    }

    private val onClick = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.header_thumb_up -> {
                    Snackbar.make(mainLayout, "좋아요는 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()
                    return true
                }
                R.id.header_thumb_down -> {
                    Snackbar.make(mainLayout, "싫어요는 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()
                    return true
                }
                R.id.header_share -> {
                    Snackbar.make(mainLayout, "공유하기 기능은 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()

                    return true
                }
                R.id.header_file_download -> {
                    Snackbar.make(mainLayout, "갤러리에 저장 기능은 추후에 추가할 예정입니다", Snackbar.LENGTH_SHORT).show()
                    return true
                }
            }
            return false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> R.layout.motion_24_recyclerview_expanded_text_header
            1 -> R.layout.motion_24_recyclerview_expanded_text_description
            else -> R.layout.motion_24_recyclerview_expanded_row
        }
    }

    override fun getItemCount() = Fishes.fishImages.size + 2

    sealed class YouTubeDemoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        class TextHeaderViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView) {

            val layoutDescriptionConstraintLayout = itemView.findViewById(R.id.layout_description) as ConstraintLayout

            val textDescriptionTextView = itemView.findViewById(R.id.text_description) as AppCompatTextView

            val imageDescriptionImageView = itemView.findViewById(R.id.image_description) as AppCompatImageView

            val headerNav = itemView.findViewById(R.id.header_nav) as BottomNavigationView

            val contentDescriptionTextView = itemView.findViewById(R.id.content_description) as ConstraintLayout
/*            val thumbUpLayout = itemView.findViewById(R.id.layout_thumb_up) as ConstraintLayout
            val thumbDownLayout = itemView.findViewById(R.id.layout_thumb_down) as ConstraintLayout
            val shareLayout = itemView.findViewById(R.id.layout_share) as ConstraintLayout
            val fileDownloadLayout = itemView.findViewById(R.id.layout_file_download) as ConstraintLayout

            val thumbUpTextView = thumbUpLayout.findViewById(R.id.text) as AppCompatTextView
            val thumbDownTextView = thumbDownLayout.findViewById(R.id.text) as AppCompatTextView
            val shareTextView = shareLayout.findViewById(R.id.text) as AppCompatTextView
            val fileDownloadTextView = fileDownloadLayout.findViewById(R.id.text) as AppCompatTextView

            val thumbUpImageView = thumbUpLayout.findViewById(R.id.image) as AppCompatImageView
            val thumbDownImageView = thumbDownLayout.findViewById(R.id.image) as AppCompatImageView
            val shareImageView = shareLayout.findViewById(R.id.image) as AppCompatImageView
            val fileDownloadImageView = fileDownloadLayout.findViewById(R.id.image) as AppCompatImageView*/
        }

        class TextDescriptionViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView)

        class FishRowViewHolder(
            itemView: View
        ) : YouTubeDemoViewHolder(itemView) {
            val imageView = itemView.findViewById(R.id.image_row) as ImageView
            val textView = itemView.findViewById(R.id.text_row) as TextView
        }

    }
}