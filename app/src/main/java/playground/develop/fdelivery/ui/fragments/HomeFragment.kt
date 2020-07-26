package playground.develop.fdelivery.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mancj.materialsearchbar.MaterialSearchBar
import com.skydoves.transformationlayout.TransformationCompat
import com.tapadoo.alerter.Alerter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.CategoryAdapter
import playground.develop.fdelivery.adapters.ProductsAdapter
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.remote.Product
import playground.develop.fdelivery.databinding.FragmentHomeBinding
import playground.develop.fdelivery.ui.activities.ProductDetailsActivity
import playground.develop.fdelivery.ui.analytics.AnalyticLogger
import playground.develop.fdelivery.viewmodel.AppViewModel
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel

class HomeFragment : Fragment(), ProductsAdapter.ProductClickListener,
    CategoryAdapter.CategoryClickListener, MaterialSearchBar.OnSearchActionListener {

    private lateinit var mBinding: FragmentHomeBinding
    private val mAppViewModel: AppViewModel by viewModel()
    private val mDatabaseViewModel: LocalDatabaseViewModel by viewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mBinding.handlers = this
        mBinding.searchBar.setOnSearchActionListener(this)
        loadCategories()
        loadProducts("pizza")
        return mBinding.root
    }

    private fun loadProducts(category: String) {
        mAppViewModel.loadProducts(category).observe(this, Observer { products ->
            setProductsAdapter(ProductsAdapter(this, context!!, products))
        })
    }

    override fun onCategoryClick(category: String) {
        mAppViewModel.loadProducts(category).observe(this, Observer { products ->
            setProductsAdapter(ProductsAdapter(this, context!!, products))
        })
    }

    private fun setProductsAdapter(productsAdapter: ProductsAdapter) {
        mBinding.productsRecyclerView.apply {
            setSlideOnFling(true)
            setItemTransformer(ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .build())
            adapter = AlphaInAnimationAdapter(productsAdapter).apply {
                setDuration(1000)
            }
        }
    }

    private fun loadCategories() {
        mAppViewModel.loadCategories().observe(this, Observer { list ->
            setCategoriesAdapter(CategoryAdapter(this@HomeFragment, context!!, list))
        })
    }

    private fun setCategoriesAdapter(categoriesAdapter: CategoryAdapter) {
        mBinding.categoryRecyclerView.apply {
            adapter = AlphaInAnimationAdapter(categoriesAdapter).apply {
                setDuration(1000)
            }
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onProductClick(product: Product) {
        logProductClicked()
        startProductDetailsActivity(product)
    }

    private fun logProductClicked() {
        AnalyticLogger.onUserClickOnProduct()
    }

    private fun startProductDetailsActivity(product: Product) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        TransformationCompat.startActivity(mBinding.transformationLayout, intent)
    }

    override fun onCartClick(product: Product) {
        logProductAddedToCart()
        addProductToCart(getProductAsCartProduct(product))
    }

    private fun addProductToCart(cartProduct: CartProducts) {
        mDatabaseViewModel.addToCart(cartProduct).observe(this, Observer { code ->
            showAddToCartAlerter()
        })
    }

    private fun logProductAddedToCart() {
        AnalyticLogger.onUserClickOnAddToCart()
    }

    private fun showAddToCartAlerter() {
        Alerter.create(activity)
            .setTitle(getString(R.string.alerter_added_cart_title))
            .setIcon(R.drawable.ic_shopping_basket)
            .setBackgroundColorRes(R.color.alerterBackground)
            .setDuration(2000)
            .show()
    }

    private fun getProductAsCartProduct(product: Product) = CartProducts(product.name,
            product.description,
            product.price,
            product.code.toLong(),
            product.image,
            1)

    fun onFilterClick(v: View) {
        showSelectionDialog()
    }

    private var categorySelectedPosition = -1
    private var mSearchCriteria = "pizza"

    private fun getSelectedCategory(): String =
        resources.getStringArray(R.array.food_categories)[categorySelectedPosition].toLowerCase()

    private fun showSelectionDialog() {
        MaterialAlertDialogBuilder(context!!).setTitle(getString(R.string.select_category_search_filter_title))
            .setSingleChoiceItems(resources.getStringArray(R.array.food_categories),
                    if (categorySelectedPosition == -1) 0 else categorySelectedPosition) { dialog, which ->
                categorySelectedPosition = which
                mSearchCriteria = getSelectedCategory()
            }
            .setPositiveButton(getString(R.string.select_category_search_filter_button_label)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onButtonClicked(buttonCode: Int) {

    }

    override fun onSearchStateChanged(enabled: Boolean) {
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        mAppViewModel.searchFor(text.toString().toLowerCase(), mSearchCriteria)
            .observe(this, Observer { results ->
                if (results.isNotEmpty() && results != null) {
                    setProductsAdapter(ProductsAdapter(this, context!!, results))
                }
            })
    }


}