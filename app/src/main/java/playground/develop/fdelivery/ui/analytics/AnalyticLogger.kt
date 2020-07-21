package playground.develop.fdelivery.ui.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.core.KoinComponent
import org.koin.core.inject

object AnalyticLogger : KoinComponent {
    private val mAnalytics: FirebaseAnalytics by inject()

    fun onUserClickOnProduct() {
        mAnalytics.logEvent(USER_CLICK_ON_PRODUCT, Bundle().apply {
            putBoolean(USER_CLICK_ON_PRODUCT, true)
        })
    }

    fun onUserClickOnAddToCart() {
        mAnalytics.logEvent(USER_CLICK_ON_ADD_TO_CART, Bundle().apply {
            putBoolean(USER_CLICK_ON_ADD_TO_CART, true)
        })
    }

    private const val USER_CLICK_ON_PRODUCT = "user_click_product"
    private const val USER_CLICK_ON_ADD_TO_CART = "user_click_add_to_cart"
}