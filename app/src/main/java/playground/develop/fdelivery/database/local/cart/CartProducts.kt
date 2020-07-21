package playground.develop.fdelivery.database.local.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProducts")
data class CartProducts(@ColumnInfo(name = "product_name") var productName: String,
    @ColumnInfo(name = "product_price") var productPrice: Float,
    @ColumnInfo(name = "product_image") var productImage: Int,
    @ColumnInfo(name = "product_description") var productDescription: String,
    @ColumnInfo(name = "product_count") var count: Int,
    @PrimaryKey @ColumnInfo(name = "product_code") var code: Long)