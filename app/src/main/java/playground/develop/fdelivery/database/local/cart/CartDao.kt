package playground.develop.fdelivery.database.local.cart

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Single

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductToCart(products: CartProducts): Single<Long>

    @Query("SELECT * FROM CartProducts WHERE product_code =:code")
    fun getSingleProductInCart(code: Long): CartProducts

    @Query("SELECT * FROM CartProducts")
    fun getCart(): DataSource.Factory<Int, CartProducts>

    @Delete
    fun deleteProductOfCart(product: CartProducts): Int

    @Update
    fun updateProductInCart(product: CartProducts)

    @Query("DELETE FROM CartProducts")
    fun nukeCart()
}