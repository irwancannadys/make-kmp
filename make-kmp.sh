#!/bin/bash

# make-kmp: Kotlin Multiplatform Generator for Android projects
#
# Usage: ./make-kmp.sh [options]
#
# Options:
#   -p, --project     Path to Android project (default: current directory)
#   -o, --output      Output path for KMP project (default: ./kmp-output)
#   -pkg, --package   Base package name (default: com.example.kmp)
#   -n, --name        Application name (default: KmpApp)
#   -h, --help        Show help
#   -v, --verbose     Verbose mode

# Default values
PROJECT_PATH="$(pwd)"
OUTPUT_PATH="./kmp-output"
PACKAGE_NAME="com.example.kmp"
APP_NAME="KmpApp"
VERBOSE=false
SIMULATION_MODE=false

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory and templates directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
TEMPLATES_DIR="$SCRIPT_DIR/templates"

# Function to display help
show_help() {
    echo "Usage: ./make-kmp.sh [options]"
    echo ""
    echo "Options:"
    echo "  -p, --project     Path to Android project (default: current directory)"
    echo "  -o, --output      Output path for KMP project (default: ./kmp-output)"
    echo "  -pkg, --package   Base package name (default: com.example.kmp)"
    echo "  -n, --name        Application name (default: KmpApp)"
    echo "  -h, --help        Show help"
    echo "  -v, --verbose     Verbose mode"
    echo "  -s, --simulation  Run in simulation mode (skip Android project validation)"
    exit 0
}

# Logging function
log() {
    if [ "$VERBOSE" = true ] || [ "$2" = "error" ] || [ "$2" = "success" ]; then
        local timestamp=$(date +"%H:%M:%S")
        case "$2" in
            "error")
                echo -e "${RED}[$timestamp] ERROR: $1${NC}"
                ;;
            "success")
                echo -e "${GREEN}[$timestamp] SUCCESS: $1${NC}"
                ;;
            "warning")
                echo -e "${YELLOW}[$timestamp] WARNING: $1${NC}"
                ;;
            "info")
                echo -e "${BLUE}[$timestamp] INFO: $1${NC}"
                ;;
            *)
                echo -e "[$timestamp] $1"
                ;;
        esac
    fi
}

# Validate Android project
validate_project() {
    if [ "$SIMULATION_MODE" = true ]; then
        log "Running in simulation mode, skipping Android project validation" "warning"
        return 0
    fi

    log "Validating Android project at $PROJECT_PATH" "info"

    if [ ! -d "$PROJECT_PATH" ]; then
        log "Project path does not exist: $PROJECT_PATH" "error"
        exit 1
    fi

    # Check for build.gradle or build.gradle.kts
    if [ ! -f "$PROJECT_PATH/build.gradle" ] && [ ! -f "$PROJECT_PATH/build.gradle.kts" ]; then
        log "No build.gradle or build.gradle.kts found in project directory" "error"
        exit 1
    fi

    log "Project validation completed" "success"
}

# Create output directory
create_output_dir() {
    log "Creating output directory: $OUTPUT_PATH" "info"

    if [ -d "$OUTPUT_PATH" ]; then
        # Check if directory is empty
        if [ "$(ls -A "$OUTPUT_PATH")" ]; then
            log "Output directory is not empty: $OUTPUT_PATH" "error"
            exit 1
        fi
    else
        mkdir -p "$OUTPUT_PATH"
    fi

    log "Output directory created" "success"
}

# Convert package name to path format
get_package_path() {
    echo $(echo $PACKAGE_NAME | sed 's/\./\//g')
}

# Create basic KMP project structure
create_project_structure() {
    log "Creating basic KMP project structure" "info"

    # Create main module directories
    mkdir -p "$OUTPUT_PATH/shared"
    mkdir -p "$OUTPUT_PATH/androidApp"
    mkdir -p "$OUTPUT_PATH/iosApp"

    # Create directory structure for shared module
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin"
    mkdir -p "$OUTPUT_PATH/shared/src/androidMain/kotlin"
    mkdir -p "$OUTPUT_PATH/shared/src/iosMain/kotlin"
    mkdir -p "$OUTPUT_PATH/shared/src/commonTest/kotlin"

    # Create specific directories for shared module with package structure
    PACKAGE_PATH=$(get_package_path)
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH"
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/domain"
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/model"
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/repository"
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/di"
    mkdir -p "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/network"
    mkdir -p "$OUTPUT_PATH/shared/src/androidMain/kotlin/$PACKAGE_PATH"
    mkdir -p "$OUTPUT_PATH/shared/src/androidMain/kotlin/$PACKAGE_PATH/di"
    mkdir -p "$OUTPUT_PATH/shared/src/iosMain/kotlin/$PACKAGE_PATH"
    mkdir -p "$OUTPUT_PATH/shared/src/iosMain/kotlin/$PACKAGE_PATH/di"

    # Create Android app directories
    mkdir -p "$OUTPUT_PATH/androidApp/src/main/kotlin/app"

    log "Basic project structure created" "success"
}

