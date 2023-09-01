package fr.ignishky.mtgcollection.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Clock

@Configuration
@Profile("!test")
class ApplicationConfiguration {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/sets/**").allowedMethods("GET").allowedOrigins("http://localhost:3000")
            }
        }
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }

}
