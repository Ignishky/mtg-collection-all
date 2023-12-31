plugins {
    id("mtgcollection.kotlin-common-conventions")

    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
}
