package {{PACKAGE_NAME}}.android.app

import android.app.Application
import {{PACKAGE_NAME}}.di.KoinDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MyApplication)
            modules(appModule)
        }
        
        // Initialize common Koin modules
        KoinDI.init()
    }
}
