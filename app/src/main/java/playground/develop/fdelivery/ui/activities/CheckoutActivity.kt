package playground.develop.fdelivery.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.CheckoutAdapter
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.databinding.ActivityCheckoutBinding
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel
import java.text.DecimalFormat

class CheckoutActivity : AppCompatActivity() {
    private val mDatabaseViewModel: LocalDatabaseViewModel by viewModel()
    private lateinit var mBinding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)
        mBinding.deliveryHandlers = this
        loadCartProducts()
    }

    private fun loadCartProducts() {
        mDatabaseViewModel.getCart().observe(this, Observer { list ->
            if (list.isNotEmpty() || list != null) {
                addAdapterToRecyclerView(list)
            } else {
                finish()

            }
        })
    }

    private fun addAdapterToRecyclerView(list: PagedList<CartProducts>) {
        calculateBill(list)
        val adapter = CheckoutAdapter(this)
        adapter.submitList(list)
        mBinding.checkoutProductsRecyclerView.adapter = adapter
    }

    private fun calculateBill(list: PagedList<CartProducts>) {
        var total = 0f
        list.forEach { product ->
            total += (product.productPrice * product.count)
        }
        mBinding.paymentPriceText.text =
            getString(R.string.cart_total_text, DecimalFormat().format(total))
    }

    fun onCashSelect(v: View) {
        if (isCreditSelect()) {
            setCashChecked()
        }
    }

    fun onCreditCardSelect(v: View) {
        if (isCashSelect()) {
            setCreditCardChecked()
        }
    }

    private fun setCashChecked() {
        mBinding.paymentCashRadio.isChecked = true
        mBinding.paymentCreditCardRadio.isChecked = false
    }

    private fun setCreditCardChecked() {
        mBinding.paymentCashRadio.isChecked = false
        mBinding.paymentCreditCardRadio.isChecked = true
    }

    private fun isCreditSelect(): Boolean = mBinding.paymentCreditCardRadio.isChecked
    private fun isCashSelect(): Boolean = mBinding.paymentCashRadio.isChecked

}