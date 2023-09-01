plugins {
    id("mtgcollection.kotlin-library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":cqrs"))

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    testImplementation("io.mockk:mockk:1.13.5")
}
