package {{PACKAGE_NAME}}.di

import {{PACKAGE_NAME}}.repository.SampleRepository
import {{PACKAGE_NAME}}.repository.SampleRepositoryImpl
import {{PACKAGE_NAME}}.domain.*
import {{PACKAGE_NAME}}.network.ApiClient
import {{PACKAGE_NAME}}.network.SampleApiService
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
     * Module for common dependencies across all platforms
     */
    private val commonModule = module {
        // Network
        single { ApiClient() }
        single { SampleApiService(get()) }
        
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
 * Platform-specific module for each platform
 */
expect fun platformModule(): Module
