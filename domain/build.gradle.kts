plugins {
    id("mtgcollection.kotlin-library-conventions")
    `java-test-fixtures`
    `maven-publish`
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":cqrs"))
}
