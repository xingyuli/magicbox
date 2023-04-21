dependencies {
    api(project(":box-http-client"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation(kotlin("reflect"))
    implementation("org.slf4j:slf4j-api:2.0.7")

    // testImplementation(kotlin("test"))
}