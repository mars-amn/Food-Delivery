package playground.develop.fdelivery.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tapadoo.alerter.Alerter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import playground.develop.fdelivery.R
import playground.develop.fdelivery.adapters.CheckoutAdapter
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.remote.Order
import playground.develop.fdelivery.databinding.ActivityCheckoutBinding
import playground.develop.fdelivery.utils.Constants.ORDER_STATUS_PROCESS
import playground.develop.fdelivery.utils.Constants.PAYMENT_TYPE_CASH
import playground.develop.fdelivery.utils.Constants.PAYMENT_TYPE_CREDIT_CARD
import playground.develop.fdelivery.utils.Extensions.long
import playground.develop.fdelivery.viewmodel.AppViewModel
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel
import java.text.DecimalFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private val mDatabaseViewModel: LocalDatabaseViewModel by viewModel()
    private val mAppViewModel: AppViewModel by viewModel()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val mAuth: FirebaseAuth by inject()
    private lateinit var gso: GoogleSignInOptions

    private lateinit var mBinding: ActivityCheckoutBinding
    private lateinit var mProducts: List<CartProducts>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)
        mBinding.deliveryHandlers = this
        loadCartProducts()
        initGso()
        initClient()
    }

    private fun initGso() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client))
            .requestEmail()
            .build()
    }

    private fun initClient() {
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    fun onOrderClick(v: View) {
        if (::mProducts.isInitialized) {
            if (isInputsAreValid()) {
                if (mAuth.currentUser == null) {
                    createAuthRequiredDialog()
                } else {
                    submitOrder()
                }
            } else {
                createEmptyAlerterMsg()
            }
        }
    }

    private fun createEmptyAlerterMsg() {
        Alerter.create(this)
            .setTitle(getString(R.string.inputs_empty_alerter_title))
            .setText(getString(R.string.inputs_empty_alerter_msg))
            .setIcon(R.drawable.ic_information)
            .setDuration(2000)
            .show()
    }

    private fun isInputsAreValid(): Boolean {
        return getDeliverUserPhone() != "" && getDeliverUserAddress() != "" && getDeliverUserName() != ""
    }


    private fun createAuthRequiredDialog() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.auth_required_dialog_title))
            .setMessage(getString(R.string.auth_required_dialog_message))
            .setPositiveButton(getString(R.string.auth_required_dialog_positive_button)) { dialog, which ->
                sendAuthIntent()
            }
            .setNegativeButton(getString(R.string.auth_required_dialog_negative_button)) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun sendAuthIntent() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REGISTER_RC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REGISTER_RC) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginUser(account.idToken!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun loginUser(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                long(this, getString(R.string.auth_success))
                submitOrder()
            } else {
                long(this, getString(R.string.auth_failed))
            }
        }
    }

    private fun submitOrder() {
        val order = createOrder()
        mAppViewModel.createOrder(order).observe(this, Observer { status ->
            if (status) createSuccessAlertDialog()
        })
    }

    private fun createSuccessAlertDialog() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.order_submit_success_title))
            .setMessage(getString(R.string.order_submit_success_message))
            .setPositiveButton(getString(R.string.order_submit_success_button)) { dialog, which ->
                dialog.dismiss()
                deleteCartTable()
            }
            .show()
    }

    private fun deleteCartTable() {
        mDatabaseViewModel.nukeCart()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun createOrder(): Order {
        return Order(mProducts,
                getDeliverUserName(),
                getDeliverUserAddress(),
                getSelectedPaymentType(),
                getDeliverUserPhone(),
                ORDER_STATUS_PROCESS,
                mAuth.currentUser?.uid + Date().time)
    }

    private fun getSelectedPaymentType(): String {
        return if (isCashSelect()) {
            PAYMENT_TYPE_CASH
        } else {
            PAYMENT_TYPE_CREDIT_CARD
        }
    }

    private fun loadCartProducts() {
        mDatabaseViewModel.getCart().observe(this, Observer { list ->
            if (list.isNotEmpty() || list != null) {
                mProducts = list
                addAdapterToRecyclerView(list)
            } else {
                finish()
            }
        })
    }

    private fun addAdapterToRecyclerView(list: PagedList<CartProducts>) {
        calculateBill(list)
        val adapter = CheckoutAdapter(this)
        adapter.submitList(list)
        mBinding.checkoutProductsRecyclerView.adapter = adapter
    }

    private fun calculateBill(list: PagedList<CartProducts>) {
        var total = 0f
        list.forEach { product ->
            total += (product.productPrice * product.count)
        }
        mBinding.paymentPriceText.text =
            getString(R.string.cart_total_text, DecimalFormat().format(total))
    }

    fun onCashSelect(v: View) {
        if (isCreditSelect()) {
            setCashChecked()
        }
    }

    fun onCreditCardSelect(v: View) {
        if (isCashSelect()) {
            setCreditCardChecked()
        }
    }

    private fun getDeliverUserName(): String =
        mBinding.deliveryDetailsNameInputField.text.toString()

    private fun getDeliverUserAddress(): String =
        mBinding.deliveryDetailsAddressInputField.text.toString()

    private fun getDeliverUserPhone(): String =
        mBinding.deliveryDetailsPhoneInputField.text.toString()

    private fun setCashChecked() {
        mBinding.paymentCashRadio.isChecked = true
        mBinding.paymentCreditCardRadio.isChecked = false
    }

    private fun setCreditCardChecked() {
        mBinding.paymentCashRadio.isChecked = false
        mBinding.paymentCreditCardRadio.isChecked = true
    }

    private fun isCreditSelect(): Boolean = mBinding.paymentCreditCardRadio.isChecked
    private fun isCashSelect(): Boolean = mBinding.paymentCashRadio.isChecked

    companion object {
        const val REGISTER_RC = 1996
    }
}