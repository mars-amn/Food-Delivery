package playground.develop.fdelivery.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playground.develop.fdelivery.R
import playground.develop.fdelivery.database.remote.Order
import playground.develop.fdelivery.databinding.ListItemOrdersBinding
import java.text.DecimalFormat

class OrdersAdapter(private val mContext: Context, private val mOrders: List<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrdersVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersVH {
        val inflater = LayoutInflater.from(mContext)
        val binding = ListItemOrdersBinding.inflate(inflater, parent, false)
        return OrdersVH(binding)
    }

    override fun getItemCount(): Int = mOrders.size

    override fun onBindViewHolder(holder: OrdersVH, position: Int) {
        holder.bind(mOrders[position])
    }

    inner class OrdersVH(private val mBinding: ListItemOrdersBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(order: Order) {
            mBinding.order = order
            setOrderDate(order)
            setOrderPrice(order)
            setOrderName(order)
        }

        private fun setOrderName(order: Order) {
            val id = order.orderId.filter { it.isDigit() }
            mBinding.listItemUserOrderId.text = id
        }

        private fun setOrderPrice(order: Order) {
            var total = 0f
            order.products.forEach { product ->
                total += (product.productPrice * product.count)
            }
            mBinding.listItemUserOrderTotalPrice.text =
                mContext.getString(R.string.cart_total_text, DecimalFormat().format(total))
        }

        private fun setOrderDate(order: Order) {
            mBinding.listItemUserOrderDate.text =
                DateFormat.format("EEEE, MMM yyyy ", order.getDateCreated())
        }
    }


}