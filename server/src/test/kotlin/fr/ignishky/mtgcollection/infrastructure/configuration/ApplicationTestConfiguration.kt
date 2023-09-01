package fr.ignishky.mtgcollection.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@Profile("test")
@Configuration
class ApplicationTestConfiguration {

    @Bean
    @Primary
    fun clock(): Clock {
        return Clock.fixed(Instant.parse("1981-08-25T13:50:00Z"), ZoneOffset.UTC)
    }
}
