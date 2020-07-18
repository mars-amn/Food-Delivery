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
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import com.transitionseverywhere.extra.Scale
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.databinding.ActivityProductDetailsBinding
import playground.develop.fdelivery.utils.Extensions.short
import playground.develop.fdelivery.viewmodel.FavoriteViewModel

class ProductDetailsActivity : TransformationAppCompatActivity() {

    private val mFavoriteViewModel: FavoriteViewModel by viewModel()
    private lateinit var mBinding: ActivityProductDetailsBinding
    private lateinit var mProduct: Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        mBinding.productHandlers = this
        mProduct = intent.getParcelableExtra("product")!!
        mBinding.productTotalPrice.text = getString(R.string.total_price, mProduct.price.toString())
        showFullscreen()
    }

    fun onMinusClick(v: View) {
        val count = getCounterCount() - 1
        val textCount = if (count <= 0) {
            "1"
        } else {
            "$count"
        }
        updateCartCounter(textCount)
        updatePrice(getCounterCount())
    }

    private fun updateCartCounter(textCount: String) {
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
            mBinding.productTotalPrice.text =
                getString(R.string.total_price, mProduct.price.toString())
            return
        }
        val totalPrice = (count * mProduct.price)
        mBinding.productTotalPrice.apply {
            visibility = View.GONE
            val set = TransitionSet().addTransition(Scale(0.7f)).addTransition(Fade())
                .setInterpolator(LinearOutSlowInInterpolator())
            TransitionManager.beginDelayedTransition(mBinding.totalPriceParent, set)
            text = getString(R.string.total_price, totalPrice.toString())
            visibility = View.VISIBLE
        }


    }

    private fun getCounterCount(): Int {
        return mBinding.counterText.text.toString().toInt()
    }

    fun onPlusClick(v: View) {
        val count = getCounterCount() + 1
        val text = "$count"
        updateCartCounter(text)
        updatePrice(getCounterCount())
    }

    fun onBackPress(v: View) {
        onBackPressed()
    }

    fun onFavoriteProduct(v: View) {
        val favProducts =
            FavProducts(mProduct.name, mProduct.description, mProduct.image, mProduct.price, mProduct.code)

        mFavoriteViewModel.insertFavoriteProduct(favProducts).observe(this, Observer { id ->
            short(this, "$id")
        })
    }

    private fun showFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}