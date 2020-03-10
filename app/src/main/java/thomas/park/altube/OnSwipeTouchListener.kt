package thomas.park.altube

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context, mainLayout: MotionLayout) : View.OnTouchListener {

    companion object {
        const val SWIPE_THRESHOLD = 100
        const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    private var gestureDetector: GestureDetector
    private var mainLayout: MotionLayout

    init {
        gestureDetector = GestureDetector(context, GestureListener())
        this.mainLayout = mainLayout
    }


    override fun onTouch(p0: View?, event: MotionEvent?) = gestureDetector.onTouchEvent(event)

    fun onSwipeLeft() {
        Log.e("onSwipeLeft", "onSwipeLeft")
    }

    fun onSwipeRight() {
        Log.e("onSwipeRight", "onSwipeRight")
    }

    fun onSwipeTop() {
        Log.e("onSwipeTop", "onSwipeTop")
        mainLayout.transitionToStart()
    }

    fun onSwipeBottom() {
        Log.e("onSwipeBottom", "onSwipeBottom")
        mainLayout.transitionToEnd()
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            var result = false

            val diffY = e2!!.y - e1!!.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                }
                result = true
            } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom()
                } else {
                    onSwipeTop()
                }

                result = true
            }

            return result
        }
    }
}