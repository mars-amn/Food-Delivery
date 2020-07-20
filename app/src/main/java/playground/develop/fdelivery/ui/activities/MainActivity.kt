package playground.develop.fdelivery.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.skydoves.transformationlayout.onTransformationStartContainer
import playground.develop.fdelivery.R
import playground.develop.fdelivery.databinding.ActivityMainBinding
import playground.develop.fdelivery.ui.fragments.FavoriteFragment
import playground.develop.fdelivery.ui.fragments.HomeFragment
import playground.develop.fdelivery.utils.Extensions.short


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.mainHandlers = this
        applyRadiusToNavView()
        showFragment(HomeFragment(), HOME_FRAGMENT)
        onBottomNavClick()
        showFullscreen()
    }

    private fun showFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
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
                R.id.action_home -> showFragment(HomeFragment(), HOME_FRAGMENT)
                R.id.action_favorites -> showFragment(FavoriteFragment(), FAVORITE_FRAGMENT)
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment, fragmentName: String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainContainer, fragment)
        // 1. Know how many fragments there are in the stack
        // 1. Know how many fragments there are in the stack
        val count: Int = fragmentManager.backStackEntryCount
        // 2. If the fragment is **not** "home type", save it to the stack
        // 2. If the fragment is **not** "home type", save it to the stack
        if (fragmentName == FAVORITE_FRAGMENT) {
            fragmentTransaction.addToBackStack(fragmentName)
        }
        // Commit !
        // Commit !
        fragmentTransaction.commit()
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(object :
            FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (fragmentManager.backStackEntryCount <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(FAVORITE_FRAGMENT, POP_BACK_STACK_INCLUSIVE)
                    fragmentManager.removeOnBackStackChangedListener(this)
                    // set the home button selected
                    mBinding.bottomNavBar.menu.getItem(0).isChecked = true
                }
            }
        })
    }

    fun onCartButtonClick(v: View) {
        short(this, "Cart click")
    }

    fun onNavDrawerClick(v: View) {
        mBinding.drawerLayout.openDrawer(GravityCompat.START)
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            count > 0 -> supportFragmentManager.popBackStack()
            mBinding.drawerLayout.isDrawerOpen(
                GravityCompat.START) -> mBinding.drawerLayout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    companion object {
        const val HOME_FRAGMENT = "HomeFragment"
        const val FAVORITE_FRAGMENT = "FavoriteFragment"
    }
}