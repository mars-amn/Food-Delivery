package playground.develop.fdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.databinding.ListItemProductsBinding

class ProductsAdapter(private val mListener: ProductClickListener, private val mContext: Context,
    private val mProducts: List<Product>) : RecyclerView.Adapter<ProductsAdapter.ProductsVH>() {
    interface ProductClickListener {
        fun onProductClick(product: Product)
        fun onCartClick(product: Product)
    }

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

        fun onAddToCartClick(v: View) {
            mListener.onCartClick(mProducts[adapterPosition])

        }

        fun onProductClick(v: View) {
            mListener.onProductClick(mProducts[adapterPosition])
        }

        fun bind(product: Product) {
            mBinding.product = product
        }
    }


}