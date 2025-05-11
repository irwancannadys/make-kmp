package {{PACKAGE_NAME}}.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * HTTP client for making API calls.
 */
class ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
    
    companion object {
        const val BASE_URL = "https://api.example.com"
    }
}
