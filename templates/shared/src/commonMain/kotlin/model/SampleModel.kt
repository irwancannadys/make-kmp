package {{PACKAGE_NAME}}.model

import kotlinx.serialization.Serializable

/**
 * Contoh model data yang digunakan di seluruh aplikasi.
 */
@Serializable
data class SampleModel(
    val id: Int,
    val name: String,
    val description: String,
    val isActive: Boolean = true
)
