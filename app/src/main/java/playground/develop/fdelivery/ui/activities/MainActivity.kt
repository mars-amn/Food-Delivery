package playground.develop.fdelivery.ui.activities

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.skydoves.transformationlayout.onTransformationStartContainer
import playground.develop.fdelivery.R
import playground.develop.fdelivery.databinding.ActivityMainBinding
import playground.develop.fdelivery.ui.fragments.HomeFragment


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        showFullscreen()
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.mainHandlers = this
        applyRadiusToNavView()
        showHomeFragment()
        onBottomNavClick()
    }

    private fun showFullscreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun applyRadiusToNavView() {
        val radius = 70f
        val navViewBackground = mBinding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel.toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius).build()
    }

    private fun onBottomNavClick() {
        mBinding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> showHomeFragment()
                R.id.action_favorites -> showMsg("favorite clicked!")
            }
            true
        }
    }

    fun onCartButtonClick(v: View) {
        showMsg("Cart click")
    }

    fun onNavDrawerClick(v: View) {
        mBinding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun showHomeFragment() {
        val mainFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, mainFragment)
        transaction.commit()
    }

    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            count > 0 -> supportFragmentManager.popBackStack()
            mBinding.drawerLayout.isDrawerOpen(GravityCompat.START) -> mBinding.drawerLayout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }
}