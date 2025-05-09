package {{PACKAGE_NAME}}.repository

import {{PACKAGE_NAME}}.model.SampleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Contoh repository yang dapat digunakan oleh Android dan iOS.
 */
interface SampleRepository {
    fun getSamples(): Flow<List<SampleModel>>
    suspend fun getSampleById(id: Int): SampleModel?
    suspend fun addSample(sample: SampleModel)
    suspend fun updateSample(sample: SampleModel)
    suspend fun deleteSample(id: Int)
}

/**
 * Implementasi sederhana dari SampleRepository menggunakan in-memory storage.
 */
class SampleRepositoryImpl : SampleRepository {
    // Menggunakan MutableStateFlow sebagai in-memory storage
    private val _samples = MutableStateFlow<List<SampleModel>>(emptyList())

    init {
        // Inisialisasi dengan beberapa data sampel
        _samples.value = listOf(
            SampleModel(id = 1, name = "Sample 1", description = "Ini adalah item sampel pertama"),
            SampleModel(id = 2, name = "Sample 2", description = "Ini adalah item sampel kedua"),
            SampleModel(id = 3, name = "Sample 3", description = "Ini adalah item sampel ketiga")
        )
    }

    override fun getSamples(): Flow<List<SampleModel>> = _samples.asStateFlow()

    override suspend fun getSampleById(id: Int): SampleModel? = 
        _samples.value.find { it.id == id }

    override suspend fun addSample(sample: SampleModel) {
        _samples.value = _samples.value + sample
    }

    override suspend fun updateSample(sample: SampleModel) {
        _samples.value = _samples.value.map {
            if (it.id == sample.id) sample else it
        }
    }

    override suspend fun deleteSample(id: Int) {
        _samples.value = _samples.value.filterNot { it.id == id }
    }
}
