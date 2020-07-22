package playground.develop.fdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.databinding.ListItemCartCheckoutBinding

class CheckoutAdapter(private val mContext: Context) :
    PagedListAdapter<CartProducts, CheckoutAdapter.CheckoutVH>(CartProductsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutVH {
        val inflater = LayoutInflater.from(mContext)
        val binding = ListItemCartCheckoutBinding.inflate(inflater, parent, false)
        return CheckoutVH(binding)
    }

    override fun onBindViewHolder(holder: CheckoutVH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CheckoutVH(private val mBinding: ListItemCartCheckoutBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(product: CartProducts) {
            mBinding.cartProduct = product
        }


    }

    class CartProductsDiffCallback : DiffUtil.ItemCallback<CartProducts>() {
        override fun areItemsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem == newItem
        }
    }
}