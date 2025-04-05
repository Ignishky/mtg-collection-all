package fr.ignishky.mtgcollection.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Profile("!test")
class ApplicationConfiguration {

    @Bean
    fun corsConfigurer() = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/sets/**")
                .allowedMethods("GET")
                .allowedOrigins(
                    "http://localhost:3000", // dev mode
                    "http://localhost:5000", // build mode
                )
            registry.addMapping("/collection/**")
                .allowedMethods("GET", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins(
                    "http://localhost:3000", // dev mode
                    "http://localhost:5000", // build mode
                )
        }
    }

}
