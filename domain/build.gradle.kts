plugins {
    id("mtgcollection.kotlin-library-conventions")
    `java-test-fixtures`
    `maven-publish`
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":cqrs"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}
