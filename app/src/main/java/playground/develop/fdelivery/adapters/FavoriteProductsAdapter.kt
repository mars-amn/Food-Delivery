package playground.develop.fdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.databinding.ListItemFavoriteBinding

class FavoriteProductsAdapter(private val mListener: DeletionListener,
    private val context: Context) :
    PagedListAdapter<FavProducts, FavoriteProductsAdapter.FavoriteProductsVH>(
        FavoriteProductsDiffCallback()) {
    interface DeletionListener {
        fun onFavProductLongClick(product: FavProducts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsVH {
        val inflater = LayoutInflater.from(context)
        val binding = ListItemFavoriteBinding.inflate(inflater, parent, false)
        return FavoriteProductsVH(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductsVH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class FavoriteProductsVH(private val mBinding: ListItemFavoriteBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(product: FavProducts) {
            mBinding.product = product
        }

        init {
            mBinding.handlers = this
        }

        fun onProductLongClick(v: View): Boolean {
            mListener.onFavProductLongClick(getItem(adapterPosition)!!)
            return true
        }
    }

    class FavoriteProductsDiffCallback : DiffUtil.ItemCallback<FavProducts>() {
        override fun areItemsTheSame(oldItem: FavProducts, newItem: FavProducts): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: FavProducts, newItem: FavProducts): Boolean {
            return oldItem == newItem
        }
    }


}