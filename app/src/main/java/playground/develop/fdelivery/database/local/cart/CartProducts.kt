package playground.develop.fdelivery.database.local.cart

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CartProducts")
data class CartProducts(@ColumnInfo(name = "product_name") var productName: String,
    @ColumnInfo(name = "product_description") var productDescription: String,
    @ColumnInfo(name = "product_price") var productPrice: Float,
    @PrimaryKey @ColumnInfo(name = "product_code") var code: Long,
    @ColumnInfo(name = "product_image") var productImage: String,
    @ColumnInfo(name = "product_count") var count: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CartProducts) return false
        return productName == other.productName && code == other.code
    }

    override fun hashCode(): Int {
        var result = productDescription.hashCode()
        result = 31 * result + (productName.hashCode())
        result = 31 * result + (productImage.hashCode())
        result = 31 * result + (productPrice.hashCode())
        result = 31 * result + (code.hashCode())
        return result
    }
}