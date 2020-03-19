package thomas.park.altube

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ymain.*
import kotlinx.android.synthetic.main.fragment_home.*
import thomas.park.altube.youtube.CustomVideoView
import thomas.park.altube.youtube.VideoInfo
import thomas.park.altube.youtube.Videos

class FragmentHome : Fragment() {

    companion object {
        private val TAG = FragmentHome::class.java.simpleName
    }

    private var isPlaying = false

    private var x = 0.0f
    private var y = 0.0f
    private var point1 = 0.0f
    private var point2 = 0.0f
    private var point3 = 0.0f
    private var point4 = 0.0f
    var itemTouch = false

    lateinit var mainLayout: MotionLayout
    lateinit var videoImage: CustomVideoView
    lateinit var playImage: ImageButton
    lateinit var photosAdapter: PhotosAdapter
    lateinit var videoTitle: TextView
    lateinit var videoUploader: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val homeRecyclerView = view.findViewById(R.id.homeRecyclerView) as RecyclerView

        homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = FragmentHomeAdapter(context, Videos)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    itemTouch = false
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    Log.e("onTouchEvent", "asdsada")
                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (MotionEvent.ACTION_UP == e.action && itemTouch) {
                        Log.e("onIntercept", "trjegwl")
                        val child = rv.findChildViewUnder(e.x, e.y)
                        val pos = rv.getChildAdapterPosition(child!!)
                        if (pos != -1) {
                            Log.e(TAG, "pos = $pos")
                            val uploader =
                                child.findViewById(R.id.videoUploaderTxtView) as AppCompatTextView
                            val txtView =
                                child.findViewById(R.id.videoDescriptionTxtView) as AppCompatTextView
                            photosAdapter.updateVideoInfo(
                                VideoInfo(
                                    uploader.text.toString(),
                                    txtView.text.toString(),
                                    txtView.text.toString()
                                )
                            )
                            videoImage.setVideoInfo(Videos.video[pos - 1], playImage, txtView.text.toString(), mainLayout)
                            videoTitle.text = Videos.videoNames[pos - 1]
                            videoUploader.text = uploader.text.toString()
                            mainLayout.transitionToState(R.id.expanded)
                        }
                    } else if (MotionEvent.ACTION_DOWN == e.action) {
                        itemTouch = true
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })
        }

        playImage.setOnTouchListener(this::touchEvent)

        return view
    }



    private fun touchEvent(view: View, evt: MotionEvent): Boolean {
        when (evt.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                point1 = view.x
                point2 = view.x + view.width
                point3 = view.y
                point4 = view.y + view.height
                return true
            }
            MotionEvent.ACTION_UP -> {
                x = evt.x + point1
                y = evt.y + point3
                if (x in point1..point2 && y in point3..point4) {
                    when (view.id) {
                        R.id.playImage -> {
                            if (videoImage.isPlaying()) {
                                playImage.setImageResource(R.drawable.ic_play_arrow_black_32dp)
                                videoImage.pause()
                            } else {
                                playImage.setImageResource(R.drawable.ic_pause_black_24dp)
                                videoImage.resume()
                            }
                        }
                    }
                }
                return false
            }
        }

        return view.performClick()
    }


}