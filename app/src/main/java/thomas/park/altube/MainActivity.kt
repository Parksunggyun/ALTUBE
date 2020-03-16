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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }



    private val fragmentHome = FragmentHome()
    private val fragmentHot = FragmentHot()
    private val fragmentSubscribe = FragmentSubscribe()
    private val fragmentLetterBox = FragmentLetterBox()
    private val fragmentCubbyhole = FragmentCubbyhole()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottom_nav.selectedItemId = R.id.navigation_home

        recyclerview_front.apply {
            adapter = PhotosAdapter(this@MainActivity, mainLayout)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    inner class MenuItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = supportFragmentManager.beginTransaction()
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
