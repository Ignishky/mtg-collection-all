plugins {
    id("mtgcollection.kotlin-library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.springframework:spring-jdbc:6.0.11")
}
