plugins {
    id("mtgcollection.kotlin-library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":cqrs"))
}
