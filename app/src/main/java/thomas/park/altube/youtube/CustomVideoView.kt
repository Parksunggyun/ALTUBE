package thomas.park.altube.youtube

import android.content.Context
import android.content.res.TypedArray
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.videoview_custom.view.*
import thomas.park.altube.R

/**
 * 추가할 예정, 유튜브처럼 VideoView 만들기
 */
class CustomVideoView : MotionLayout  {

    companion object {
        val TAG = CustomVideoView::class.java.simpleName
    }

    constructor(context: Context): super(context, null) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs, 0) {
        init()
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
        getAttrs(attrs, defStyleAttr)
    }

    private lateinit var videoView: VideoView
    private lateinit var coverLayout: ConstraintLayout
    private lateinit var downArrow: ImageView
    private lateinit var fullScreenVideoTitle: TextView
    private lateinit var prevArrow: ImageView
    private lateinit var nextArrow: ImageView
    private lateinit var playingArrow: ImageView

    private fun init() {
        val layoutInflateService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(layoutInflateService) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.videoview_custom, this, false)
        addView(view)

        // View initialized
        videoView = view.findViewById(R.id.videoView) as VideoView
        coverLayout = view.findViewById(R.id.videoCoverLayout) as ConstraintLayout
        downArrow = view.findViewById(R.id.downArrow) as ImageView
        fullScreenVideoTitle = view.findViewById(R.id.fullScreenVideoTitle) as TextView
        prevArrow = view.findViewById(R.id.prevArrow) as ImageView
        nextArrow = view.findViewById(R.id.nextArrow) as ImageView
        playingArrow = view.findViewById(R.id.playing) as ImageView

        videoView.setOnClickListener(this::onClick)
        playingArrow.setOnClickListener(this::onClick)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomVideoView)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomVideoView, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {

        typedArray.recycle()
    }


    private fun onClick(view: View) {
        Log.e(TAG, "click event")
        when(view.id) {
            R.id.videoCoverLayout -> {
                coverLayout.visibility = if(coverLayout.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
            }
            R.id.playing -> {
                if(videoView.isPlaying) {
                    playingArrow.setImageResource(R.drawable.ic_play)
                    pause()
                } else {
                    playingArrow.setImageResource(R.drawable.ic_pause)
                    resume()
                }
            }
        }
    }

    fun setVideoInfo(videoFile: Int, playImage: ImageButton, title: String, motionLayout: MotionLayout) {

        fullScreenVideoTitle.text = title
        fullScreenVideoTitle.visibility = View.INVISIBLE
        val videoRootPath = "android.resource://${context.packageName}/"
        videoView.setVideoURI(Uri.parse(videoRootPath + videoFile))
        videoView.setOnPreparedListener { videoView.start() }
        videoView.setOnCompletionListener {
            if(motionLayout.currentState == R.id.expanded) {
                coverLayout.visibility = View.VISIBLE
                playingArrow.setImageResource(R.drawable.ic_play)
            } else if(motionLayout.currentState == R.id.collapsed) {
                playImage.setImageResource(R.drawable.ic_play_arrow_black_32dp)
            }
        }
    }

    fun coverVisible(visible: Int) {
        coverLayout.visibility = visible
    }

    fun isPlaying() = videoView.isPlaying

    fun pause() = videoView.pause()

    fun resume() = videoView.resume()

    fun stop() {
        videoView.pause()
        videoView.stopPlayback()
    }






}