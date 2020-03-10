package thomas.park.altube

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class FragmentHome(val mainLayout: MotionLayout) : Fragment() {

    var itemTouch = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = FragmentHomeAdapter(context, Fishes)
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
                        mainLayout.transitionToStart()
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