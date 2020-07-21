package playground.develop.fdelivery.database.local.favorite

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavProduct(product: FavProducts): Single<Long>

    @Query("SELECT * FROM FavoriteProducts WHERE product_code =:id")
    fun getFavProduct(id: Long): FavProducts

    @Query("SELECT * FROM FavoriteProducts")
    fun getFavoriteProducts(): DataSource.Factory<Int, FavProducts>

    @Delete
    fun deleteProduct(favProducts: FavProducts): Int
}