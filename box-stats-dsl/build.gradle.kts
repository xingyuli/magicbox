dependencies {
    implementation(kotlin("reflect"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // ktor
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    // TODO viclau - declare customized LogLevel, so that this library can be a implementation dependency
    api("io.ktor:ktor-client-logging:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-gson:2.2.4")
    implementation("io.ktor:ktor-client-logging:2.2.4")

    // expose gson only rather than the entire ktor-serialization-gson
    api("com.google.code.gson:gson:2.10.1")

    // TODO this should be declared in test or client module
    runtimeOnly("ch.qos.logback:logback-classic:1.4.6")

    // testImplementation(kotlin("test"))
}