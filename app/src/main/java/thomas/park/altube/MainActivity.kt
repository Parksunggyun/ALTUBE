package thomas.park.altube

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private val fragmentManager = supportFragmentManager
    private val fragmentHome = FragmentHome()
    private val fragmentHot = FragmentHot()
    private val fragmentSubscribe = FragmentSubscribe()
    private val fragmentLetterBox = FragmentLetterBox()
    private val fragmentCubbyhole = FragmentCubbyhole()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<MotionLayout>(R.id.mainLayout)

        val imageClear = findViewById<ImageView>(R.id.image_clear)
        val topImageContainer = findViewById<ConstraintLayout>(R.id.top_image_container)
        //topImageContainer.setOnTouchListener(OnSwipeTouchListener(this@MainActivity, mainLayout))

/*        imageClear.setOnClickListener {
            Log.e(TAG, "asdsadasdsadasdsadas")
            when (mainLayout.currentState) {
                R.id.end -> {
                    mainLayout.setTransition(R.id.end, R.id.start)
                    mainLayout.transitionToEnd()
                }
            }
        }*/


        val recyclerViewFront = findViewById<RecyclerView>(R.id.recyclerview_front)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottomNavigationView.selectedItemId = R.id.navigation_home


        recyclerViewFront.apply {
            adapter = PhotosAdapter(this@MainActivity, mainLayout)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    inner class MenuItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = fragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.drawable.ic_home_red_24dp)
                    transaction.replace(R.id.container_bottom_menu, fragmentHome)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_hot -> {
                    item.setIcon(R.drawable.ic_whatshot_red_24dp)
                    transaction.replace(R.id.container_bottom_menu, fragmentHot)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_subscribe -> {
                    transaction.replace(R.id.container_bottom_menu, fragmentSubscribe)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_letter_box -> {
                    transaction.replace(R.id.container_bottom_menu, fragmentLetterBox)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_cubby_hole -> {
                    transaction.replace(R.id.container_bottom_menu, fragmentCubbyhole)
                        .commitAllowingStateLoss()
                }
            }
            return true
        }
    }
}
