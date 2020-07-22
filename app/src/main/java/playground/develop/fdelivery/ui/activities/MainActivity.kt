package playground.develop.fdelivery.ui.activities

import android.content.Intent
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
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.transitionseverywhere.extra.Scale
import playground.develop.fdelivery.R
import playground.develop.fdelivery.databinding.ActivityMainBinding
import playground.develop.fdelivery.ui.analytics.AnalyticLogger
import playground.develop.fdelivery.ui.fragments.CartFragment
import playground.develop.fdelivery.ui.fragments.FavoriteFragment
import playground.develop.fdelivery.ui.fragments.HomeFragment


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

    @Suppress("deprecation")
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
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    private fun onBottomNavClick() {
        mBinding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> showFragment(HomeFragment(), HOME_FRAGMENT)
                R.id.action_favorites -> showFragment(FavoriteFragment(), OTHER_FRAGMENT)
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment, fragmentName: String) {
        if (fragment is CartFragment) {
            showCheckoutButton()
        } else {
            hideCheckoutButton()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.mainContainer, fragment)

        val count: Int = fragmentManager.backStackEntryCount
        if (fragmentName == OTHER_FRAGMENT) {
            fragmentTransaction.addToBackStack(fragmentName)
        }
        fragmentTransaction.commit()
        handleOnBackStackChangedCallback(fragmentManager, count)

    }

    private fun handleOnBackStackChangedCallback(fragmentManager: FragmentManager, count: Int) {
        fragmentManager.addOnBackStackChangedListener(object :
            FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                if (fragmentManager.backStackEntryCount <= count) {
                    fragmentManager.popBackStack(OTHER_FRAGMENT, POP_BACK_STACK_INCLUSIVE)
                    fragmentManager.removeOnBackStackChangedListener(this)
                    mBinding.bottomNavBar.menu.getItem(0).isChecked = true
                }
            }
        })
    }

    fun onCartButtonClick(v: View) {
        showFragment(CartFragment(), OTHER_FRAGMENT)
    }

    private fun showCheckoutButton() {
        mBinding.cartButton.visibility = View.GONE
        mBinding.checkoutText.apply {
            visibility = View.GONE
            applyAnimationOnMainParent()
            visibility = View.VISIBLE
        }
    }

    private fun hideCheckoutButton() {
        mBinding.checkoutText.visibility = View.GONE
        mBinding.cartButton.apply {
            visibility = View.GONE
            applyAnimationOnMainParent()
            visibility = View.VISIBLE
        }
    }

    private fun applyAnimationOnMainParent() {
        val set = TransitionSet().addTransition(Scale(0.7f))
            .addTransition(Fade())
            .setInterpolator(LinearOutSlowInInterpolator())
        TransitionManager.beginDelayedTransition(mBinding.mainActivityParent, set)
    }

    fun onCheckoutClick(v: View) {
        AnalyticLogger.onUserClickOnCheckout()
        startActivity(Intent(this, CheckoutActivity::class.java))
    }

    fun onNavDrawerClick(v: View) {
        mBinding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        when {
            mBinding.drawerLayout.isDrawerOpen(GravityCompat.START) -> mBinding.drawerLayout.closeDrawer(
                    GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    companion object {
        const val HOME_FRAGMENT = "HomeFragment"
        const val OTHER_FRAGMENT = "OtherFragment"
    }
}