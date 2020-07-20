package playground.develop.fdelivery.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Product(@DrawableRes val image: Int, val name: String, val description: String,
    val price: Float,
    /**
     * should be unique code that identifies the product & its details
     */
    val code: Long) : Parcelable