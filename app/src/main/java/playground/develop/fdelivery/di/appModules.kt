package playground.develop.fdelivery.di

import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playground.develop.fdelivery.database.local.AppDatabase
import playground.develop.fdelivery.repository.DataRepository
import playground.develop.fdelivery.repository.LocalDatabaseRepository
import playground.develop.fdelivery.viewmodel.AppViewModel
import playground.develop.fdelivery.viewmodel.LocalDatabaseViewModel

val appModules = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "food_delivery_app.dp")
            .build()
    }
    single { get<AppDatabase>().favProductsDao() }
    single { get<AppDatabase>().cartDao() }
    single { FirebaseAnalytics.getInstance(androidContext()) }
    factory { DataRepository() }
    factory { LocalDatabaseRepository() }
    viewModel { AppViewModel() }
    viewModel { LocalDatabaseViewModel() }
    factory { Firebase.firestore }
    factory { FirebaseAuth.getInstance() }
}