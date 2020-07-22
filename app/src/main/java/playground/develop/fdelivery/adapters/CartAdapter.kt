package playground.develop.fdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.databinding.ListItemCartBinding

class CartAdapter(val mListener: CartClickListener, private val mContext: Context) :
    PagedListAdapter<CartProducts, CartAdapter.CartProductsVH>(CartProductsDiffCallback()) {

    interface CartClickListener {
        fun onCountPlusClick(product: CartProducts)
        fun onCountMinusClick(product: CartProducts)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.code!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsVH {
        val inflater = LayoutInflater.from(mContext)
        val binding = ListItemCartBinding.inflate(inflater, parent, false)
        return CartProductsVH(binding)
    }

    override fun onBindViewHolder(holder: CartProductsVH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CartProductsVH(private val mBinding: ListItemCartBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.cartHandlers = this
        }

        fun bind(product: CartProducts) {
            mBinding.cartProduct = product
        }

        fun onCartPlusClick(v: View) {
            mListener.onCountPlusClick(getItem(adapterPosition)!!)
        }

        fun onCartMinusClick(v: View) {
            mListener.onCountMinusClick(getItem(adapterPosition)!!)
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

