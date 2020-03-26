package thomas.park.altube.overlay

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bothab.view.*
import kotlinx.android.synthetic.main.activity_overlay.*
import kotlinx.android.synthetic.main.activity_ymain.*
import thomas.park.altube.*

class OverlayActivity: AppCompatActivity() {

    companion object {
        private val TAG = OverlayActivity::class.java.simpleName
    }


    private val homeFragment = HomeFragment()
    private val fragmentHot = FragmentHot()
    private val fragmentSubscribe = FragmentSubscribe()
    private val fragmentLetterBox = FragmentLetterBox()
    private val fragmentCubbyhole = FragmentCubbyhole()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_overlay)

        overlayView.setBottomNavItemTouchListener(MenuItemSelectedListener(overlayView), R.id.navigation_home)

    }

    inner class MenuItemSelectedListener(private val overlayView: OverlayMotionLayout) : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            Log.e(TAG, "setBottomNavItemTouchListener")
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.e(TAG, "navigation_home")
                    item.setIcon(R.drawable.ic_home_red_24dp)
                    homeFragment.overlayView = overlayView
                    transaction.replace(R.id.pageContainer, homeFragment)
                        .commitAllowingStateLoss()
                }
                R.id.navigation_hot -> {
                    item.setIcon(R.drawable.ic_whatshot_red_24dp)
                    transaction.replace(R.id.pageContainer, fragmentHot)
                        .commitAllowingStateLoss()
                }

                R.id.navigation_subscribe -> {
                    transaction.replace(R.id.pageContainer, fragmentSubscribe)
                        .commitAllowingStateLoss()
                }

                R.id.navigation_letter_box -> {
                    transaction.replace(R.id.pageContainer, fragmentLetterBox)
                        .commitAllowingStateLoss()
                }

                R.id.navigation_cubby_hole -> {
                    transaction.replace(R.id.pageContainer, fragmentCubbyhole)
                        .commitAllowingStateLoss()
                }
            }
            return true
        }
    }

}