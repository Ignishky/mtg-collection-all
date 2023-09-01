plugins {
    id("mtgcollection.kotlin-application-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":cqrs"))
    implementation(project(":domain"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.mock-server:mockserver-junit-jupiter:5.14.0")
    testImplementation("org.mock-server:mockserver-spring-test-listener:5.14.0")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.17.6")
}
