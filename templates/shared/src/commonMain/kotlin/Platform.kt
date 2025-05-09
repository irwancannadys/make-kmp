package {{PACKAGE_NAME}}

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform