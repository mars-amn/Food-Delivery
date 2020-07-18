package playground.develop.fdelivery.database.locale.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @PrimaryKey would be the product code in the future
 */
@Entity(tableName = "FavoriteProducts")
data class FavProducts(@ColumnInfo(name = "product_name") var productName: String,
                       @ColumnInfo(name = "product_description") var productDescription: String,
                       @ColumnInfo(name = "product_image") var productImage: Int,
                       @ColumnInfo(name = "product_price") var productPrice: Float,
                       @PrimaryKey @ColumnInfo(name = "product_code") var code: Long)