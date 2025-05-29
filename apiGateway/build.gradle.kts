plugins {
    id("mtgcollection.kotlin-application-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.2.3")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.2.1")
}
