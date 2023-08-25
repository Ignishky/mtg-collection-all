plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.10")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.1.3")
}
