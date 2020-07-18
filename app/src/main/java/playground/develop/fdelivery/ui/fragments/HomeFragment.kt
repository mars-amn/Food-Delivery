package playground.develop.fdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.CategoryAdapter
import playground.develop.fdelivery.adapters.ProductsAdapter
import playground.develop.fdelivery.databinding.FragmentHomeBinding
import playground.develop.fdelivery.viewmodel.AppViewModel

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val mAppViewModel: AppViewModel by viewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        loadCategories()
        loadProducts()
        return mBinding.root
    }

    private fun loadProducts() {
        mAppViewModel.loadProducts().observe(this, Observer { products ->
            val productsAdapter = ProductsAdapter(context!!, products)
            //   val wrapper = InfiniteScrollAdapter.wrap(productsAdapter)
            mBinding.productsRecyclerView.apply {
                setSlideOnFling(true)
                setItemTransformer(ScaleTransformer.Builder().setMaxScale(1.05f).setMinScale(0.8f)
                                       .build())
                adapter = AlphaInAnimationAdapter(productsAdapter).apply {
                    setDuration(1000)
                }
            }
        })
    }

    private fun loadCategories() {
        mAppViewModel.loadCategories().observe(this, Observer { list ->
            val categoriesAdapter = CategoryAdapter(context!!, list)
            mBinding.categoryRecyclerView.apply {
                adapter = AlphaInAnimationAdapter(categoriesAdapter).apply {
                    setDuration(1000)
                }
                layoutManager =
                    LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)

            }
        })
    }
}