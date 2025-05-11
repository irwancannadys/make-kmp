package {{PACKAGE_NAME}}.network

import {{PACKAGE_NAME}}.model.SampleModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Sample API service that demonstrates how to make API calls.
 */
class SampleApiService(private val apiClient: ApiClient) {
    
    /**
     * Get a list of samples from the API.
     */
    fun getSamples(): Flow<ApiResponse<List<SampleModel>>> = flow {
        emit(ApiResponse.loading())
        try {
            val response = apiClient.client.get("${ApiClient.BASE_URL}/samples")
            
            if (response.status.isSuccess()) {
                val samples: List<SampleModel> = response.body()
                emit(ApiResponse.success(samples))
            } else {
                emit(ApiResponse.error("Error: ${response.status.description}", response.status.value))
            }
        } catch (e: Exception) {
            emit(ApiResponse.error(e.message ?: "Unknown error occurred"))
        }
    }
    
    /**
     * Get a single sample by ID.
     */
    suspend fun getSampleById(id: Int): ApiResponse<SampleModel> {
        return try {
            val response = apiClient.client.get("${ApiClient.BASE_URL}/samples/$id")
            
            if (response.status.isSuccess()) {
                val sample: SampleModel = response.body()
                ApiResponse.success(sample)
            } else {
                ApiResponse.error("Error: ${response.status.description}", response.status.value)
            }
        } catch (e: Exception) {
            ApiResponse.error(e.message ?: "Unknown error occurred")
        }
    }
    
    /**
     * Create a new sample.
     */
    suspend fun createSample(sample: SampleModel): ApiResponse<SampleModel> {
        return try {
            val response = apiClient.client.post("${ApiClient.BASE_URL}/samples") {
                contentType(ContentType.Application.Json)
                setBody(sample)
            }
            
            if (response.status.isSuccess()) {
                val createdSample: SampleModel = response.body()
                ApiResponse.success(createdSample)
            } else {
                ApiResponse.error("Error: ${response.status.description}", response.status.value)
            }
        } catch (e: Exception) {
            ApiResponse.error(e.message ?: "Unknown error occurred")
        }
    }
}
