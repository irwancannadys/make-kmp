package {{PACKAGE_NAME}}.model

import kotlinx.serialization.Serializable

/**
 * Example model class.
 */
@Serializable
data class SampleModel(
    val id: Int,
    val name: String,
    val description: String,
    val isActive: Boolean = true
)
