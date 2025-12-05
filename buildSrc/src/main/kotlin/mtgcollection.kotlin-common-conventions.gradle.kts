import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
}

group = "fr.ignishky"

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("21")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.withType<Test> {
    useJUnitPlatform {
        excludeEngines("junit-vintage")
    }

    testLogging {
        events("skipped", "failed")
    }
}
