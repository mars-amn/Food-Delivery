package playground.develop.fdelivery.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets.Type.statusBars
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import com.tapadoo.alerter.Alerter
import com.transitionseverywhere.extra.Scale
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.local.favorite.FavProducts
import playground.develop.fdelivery.database.remote.Product
import playground.develop.fdelivery.databinding.ActivityProductDetailsBinding
import playground.develop.fdelivery.ui.analytics.AnalyticLogger
import playground.develop.fdelivery.utils.Extensions.short
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel
import java.text.DecimalFormat

class ProductDetailsActivity : TransformationAppCompatActivity() {

    private val mFavoriteViewModel: LocalDatabaseViewModel by viewModel()
    private lateinit var mBinding: ActivityProductDetailsBinding
    private lateinit var mProduct: Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        mBinding.productHandlers = this
        mProduct = intent.getParcelableExtra("product")!!
        setProductDetails()
        showFullscreen()
    }

    fun onMinusClick(v: View) {
        val count = getCounterCount() - 1
        val textCount = if (count <= 0) {
            "1"
        } else {
            "$count"
        }
        setCounterTextAndApplyAnimation(textCount)
        updatePrice(getCounterCount())
    }

    private fun setCounterTextAndApplyAnimation(textCount: String) {
        mBinding.counterText.apply {
            visibility = View.GONE
            val set = TransitionSet().addTransition(Scale(0.7f))
                .setInterpolator(LinearOutSlowInInterpolator())
            TransitionManager.beginDelayedTransition(mBinding.cartCounterParent, set)
            text = textCount
            visibility = View.VISIBLE
        }
    }

    private fun updatePrice(count: Int) {
        if (count == 0) {
            mBinding.productTotalPrice.text = getProductPriceFormatted()
            return
        }
        val totalPrice = (count * mProduct.price)
        setTotalPriceTextAndApplyAnimation(totalPrice)
    }

    private fun setProductDetails() {
        mBinding.productName.text = mProduct.name
        mBinding.productImage.load(mProduct.image) { crossfade(true) }
        mBinding.descriptionText.text = mProduct.description
        mBinding.productTotalPrice.text = getProductPriceFormatted()
    }

    private fun getProductPriceFormatted() =
        getString(R.string.total_price, mProduct.price.toString())

    private fun setTotalPriceTextAndApplyAnimation(totalPrice: Float) {
        mBinding.productTotalPrice.apply {
            visibility = View.GONE
            val set = TransitionSet().addTransition(Scale(0.7f))
                .addTransition(Fade())
                .setInterpolator(LinearOutSlowInInterpolator())
            TransitionManager.beginDelayedTransition(mBinding.totalPriceParent, set)
            text = getString(R.string.total_price, DecimalFormat().format(totalPrice))
            visibility = View.VISIBLE
        }
    }

    private fun getCounterCount(): Int {
        return mBinding.counterText.text.toString().toInt()
    }

    fun onPlusClick(v: View) {
        val count = getCounterCount() + 1
        val text = "$count"
        setCounterTextAndApplyAnimation(text)
        updatePrice(getCounterCount())
    }

    fun onBackPress(v: View) {
        onBackPressed()
    }

    fun onFavoriteProduct(v: View) {
        addProductToFavorite(getProductAsFavorite())
    }

    private fun addProductToFavorite(product: FavProducts) {
        mFavoriteViewModel.addProductToFavorite(product).observe(this, Observer { id ->
            short(this, "$id")
        })
    }

    fun onAddCartClick(v: View) {
        logProductAddedToCart()
        addProductToCart()
    }

    private fun addProductToCart() {
        mFavoriteViewModel.addToCart(getProductAsCartProduct()).observe(this, Observer { id ->
            showAddedCartAlerter()
        })
    }

    private fun showAddedCartAlerter() {
        Alerter.create(this)
            .setTitle(getString(R.string.alerter_added_cart_title))
            .setIcon(R.drawable.ic_shopping_basket)
            .setBackgroundColorRes(R.color.alerterBackground)
            .setDuration(2000)
            .show()
    }

    private fun getProductAsCartProduct() = CartProducts(mProduct.name,
            mProduct.description,
            mProduct.price,
            mProduct.code.toLong(),
            mProduct.image,
            getCounterCount())

    private fun logProductAddedToCart() {
        AnalyticLogger.onUserClickOnAddToCart()
    }

    private fun getProductAsFavorite(): FavProducts = FavProducts(mProduct.name,
            mProduct.description,
            mProduct.price,
            mProduct.code.toLong(),
            mProduct.image)

    @Suppress("deprecation")
    private fun showFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}