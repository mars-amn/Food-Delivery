package playground.develop.fdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.CartAdapter
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.databinding.FragmentCartBinding
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel

class CartFragment : Fragment(), CartAdapter.CartClickListener {
    private lateinit var mBinding: FragmentCartBinding
    private val mDatabaseViewModel: LocalDatabaseViewModel by viewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        loadCartProducts()
        return mBinding.root
    }

    private fun loadCartProducts() {
        mDatabaseViewModel.getCart().observe(this, Observer { list ->
            if (list.isEmpty() || list == null) {
                // todo: show empty state
            } else {
                setCartProductsAdapter(list)
            }
        })
    }

    private fun setCartProductsAdapter(list: PagedList<CartProducts>) {
        val cartAdapter = CartAdapter(this, context!!)
        cartAdapter.submitList(list)
        mBinding.cartProductsRecyclerView.adapter = cartAdapter
    }

    override fun onCountPlusClick(product: CartProducts) {
        val count = product.count + 1
        product.count = count

        mDatabaseViewModel.updateProductInCart(product)
    }

    override fun onCountMinusClick(product: CartProducts) {
        if (product.count == 1) {
            return
        }
        val count = product.count - 1
        product.count = count
        mDatabaseViewModel.updateProductInCart(product)
    }
}