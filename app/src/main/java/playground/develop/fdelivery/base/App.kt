package playground.develop.fdelivery.base

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import playground.develop.fdelivery.di.appModules

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModules))
        }
    }
}