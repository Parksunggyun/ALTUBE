package thomas.park.altube.overlay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import thomas.park.altube.FragmentHome
import thomas.park.altube.FragmentHomeAdapter
import thomas.park.altube.R
import thomas.park.altube.youtube.Videos

class HomeFragment :Fragment() {


    companion object {
        private val TAG = FragmentHome::class.java.simpleName
    }

    var itemTouch = false

    lateinit var overlayView: OverlayMotionLayout

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

                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (MotionEvent.ACTION_UP == e.action && itemTouch) {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        val pos = rv.getChildAdapterPosition(child!!)
                        if(pos != -1) {

                            Log.e("onInterceptTouchEvent", "asdsada")
                            val videoInfo = VideoInfo(Videos.videoUploader[pos - 1], Videos.videoNames[pos - 1], Videos.videoNames[pos - 1], Videos.thumbnail[pos - 1], Videos.video[pos - 1])

                            overlayView.photosAdapter.updateVideoInfo2(videoInfo)

                            overlayView.startExpand(videoInfo)
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

        return view
    }

}