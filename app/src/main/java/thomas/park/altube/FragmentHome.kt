package thomas.park.altube

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import thomas.park.altube.youtube.VideoInfo

class FragmentHome : Fragment() {

    companion object {
        private val TAG = FragmentHome::class.java.simpleName
    }

    var itemTouch = false

    lateinit var mainLayout : MotionLayout
    lateinit var videoImage : ImageView
    lateinit var photosAdapter: PhotosAdapter


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
            adapter = FragmentHomeAdapter(context, Photos)
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
                    if(MotionEvent.ACTION_UP == e.action && itemTouch) {
                        Log.e("onIntercept", "trjegwl")
                        val child = rv.findChildViewUnder(e.x, e.y)
                        val pos = rv.getChildAdapterPosition(child!!)
                        if(pos != -1) {
                            Log.e(TAG, "pos = $pos")
                            val txtView = child.findViewById(R.id.videoDescriptionTxtView) as AppCompatTextView
                            photosAdapter.updateVideoInfo(VideoInfo(txtView.text.toString(), txtView.text.toString()))
                            videoImage.setImageResource(Photos.photoImages[pos - 1])
                            mainLayout.transitionToState(R.id.expanded)
                        }
                    } else if(MotionEvent.ACTION_DOWN == e.action) {
                        itemTouch = true
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }

        return view
    }
}