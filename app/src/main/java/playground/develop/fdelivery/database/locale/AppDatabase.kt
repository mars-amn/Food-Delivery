package playground.develop.fdelivery.database.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.database.locale.favorite.FavoriteDao

@Database(entities = [FavProducts::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favProductsDao(): FavoriteDao
}