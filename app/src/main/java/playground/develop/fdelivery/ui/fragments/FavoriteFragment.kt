package playground.develop.fdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import org.koin.android.ext.android.inject
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.FavoriteProductsAdapter
import playground.develop.fdelivery.databinding.FragmentFavoriteProductsBinding
import playground.develop.fdelivery.viewmodel.FavoriteProductsViewModel

class FavoriteFragment : Fragment() {

    private lateinit var mBinding: FragmentFavoriteProductsBinding
    private val mFavoriteViewModel: FavoriteProductsViewModel by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_products, container, false)
        loadFavoriteProducts()
        return mBinding.root
    }

    private fun loadFavoriteProducts() {
        mFavoriteViewModel.getFavoriteProducts().observe(this, Observer { list ->
            if (list.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                val favoriteAdapter = FavoriteProductsAdapter(context!!)
                favoriteAdapter.submitList(list)
                mBinding.favoriteProductsRecyclerView.adapter =
                    ScaleInAnimationAdapter(favoriteAdapter).apply {
                        setDuration(700)
                        setFirstOnly(false)
                    }

            }
        })
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