package playground.develop.fdelivery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.transformationlayout.TransformationCompat
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.databinding.ListItemProductsBinding
import playground.develop.fdelivery.ui.activities.ProductDetailsActivity

class ProductsAdapter(private val mContext: Context, private val mProducts: List<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ProductsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        val inflater = LayoutInflater.from(mContext)
        val binding = ListItemProductsBinding.inflate(inflater, parent, false)
        return ProductsVH(binding)
    }

    override fun getItemCount(): Int = mProducts.size

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        holder.bind(mProducts[position])
    }

    inner class ProductsVH(private val mBinding: ListItemProductsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.productHandlers = this
        }

        fun onProductClick(v: View) {
            val intent = Intent(mContext, ProductDetailsActivity::class.java)
            intent.putExtra("product", mProducts[adapterPosition])
            TransformationCompat.startActivity(mBinding.transformationLayout, intent)
        }

        fun bind(product: Product) {
            mBinding.product = product
        }
    }


}