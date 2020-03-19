package thomas.park.altube.youtube

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.videoview_custom.view.*
import thomas.park.altube.R
import kotlin.math.abs

class VideoPlayerView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): MotionLayout(context, attrs, defStyleAttr) {

    private val TAG = "VideoPlayerView"

    //Widget
    private var motionLayout: MotionLayout

    private var touchableArea: View? = null

    private val coverLayout: ConstraintLayout
    private val playingImgView: ImageView
    private val prevImgView: ImageView
    private val nextImgView: ImageView

    // point
    private var startX: Float? = null
    private var startY: Float? = null

    init {

        motionLayout = LayoutInflater.from(context).inflate(R.layout.view_videoplayer, this, false) as MotionLayout

        addView(motionLayout)

        coverLayout = motionLayout.findViewById(R.id.videoCoverLayout) as ConstraintLayout
        playingImgView = motionLayout.findViewById(R.id.playing) as ImageView
        prevImgView = motionLayout.findViewById(R.id.prevArrow) as ImageView
        nextImgView = motionLayout.findViewById(R.id.nextArrow) as ImageView
    }

    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onInterceptHoverEvent")
        val isInProgress = (motionLayout.progress > 0.0f && motionLayout.progress < 1.0f)
        val isInTarget = touchEventInsideTargetView(motionLayout, event)

        return if(isInProgress || isInTarget) {
            super.onInterceptHoverEvent(event)
        } else {
            true
        }
    }

    private fun touchEventInsideTargetView(view: View, event: MotionEvent): Boolean {
        Log.e(TAG, "touchEventInsideTargetView")
        if(event.x > view.left && event.x < view.right) {
            if(event.y > view.top && event.y < view.bottom) {
                return true
            }
        }
         return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, "dispatchTouchEvent")
        if(touchEventInsideTargetView(coverLayout, ev)) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = ev.x
                    startY = ev.y
                }

                MotionEvent.ACTION_UP -> {
                    val endX = ev.x
                    val endY = ev.y
                    if(isAClick(startX!!, endX, startY!!, endY)) {
                        if(doClickTransition()) {
                            return true
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun doClickTransition(): Boolean {
        Log.e(TAG, "doClickTransition")
        var isClickHandled = false

        if(motionLayout.progress < 0.05f) {
            motionLayout.transitionToEnd()
            isClickHandled = true
        } else if(motionLayout.progress > 0.95f) {
            motionLayout.transitionToStart()
            isClickHandled = true
        }
        return isClickHandled
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        Log.e(TAG, "isAClick")
        val differenceX = abs(startX - endX)
        val differenceY = abs(startY - endY)
        return !/* =5*/(differenceX > 200 || differenceY > 200)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(TAG, "onTouchEvent")
        return false
    }


    private fun onClick(view: View) {
        when(view.id) {
            R.id.videoCoverLayout -> {
                Log.e(CustomVideoView.TAG, "click event")
                coverLayout.visibility = if(coverLayout.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    fun setVideoInfo(videoFile: Int, uploaderName: String, title: String) {

        fullScreenVideoTitle.text = title
        fullScreenVideoTitle.visibility = View.INVISIBLE
        val videoRootPath = "android.resource://${context.packageName}/"
        videoView.setVideoURI(Uri.parse(videoRootPath + videoFile))
        videoView.setOnPreparedListener { videoView.start() }
    }

    fun isPlaying() = videoView.isPlaying

    fun pause() = videoView.pause()

    fun resume() = videoView.resume()


    fun stop() {
        videoView.pause()
        videoView.stopPlayback()
    }

}