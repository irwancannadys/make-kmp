package {{PACKAGE_NAME}}.di

import {{PACKAGE_NAME}}.repository.SampleRepository
import {{PACKAGE_NAME}}.repository.SampleRepositoryImpl
import {{PACKAGE_NAME}}.domain.*
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Setup Koin DI untuk seluruh aplikasi.
 */
object KoinDI {
    fun init(appDeclaration: KoinAppDeclaration = {}) {
        startKoin {
            appDeclaration()
            modules(commonModule, platformModule())
        }
    }

    /**
     * Modul untuk dependensi yang umum untuk semua platform
     */
    private val commonModule = module {
        // Repositories
        single<SampleRepository> { SampleRepositoryImpl() }

        // Use Cases
        factory { GetSamplesUseCase(get()) }
        factory { GetSampleByIdUseCase(get()) }
        factory { AddSampleUseCase(get()) }
        factory { UpdateSampleUseCase(get()) }
        factory { DeleteSampleUseCase(get()) }
    }
}

/**
 * Modul spesifik untuk masing-masing platform
 */
expect fun platformModule(): Module