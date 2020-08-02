package playground.develop.fdelivery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.OrdersAdapter
import playground.develop.fdelivery.databinding.FragmentUserOrdersBinding
import playground.develop.fdelivery.viewmodel.AppViewModel

class UserOrdersFragment : Fragment() {

    private val mAuth: FirebaseAuth by inject()
    private val mViewModel: AppViewModel by viewModel()
    private lateinit var mBinding: FragmentUserOrdersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_orders, container, false)
        loadUserOrders()
        return mBinding.root
    }

    private fun loadUserOrders() {
        if (mAuth.currentUser == null) {
            return
        } else {
            mViewModel.getUserOrders(mAuth.currentUser?.uid).observe(this, Observer { orders ->
                if (orders != null) {
                    val adapter = OrdersAdapter(context!!, orders)
                    mBinding.userOrdersRecyclerView.adapter = adapter
                }
            })
        }
    }
}