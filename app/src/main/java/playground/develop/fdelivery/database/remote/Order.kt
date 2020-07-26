package playground.develop.fdelivery.database.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import playground.develop.fdelivery.database.local.cart.CartProducts

@Parcelize
data class Order(var products: List<CartProducts>, val name: String, var address: String,
    var status: String,

    var orderId: String) : Parcelable {
    constructor() : this(ArrayList<CartProducts>(), "", "", "", "")
}