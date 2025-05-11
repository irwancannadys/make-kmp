package {{PACKAGE_NAME}}.repository

import {{PACKAGE_NAME}}.model.SampleModel
import {{PACKAGE_NAME}}.network.ApiResponse
import {{PACKAGE_NAME}}.network.SampleApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Interface for Sample repository.
 */
interface SampleRepository {
    fun getSamples(): Flow<List<SampleModel>>
    suspend fun getSampleById(id: Int): SampleModel?
    suspend fun addSample(sample: SampleModel)
    suspend fun updateSample(sample: SampleModel)
    suspend fun deleteSample(id: Int)
}

/**
 * Implementation of SampleRepository with both local cache and network support.
 */
class SampleRepositoryImpl(
    private val apiService: SampleApiService? = null // Optional dependency, can be null for offline mode
) : SampleRepository {
    // In-memory cache
    private val _samples = MutableStateFlow<List<SampleModel>>(emptyList())

    init {
        // Initialize with some sample data
        _samples.value = listOf(
            SampleModel(1, "Sample 1", "This is a sample item"),
            SampleModel(2, "Sample 2", "Another sample item"),
            SampleModel(3, "Sample 3", "Yet another sample item")
        )
    }

    override fun getSamples(): Flow<List<SampleModel>> {
        // Try to fetch from API if available, otherwise use local cache
        apiService?.let { service ->
            return service.getSamples().map { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        // Update cache with network data
                        _samples.value = response.data
                        response.data
                    }
                    else -> _samples.value // Return cached data on error or loading
                }
            }
        }
        
        // If no API service, just return the cache
        return _samples.asStateFlow()
    }

    override suspend fun getSampleById(id: Int): SampleModel? {
        // Try to get from API first if available
        apiService?.let { service ->
            val response = service.getSampleById(id)
            if (response is ApiResponse.Success) {
                return response.data
            }
        }
        
        // Fallback to local cache
        return _samples.value.find { it.id == id }
    }

    override suspend fun addSample(sample: SampleModel) {
        // Try to add via API if available
        apiService?.let { service ->
            val response = service.createSample(sample)
            if (response is ApiResponse.Success) {
                // Update local cache with the created sample from API
                _samples.value = _samples.value + response.data
                return
            }
        }
        
        // If API call failed or API is not available, just update local cache
        _samples.value = _samples.value + sample
    }

    override suspend fun updateSample(sample: SampleModel) {
        // For simplicity, we just update the local cache
        // In a real app, you would call an API to update the sample
        _samples.value = _samples.value.map {
            if (it.id == sample.id) sample else it
        }
    }

    override suspend fun deleteSample(id: Int) {
        // For simplicity, we just update the local cache
        // In a real app, you would call an API to delete the sample
        _samples.value = _samples.value.filterNot { it.id == id }
    }
}
