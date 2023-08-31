plugins {
    id("mtgcollection.kotlin-library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.springframework.data:spring-data-jpa:3.0.0")
}
