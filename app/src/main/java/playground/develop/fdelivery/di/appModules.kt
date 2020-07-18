package playground.develop.fdelivery.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playground.develop.fdelivery.database.locale.AppDatabase
import playground.develop.fdelivery.repository.DataRepository
import playground.develop.fdelivery.repository.FavoriteRepository
import playground.develop.fdelivery.viewmodel.AppViewModel
import playground.develop.fdelivery.viewmodel.FavoriteViewModel

val appModules = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "food_delivery_app.dp")
            // DANGER! - DEBUG PURPOSES ONLY!.
            .allowMainThreadQueries().build()
    }
    single { get<AppDatabase>().favProductsDao() }
    factory { DataRepository() }
    factory { FavoriteRepository() }
    viewModel { AppViewModel() }
    viewModel { FavoriteViewModel() }
}