package playground.develop.fdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playground.develop.fdelivery.data.Category
import playground.develop.fdelivery.databinding.ListItemCategoryBinding

class CategoryAdapter(private val mContext: Context, private var mCategories: List<Category>) :
        RecyclerView.Adapter<CategoryAdapter.CategoriesVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesVH {
        val inflater = LayoutInflater.from(mContext)
        val binding = ListItemCategoryBinding.inflate(inflater, parent, false)
        return CategoriesVH(binding)
    }

    override fun getItemCount(): Int = mCategories.size

    override fun onBindViewHolder(holder: CategoriesVH, position: Int) {
        holder.bind(mCategories[position])
    }

    inner class CategoriesVH(
            private val mBinding: ListItemCategoryBinding) : RecyclerView.ViewHolder(mBinding.root) {


        fun bind(category: Category) {
            mBinding.category = category
        }

    }
}