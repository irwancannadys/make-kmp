package {{PACKAGE_NAME}}.network

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with message and statusCode
 */
sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val statusCode: Int? = null) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
    
    companion object {
        fun <T> loading(): ApiResponse<T> = Loading
        fun <T> success(data: T): ApiResponse<T> = Success(data)
        fun <T> error(message: String, statusCode: Int? = null): ApiResponse<T> = Error(message, statusCode)
    }
}
