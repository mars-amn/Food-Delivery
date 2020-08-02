package playground.develop.fdelivery.database.remote

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import playground.develop.fdelivery.database.local.cart.CartProducts
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Order(var products: List<CartProducts>, val name: String, var address: String,
    var paymentType: String, var phone: String, var status: String,
    private var dateCreated: Timestamp? = null, var orderId: String) : Parcelable {
    constructor() : this(ArrayList<CartProducts>(), "", "", "", "", "", Timestamp(Date()), "")

    fun getDateCreated(): Date {
        return dateCreated!!.toDate()
    }

    fun setDateCreated(date: Timestamp) {
        this.dateCreated = date
    }
}