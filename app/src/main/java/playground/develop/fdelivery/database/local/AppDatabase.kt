package playground.develop.fdelivery.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import playground.develop.fdelivery.database.local.cart.CartDao
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.local.favorite.FavProducts
import playground.develop.fdelivery.database.local.favorite.FavoriteDao

@Database(entities = [FavProducts::class, CartProducts::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favProductsDao(): FavoriteDao
    abstract fun cartDao(): CartDao
}