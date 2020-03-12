package thomas.park.altube.youtube

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ymain.*
import thomas.park.altube.*

class YMainActivity : AppCompatActivity() {

    companion object {
        val TAG = YMainActivity::class.java.simpleName
    }

    private val fragmentHome = FragmentHome()
    private val fragmentHot = FragmentHot()
    private val fragmentSubscribe = FragmentSubscribe()
    private val fragmentLetterBox = FragmentLetterBox()
    private val fragmentCubbyhole = FragmentCubbyhole()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ymain)

        bottom_ynav.setOnNavigationItemSelectedListener(MenuItemSelectedListener())
        bottom_ynav.selectedItemId = R.id.navigation_home

        recommendListView.apply {
            adapter = PhotosAdapter(this@YMainActivity, ymainLayout)
            layoutManager = LinearLayoutManager(this@YMainActivity)
        }

        videoDetailLayout.setOnClickListener {
            if(ymainLayout.currentState == R.id.collapsed) {
                ymainLayout.transitionToState(R.id.expanded)
            }
        }

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