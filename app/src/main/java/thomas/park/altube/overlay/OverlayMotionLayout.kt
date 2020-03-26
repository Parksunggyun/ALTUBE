package thomas.park.altube.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import thomas.park.altube.overlay.PhotosAdapter
import thomas.park.altube.R
import kotlin.math.abs

class OverlayMotionLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): MotionLayout(context, attrs, defStyleAttr) {

    private val TAG = OverlayMotionLayout::class.java.simpleName

    private var isCover = false

    private var motionLayout: MotionLayout = LayoutInflater.from(context).inflate(R.layout.layout_overlay, this, false) as MotionLayout
    private val touchableArea: View
    private val clickableArea: VideoView
    private val clearArea: ImageButton
    private val playArea: ImageButton
    private val coverArea: View
    private val titleArea: TextView
    private val bottomNav: BottomNavigationView
    private val recommendListView: RecyclerView
    private val recommendContainer: FrameLayout
    private val smallerImageView: AppCompatImageView
    private val prevImageView: AppCompatImageView
    private val playPauseImageView: AppCompatImageView
    private val nextImageView: AppCompatImageView

    lateinit var photosAdapter: PhotosAdapter

    private var startX: Float? = null
    private var startY: Float? = null

    init {

        addView(motionLayout)
        touchableArea = motionLayout.findViewById(R.id.video_overlay_touchable_area)
        clickableArea = motionLayout.findViewById(R.id.video_overlay_thumbnail)
        clearArea = motionLayout.findViewById(R.id.clearImageView)
        playArea = motionLayout.findViewById(R.id.playImageView)
        coverArea = motionLayout.findViewById(R.id.cover)
        titleArea = motionLayout.findViewById(R.id.video_overlay_title)
        bottomNav = motionLayout.findViewById(R.id.bottomNav)

        smallerImageView = motionLayout.findViewById(R.id.smallerImageView)
        prevImageView = motionLayout.findViewById(R.id.prevImageView)
        playPauseImageView = motionLayout.findViewById(R.id.playPauseImageView)
        nextImageView = motionLayout.findViewById(R.id.nextImageView)

        recommendContainer = motionLayout.findViewById(R.id.recommendListViewContainer)
        recommendListView = motionLayout.findViewById(R.id.recommendListView)
        initAdapter()

        motionLayout.setTransitionListener(object : TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout, p1: Int) {
                if(p0.currentState == R.id.small) {
                    coverArea.alpha = 0.0f
                    isCover = !isCover
                }

            }
        })

    }

    private fun setRecommendData() {
        recommendListView.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = photosAdapter

        }
    }


    /*bottom nav item touch event*/
    fun setBottomNavItemTouchListener(listener: BottomNavigationView.OnNavigationItemSelectedListener, selected: Int) {
        bottomNav.setOnNavigationItemSelectedListener(listener)
        bottomNav.selectedItemId = selected
    }

    private fun initAdapter() {
        photosAdapter = PhotosAdapter(context, motionLayout)
    }

    fun startExpand(videoInfo: VideoInfo) {
        titleArea.text = videoInfo.videoTitle
        prepareVideo(clickableArea, videoInfo.video)
        motionLayout.setTransition(R.id.invisible, R.id.fullscreen)
        motionLayout.setTransitionDuration(250)
        motionLayout.transitionToEnd()
        Log.e(TAG, "currentstate =${motionLayout.currentState}")
        setRecommendData()
    }

    private fun prepareVideo(videoView: VideoView, videoFile: Int) {

        val videoRootPath = "android.resource://${context.packageName}/"
        videoView.setVideoURI(Uri.parse(videoRootPath + videoFile))
        videoView.setOnPreparedListener { videoView.start() }
        videoView.setOnCompletionListener {
            if(motionLayout.currentState == R.id.expanded) {
                coverArea.visibility = View.VISIBLE
            }
            playArea.setImageResource(R.drawable.ic_play_arrow_black_32dp)
        }

    }

    private fun touchEventInsideTargetView(v: View, ev: MotionEvent): Boolean {
        if (ev.x > v.left && ev.x < v.right) {
            if (ev.y > v.top && ev.y < v.bottom) {
                return true
            }
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isInProgress = (motionLayout.progress > 0.0f && motionLayout.progress < 1.0f)
        val isInTarget = touchEventInsideTargetView(touchableArea, ev)
        val isBottomNavTarget = touchEventInsideTargetView(bottomNav, ev)
        val isRecommenListViewTarget = touchEventInsideTargetView(recommendListView, ev)
        return if (isInProgress || isInTarget || isBottomNavTarget || isRecommenListViewTarget) {
            Log.e(TAG, "onInterceptTouchEvent1 ${super.onInterceptTouchEvent(ev)}")
            super.onInterceptTouchEvent(ev)
        } else {
            true
        }
    }

    private fun checkAction(ev: MotionEvent, viewName: String) {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }

            MotionEvent.ACTION_UP   -> {
                val endX = ev.x
                val endY = ev.y
                if (isAClick(startX!!, endX, startY!!, endY)) {
                    when(viewName) {
                        "clearArea" -> doClearTransition()
                        "playArea" -> doPlayTransition()
                        "coverArea" -> doCoverTransition()
                        "touchableArea" -> doClickTransition()
                        "bottomNav" -> doNavItemTransition()
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, "========== dispatchTouchEvent ==========")
        Log.e(TAG, "dispatchTouchEvent")
        Log.e(TAG, "========== dispatchTouchEvent ==========")

        if (touchEventInsideTargetView(clearArea, ev)) {
            checkAction(ev, "clearArea")
        } else if (touchEventInsideTargetView(playArea, ev)) {
            checkAction(ev, "playArea")
        } else if (touchEventInsideTargetView(coverArea, ev)) {
            checkAction(ev, "coverArea")
        } else if (touchEventInsideTargetView(touchableArea, ev)) {
            checkAction(ev, "touchableArea")
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun doClickTransition() {
        if (motionLayout.progress < 0.05F) {
            motionLayout.transitionToEnd()
        } else if (motionLayout.progress > 0.95F) {
            motionLayout.transitionToStart()
        }
    }

    private fun doClearTransition() {
        if (motionLayout.progress > 0.95F) {
            motionLayout.transitionToState(R.id.invisible)
            Log.e(TAG, "doClearTransition ${motionLayout.progress}")
        }
    }

    private fun doPlayTransition() {
        if (motionLayout.progress < 0.05F) {
            Log.e(TAG, "doPlayTransition ${motionLayout.progress}")
        } else if (motionLayout.progress > 0.95F) {
            Log.e(TAG, "doPlayTransition ${motionLayout.progress}")
            if (clickableArea.isPlaying) {
                playArea.setImageResource(R.drawable.ic_play_arrow_black_32dp)
                clickableArea.pause()
            } else {
                playArea.setImageResource(R.drawable.ic_pause_black_24dp)
                clickableArea.resume()
            }
        }
    }

    private fun doCoverTransition() {
        if (motionLayout.progress < 0.05F) {
            if(!isCover) {
                coverArea.alpha = 0.7f
                coverArea.animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                coverArea.startAnimation(coverArea.animation)
            } else {
                coverArea.animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
                coverArea.startAnimation(coverArea.animation)
                coverArea.alpha = 0.0f
            }
            isCover = !isCover
            Log.e(TAG, "doPlayTransition ${motionLayout.progress}")
        } else if (motionLayout.progress > 0.95F) {
            motionLayout.transitionToState(R.id.fullscreen)
        }
    }

    private fun doNavItemTransition() {
        if (motionLayout.progress < 0.05F) {
            Log.e(TAG, "doNavItemTransition ${motionLayout.progress}")
        } else if (motionLayout.progress > 0.95F) {
            Log.e(TAG, "doNavItemTransition ${motionLayout.progress}")
        }
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = abs(startX - endX)
        val differenceY = abs(startY - endY)
        return !/* =5 */(differenceX > 200 || differenceY > 200)
    }


}