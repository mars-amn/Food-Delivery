package playground.develop.fdelivery.database.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(var name: String, var description: String, var price: Float, var code: Int,
    var image: String) : Parcelable {
    constructor() : this("", "", 0f, 0, "")
}