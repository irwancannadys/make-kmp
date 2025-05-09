# Make-KMP

**Make-KMP** is a generator that converts an existing Android project into a Kotlin Multiplatform (KMP) project. This tool helps you create a complete KMP project structure with ready-to-use shared, androidApp, and iOS modules.

## Features

* Generate a basic KMP project structure with Gradle configuration
* Apply Clean Architecture with model, repository, and domain layers
* Integrate Dependency Injection using Koin
* iOS support with Swift and Xcode templates
* Support for Kotlin Serialization and kotlin-x libraries
* Setup for popular dependencies such as Coroutines, Ktor, DateTime, etc.
* Follows project structure best practices

## Requirements

* Bash shell
* Java Development Kit (JDK) 17 or higher
* Android Studio Flamingo (2022.2.1) or higher

## Usage

Clone the repository:

```bash
git clone https://github.com/username/make-kmp.git
cd make-kmp
```

Grant execution permission to the script:

```bash
chmod +x make-kmp.sh
```

Run the generator:

```bash
./make-kmp.sh -o /path/to/output -pkg com.example.myapp -n MyApp
```

## Command Line Options

| Option             | Description                                       | Default           |
| ------------------ | ------------------------------------------------- | ----------------- |
| `-p, --project`    | Path to the Android project to convert            | Current directory |
| `-o, --output`     | Output directory for the generated KMP project    | `./kmp-output`    |
| `-pkg, --package`  | Base package name for the project                 | `com.example.kmp` |
| `-n, --name`       | Application name                                  | `KmpApp`          |
| `-v, --verbose`    | Verbose mode for detailed logging                 | Disabled          |
| `-s, --simulation` | Simulation mode, skips Android project validation | Disabled          |
| `-h, --help`       | Display help                                      | -                 |

## Example Usage

```bash
# Convert using default options
./make-kmp.sh

# Convert a specific Android project with custom configuration
./make-kmp.sh -p /path/to/android/project -o ~/my-kmp-project -pkg com.mycompany.myapp -n AwesomeApp -v

# Only generate a new KMP structure without an Android project (simulation mode)
./make-kmp.sh -o ~/new-kmp-project -pkg com.mycompany.app -n MyKMPApp -s
```

## Project Structure

```
output-directory/
├── androidApp/              # Android module
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/      # Android Kotlin code
│   │       └── res/         # Android resources
│   └── build.gradle.kts     # Android Gradle config
├── iosApp/                  # iOS module
│   ├── iosApp/
│   │   ├── ContentView.swift
│   │   └── iosAppApp.swift
│   └── iosApp.xcodeproj/    # iOS Xcode project config
├── shared/                  # Shared KMP module
│   ├── src/
│   │   ├── androidMain/     # Android-specific code
│   │   ├── commonMain/      # Shared code across platforms
│   │   │   └── kotlin/
│   │   │       └── com/example/myapp/
│   │   │           ├── di/         # Dependency Injection
│   │   │           ├── domain/     # Use cases / business logic
│   │   │           ├── model/      # Data models
│   │   │           └── repository/ # Repositories
│   │   ├── commonTest/     # Shared tests
│   │   └── iosMain/        # iOS-specific code
│   └── build.gradle.kts     # Shared Gradle config
├── build.gradle.kts         # Root Gradle config
├── gradle.properties        # Gradle properties
└── settings.gradle.kts      # Gradle settings
```

## Generated Components

### Model Layer

Basic data structure using Kotlin Serialization:

```kotlin
@Serializable
data class SampleModel(
    val id: Int,
    val name: String,
    val description: String,
    val isActive: Boolean = true
)
```

### Repository Layer

Data access interface and implementation:

```kotlin
interface SampleRepository {
    fun getSamples(): Flow<List<SampleModel>>
    suspend fun getSampleById(id: Int): SampleModel?
    suspend fun addSample(sample: SampleModel)
    suspend fun updateSample(sample: SampleModel)
    suspend fun deleteSample(id: Int)
}
```

### Domain Layer (Use Cases)

Business logic as use cases:

```kotlin
class GetSamplesUseCase(private val repository: SampleRepository) {
    operator fun invoke(): Flow<List<SampleModel>> = repository.getSamples()
}
```

### Dependency Injection

Koin setup for managing dependencies:

```kotlin
object KoinDI {
    fun init() {
        startKoin {
            modules(commonModule, platformModule())
        }
    }

    private val commonModule = module {
        single<SampleRepository> { SampleRepositoryImpl() }
        factory { GetSamplesUseCase(get()) }
        // ... other use cases
    }
}
```

## Included Dependencies

* **Kotlin Coroutines**: Asynchronous programming
* **Ktor Client**: HTTP networking
* **Kotlinx Serialization**: JSON serialization
* **Kotlinx DateTime**: Date and time handling
* **Koin**: Dependency Injection
* **Multiplatform Settings**: Key-value storage
* **Jetpack Compose (Android)**: Modern Android UI toolkit
* **SwiftUI (iOS)**: iOS UI framework

## Customizing the Project

* **Add Modules**: Modify `settings.gradle.kts`
* **Add Dependencies**: Edit corresponding `build.gradle.kts`
* **Change Target Platforms**: Add more targets in Kotlin Multiplatform config

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

Yuk Ngopi ☕️🥩.
