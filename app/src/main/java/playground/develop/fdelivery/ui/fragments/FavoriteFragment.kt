package playground.develop.fdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.FavoriteProductsAdapter
import playground.develop.fdelivery.database.local.favorite.FavProducts
import playground.develop.fdelivery.databinding.FragmentFavoriteProductsBinding
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel

class FavoriteFragment : Fragment(), FavoriteProductsAdapter.DeletionListener {

    private lateinit var mBinding: FragmentFavoriteProductsBinding
    private val mDatabaseViewModel: LocalDatabaseViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_products, container, false)
        loadFavoriteProducts()
        return mBinding.root
    }

    private fun loadFavoriteProducts() {
        mDatabaseViewModel.getFavoriteProducts().observe(this, Observer { list ->
            if (list.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                setFavoriteProductsAdapter(list)
            }
        })
    }

    private fun setFavoriteProductsAdapter(list: PagedList<FavProducts>?) {
        val favoriteAdapter = FavoriteProductsAdapter(this@FavoriteFragment, context!!)
        favoriteAdapter.submitList(list)
        mBinding.favoriteProductsRecyclerView.adapter =
            ScaleInAnimationAdapter(favoriteAdapter).apply {
                setDuration(700)
                setFirstOnly(false)
            }
    }

    override fun onFavProductLongClick(product: FavProducts) {
        showFavoriteProductDeletionDialog(product)
    }

    private fun showFavoriteProductDeletionDialog(product: FavProducts) {
        MaterialAlertDialogBuilder(context!!).setTitle(getString(R.string.delete_fav_product_dialog_title))
            .setMessage(getString(R.string.delete_fav_product_dialog_message))
            .setPositiveButton(getString(R.string.delete_fav_product_dialog_positive_button_label)) { dialog, which ->
                mDatabaseViewModel.deleteFavoriteProduct(product)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.delete_fav_product_dialog_negative_button_label)) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun showEmptyState() {
        mBinding.emptyState.visibility = View.VISIBLE
        mBinding.favoriteProductsRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        mBinding.favoriteProductsRecyclerView.visibility = View.VISIBLE
        mBinding.emptyState.visibility = View.GONE
    }


}