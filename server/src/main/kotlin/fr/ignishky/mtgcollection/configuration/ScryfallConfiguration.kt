package fr.ignishky.mtgcollection.configuration

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ScryfallProperties::class)
class ScryfallConfiguration {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder) = builder.build()

    @Bean
    fun objectMapper() = ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setSerializationInclusion(NON_EMPTY)

}
