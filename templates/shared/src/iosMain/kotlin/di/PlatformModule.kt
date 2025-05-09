package {{PACKAGE_NAME}}.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Modul Koin untuk komponen spesifik iOS.
 */
actual fun platformModule(): Module = module {
    // iOS specific dependencies
}