# Verify templates directory exists
create_templates_dir() {
    if [ ! -d "$TEMPLATES_DIR" ]; then
        log "Templates directory not found: $TEMPLATES_DIR" "error"
        exit 1
    fi

    log "Templates directory: $TEMPLATES_DIR" "info"
}

# Function to copy and process template files
copy_and_process_templates() {
    log "Copying and processing template files" "info"

    # Get the package path
    PACKAGE_PATH=$(get_package_path)

    # Copy root template files
    if [ -d "$TEMPLATES_DIR/root" ]; then
        cp -r "$TEMPLATES_DIR/root/"* "$OUTPUT_PATH/"
        log "Copied root templates" "info"

        # Check if GitHub Actions workflows exist and copy them
        if [ -d "$TEMPLATES_DIR/root/.github/workflows" ]; then
            mkdir -p "$OUTPUT_PATH/.github/workflows"
            cp -r "$TEMPLATES_DIR/root/.github/workflows/"* "$OUTPUT_PATH/.github/workflows/"
            log "Copied GitHub Actions workflows" "info"
        fi

        # Check if other GitHub configs exist and copy them
        if [ -f "$TEMPLATES_DIR/root/.github/CODEOWNERS" ]; then
            mkdir -p "$OUTPUT_PATH/.github"
            cp "$TEMPLATES_DIR/root/.github/CODEOWNERS" "$OUTPUT_PATH/.github/"
            log "Copied GitHub CODEOWNERS file" "info"
        fi
    fi

    # Copy shared module template files
    if [ -d "$TEMPLATES_DIR/shared" ]; then
        cp -r "$TEMPLATES_DIR/shared/"* "$OUTPUT_PATH/shared/"
        log "Copied shared module templates" "info"
    fi

    # Copy androidApp module template files
    if [ -d "$TEMPLATES_DIR/androidApp" ]; then
        cp -r "$TEMPLATES_DIR/androidApp/"* "$OUTPUT_PATH/androidApp/"
        log "Copied androidApp module templates" "info"
    fi

    # Copy iOS module template files
    if [ -d "$TEMPLATES_DIR/iosApp" ]; then
        cp -r "$TEMPLATES_DIR/iosApp/"* "$OUTPUT_PATH/iosApp/"
        log "Copied iosApp module templates" "info"
    fi

    # Process template placeholders
    log "Processing template placeholders" "info"

    # Find all files and replace placeholders
    find "$OUTPUT_PATH" -type f -not -path "*/\.*" | while read file; do
        # Skip binary files
        if file "$file" | grep -q "text"; then
            sed -i.bak "s/{{APP_NAME}}/$APP_NAME/g" "$file"
            sed -i.bak "s/{{PACKAGE_NAME}}/$PACKAGE_NAME/g" "$file"
            sed -i.bak "s/{{PACKAGE_PATH}}/$PACKAGE_PATH/g" "$file"
            rm -f "${file}.bak"
        fi
    done

    # Special processing for GitHub workflow files which are in a hidden directory
    if [ -d "$OUTPUT_PATH/.github/workflows" ]; then
        find "$OUTPUT_PATH/.github" -type f | while read file; do
            if file "$file" | grep -q "text"; then
                sed -i.bak "s/{{APP_NAME}}/$APP_NAME/g" "$file"
                sed -i.bak "s/{{PACKAGE_NAME}}/$PACKAGE_NAME/g" "$file"
                sed -i.bak "s/{{PACKAGE_PATH}}/$PACKAGE_PATH/g" "$file"
                # Optional: Replace GitHub username placeholder if needed
                # sed -i.bak "s/{{GITHUB_USERNAME}}/yourusername/g" "$file"
                rm -f "${file}.bak"
            fi
        done
        log "Processed GitHub workflow files" "info"
    fi

    # Special processing for iOS project.pbxproj - it's a text file but some utilities might not detect it
    if [ -f "$OUTPUT_PATH/iosApp/iosApp.xcodeproj/project.pbxproj" ]; then
        sed -i.bak "s/{{APP_NAME}}/$APP_NAME/g" "$OUTPUT_PATH/iosApp/iosApp.xcodeproj/project.pbxproj"
        sed -i.bak "s/{{PACKAGE_NAME}}/$PACKAGE_NAME/g" "$OUTPUT_PATH/iosApp/iosApp.xcodeproj/project.pbxproj"
        rm -f "$OUTPUT_PATH/iosApp/iosApp.xcodeproj/project.pbxproj.bak"
        log "Processed iOS project.pbxproj" "info"
    fi

    # Move template files to correct package structure
    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/domain/SampleUseCase.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/domain/SampleUseCase.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/domain/"
        log "Moved SampleUseCase.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/model/SampleModel.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/model/SampleModel.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/model/"
        log "Moved SampleModel.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/repository/SampleRepository.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/repository/SampleRepository.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/repository/"
        log "Moved SampleRepository.kt to package directory" "info"
    fi

    # Move network files to correct package structure
    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/ApiClient.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/ApiClient.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/network/"
        log "Moved ApiClient.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/ApiResponse.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/ApiResponse.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/network/"
        log "Moved ApiResponse.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/SampleApiService.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/network/SampleApiService.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/network/"
        log "Moved SampleApiService.kt to package directory" "info"
    fi

    # Move other shared files if they exist outside package structure
    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/Greeting.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/Greeting.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/"
        log "Moved Greeting.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/Platform.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/Platform.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/"
        log "Moved Platform.kt to package directory" "info"
    fi

    # Move DI files to correct package structure
    if [ -f "$OUTPUT_PATH/shared/src/commonMain/kotlin/di/KoinModule.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/commonMain/kotlin/di/KoinModule.kt" "$OUTPUT_PATH/shared/src/commonMain/kotlin/$PACKAGE_PATH/di/"
        log "Moved KoinModule.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/androidMain/kotlin/di/PlatformModule.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/androidMain/kotlin/di/PlatformModule.kt" "$OUTPUT_PATH/shared/src/androidMain/kotlin/$PACKAGE_PATH/di/"
        log "Moved Android PlatformModule.kt to package directory" "info"
    fi

    if [ -f "$OUTPUT_PATH/shared/src/iosMain/kotlin/di/PlatformModule.kt" ]; then
        mv "$OUTPUT_PATH/shared/src/iosMain/kotlin/di/PlatformModule.kt" "$OUTPUT_PATH/shared/src/iosMain/kotlin/$PACKAGE_PATH/di/"
        log "Moved iOS PlatformModule.kt to package directory" "info"
    fi

    # Clean up empty directories that might be left
    if [ -d "$OUTPUT_PATH/shared/src/commonMain/kotlin/domain" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/commonMain/kotlin/domain")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/commonMain/kotlin/domain"
        log "Removed empty domain directory" "info"
    fi

    if [ -d "$OUTPUT_PATH/shared/src/commonMain/kotlin/model" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/commonMain/kotlin/model")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/commonMain/kotlin/model"
        log "Removed empty model directory" "info"
    fi

    if [ -d "$OUTPUT_PATH/shared/src/commonMain/kotlin/repository" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/commonMain/kotlin/repository")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/commonMain/kotlin/repository"
        log "Removed empty repository directory" "info"
    fi

    if [ -d "$OUTPUT_PATH/shared/src/commonMain/kotlin/network" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/commonMain/kotlin/network")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/commonMain/kotlin/network"
        log "Removed empty network directory" "info"
    fi

    # Clean up empty di directories
    if [ -d "$OUTPUT_PATH/shared/src/commonMain/kotlin/di" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/commonMain/kotlin/di")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/commonMain/kotlin/di"
        log "Removed empty di directory in commonMain" "info"
    fi

    if [ -d "$OUTPUT_PATH/shared/src/androidMain/kotlin/di" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/androidMain/kotlin/di")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/androidMain/kotlin/di"
        log "Removed empty di directory in androidMain" "info"
    fi

    if [ -d "$OUTPUT_PATH/shared/src/iosMain/kotlin/di" ] && [ -z "$(ls -A "$OUTPUT_PATH/shared/src/iosMain/kotlin/di")" ]; then
        rmdir "$OUTPUT_PATH/shared/src/iosMain/kotlin/di"
        log "Removed empty di directory in iosMain" "info"
    fi

    log "Template processing completed" "success"
}

# Main function
main() {
    echo -e "${GREEN}Make-KMP Generator${NC}"
    echo "This tool will convert your Android project to Kotlin Multiplatform."
    echo ""

    log "Starting make-kmp with configuration:" "info"
    log "Project path: $PROJECT_PATH" "info"
    log "Output path: $OUTPUT_PATH" "info"
    log "Package name: $PACKAGE_NAME" "info"
    log "App name: $APP_NAME" "info"

    validate_project
    create_output_dir
    create_templates_dir
    create_project_structure
    copy_and_process_templates

    log "KMP project successfully generated at $OUTPUT_PATH" "success"
    echo -e "\n${GREEN}✅ KMP project successfully generated at $OUTPUT_PATH${NC}"
    echo -e "${BLUE}You can open it in Android Studio or IntelliJ IDEA.${NC}"
}

# Parse arguments
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -p|--project)
            PROJECT_PATH="$2"
            shift
            shift
            ;;
        -o|--output)
            OUTPUT_PATH="$2"
            shift
            shift
            ;;
        -pkg|--package)
            PACKAGE_NAME="$2"
            shift
            shift
            ;;
        -n|--name)
            APP_NAME="$2"
            shift
            shift
            ;;
        -h|--help)
            show_help
            shift
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        -s|--simulation)
            SIMULATION_MODE=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            shift
            ;;
    esac
done

# Execute main function
main