package thomas.park.altube.youtube

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_ymain.*
import kotlinx.android.synthetic.main.activity_ymain.ymainLayout
import thomas.park.altube.*
import kotlin.math.abs

class YMainActivity : AppCompatActivity() {

    companion object {
        val TAG = YMainActivity::class.java.simpleName
    }

    private var isPlaying = false
    private var alpha = 0.0f
    lateinit var photosAdapter: PhotosAdapter

    private var x = 0.0f
    private var y = 0.0f
    private var point1 = 0.0f
    private var point2 = 0.0f
    private var point3 = 0.0f
    private var point4 = 0.0f


    private val fragmentHome = FragmentHome()
    private val fragmentHot = FragmentHot()
    private val fragmentSubscribe = FragmentSubscribe()
    private val fragmentLetterBox = FragmentLetterBox()
    private val fragmentCubbyhole = FragmentCubbyhole()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ymain)

        photosAdapter = PhotosAdapter(this@YMainActivity, ymainLayout)
        fragmentHome.photosAdapter = photosAdapter
        recommendListView.apply {
            adapter = photosAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@YMainActivity)
        }

        bottom_ynav.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottom_ynav.selectedItemId = R.id.navigation_home

        val ymainLayout = findViewById<MotionLayout>(R.id.ymainLayout)

        playImage.setOnTouchListener(this@YMainActivity::touchEvent)
        clearImage.setOnTouchListener(this@YMainActivity::touchEvent)
        videoImage.setOnTouchListener(this@YMainActivity::touchEvent)


        ymainLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.e(TAG, "onTransitionTrigger")
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.e(TAG, "onTransitionStarted")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                fromState: Int,
                toState: Int,
                progress: Float
            ) {
                if (toState == R.id.collapsed) {
                    if (progress >= 0.9) {
                        alpha = (progress - 0.9f) + 0.1f
                        clearImage.alpha = alpha
                        playImage.alpha = alpha
                        videoTitle.alpha = alpha
                        videoUploaderName.alpha = alpha
                    }
                }
                if (progress == 1.0f) {
                    visible(1.0f, View.VISIBLE)
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentState: Int) {
                Log.e(TAG, "onTransitionCompleted")
                motionLayout.clearFocus()
                if (motionLayout.startState == R.id.baseState) {
                    motionLayout.setTransition(R.id.expanded, R.id.collapsed)
                    motionLayout.progress = 0.0f
                }
                if (currentState == R.id.collapsed) {

                    emptySpace.visibility = View.VISIBLE
                    visible(1.0f, View.VISIBLE)
                    videoImage.coverVisible(View.INVISIBLE)
                } else {
                    emptySpace.visibility = View.GONE
                    visible(0.0f, View.INVISIBLE)
                }
            }
        })

    }

    fun visible(alpha: Float, visibility: Int) {
        clearImage.alphaVisibility(alpha, visibility)
        playImage.alphaVisibility(alpha, visibility)
        videoTitle.alphaVisibility(alpha, visibility)
        videoUploaderName.alphaVisibility(alpha, visibility)
    }


    private fun View.alphaVisibility(alpha: Float, visibility: Int) {
        this.alpha = alpha
        this.visibility = visibility
    }

    private fun touchEvent(view: View, evt: MotionEvent): Boolean {
        Log.e(TAG, "touchEvent")
        when (evt.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                point1 = view.x
                point2 = view.x + view.width
                point3 = view.y
                point4 = view.y + view.height
                return true
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                x = evt.x + point1
                y = evt.y + point3
                if (x in point1..point2 && y in point3..point4) {
                    when (view.id) {
                        R.id.clearImage -> {
                            ymainLayout.transitionToState(R.id.baseState)
                            videoImage.stop()
                        }
                    }
                }
                return false
            }
        }

        return view.performClick()
    }

    override fun onBackPressed() {
        when (ymainLayout.currentState) {
            R.id.expanded -> {
                ymainLayout.transitionToState(R.id.collapsed)
            }
            else -> super.onBackPressed()
        }

    }

    inner class MenuItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.drawable.ic_home_red_24dp)
                    fragmentHome.mainLayout = ymainLayout
                    fragmentHome.videoImage = videoImage
                    fragmentHome.videoTitle = videoTitle
                    fragmentHome.videoUploader = videoUploaderName
                    fragmentHome.playImage = playImage
                    transaction.replace(R.id.page_container, fragmentHome)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_hot -> {
                    item.setIcon(R.drawable.ic_whatshot_red_24dp)
                    transaction.replace(R.id.page_container, fragmentHot)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_subscribe -> {
                    transaction.replace(R.id.page_container, fragmentSubscribe)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_letter_box -> {
                    transaction.replace(R.id.page_container, fragmentLetterBox)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_cubby_hole -> {
                    transaction.replace(R.id.page_container, fragmentCubbyhole)
                        .commitAllowingStateLoss()
                }
            }
            return true
        }
    }
}