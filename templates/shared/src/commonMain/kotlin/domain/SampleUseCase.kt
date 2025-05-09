package {{PACKAGE_NAME}}.domain

import {{PACKAGE_NAME}}.model.SampleModel
import {{PACKAGE_NAME}}.repository.SampleRepository
import kotlinx.coroutines.flow.Flow

/**
 * Contoh Use Case untuk menampilkan logika bisnis dan pemisahan antara UI dan repository.
 */
class GetSamplesUseCase(private val repository: SampleRepository) {
    // Kotlin style operator function
    operator fun invoke(): Flow<List<SampleModel>> = repository.getSamples()
}

class GetSampleByIdUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(id: Int): SampleModel? = repository.getSampleById(id)
}

class AddSampleUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(name: String, description: String) {
        // Buat sample baru dengan ID acak
        val newSample = SampleModel(
            id = (1..1000).random(), // Cara sederhana untuk demo
            name = name,
            description = description
        )
        repository.addSample(newSample)
    }
}

class UpdateSampleUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(sample: SampleModel) {
        repository.updateSample(sample)
    }
}

class DeleteSampleUseCase(private val repository: SampleRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteSample(id)
    }
}
