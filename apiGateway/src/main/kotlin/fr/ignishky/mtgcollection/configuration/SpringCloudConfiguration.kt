package fr.ignishky.mtgcollection.configuration

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringCloudConfiguration {

    @Bean
    fun gatewayRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("setsModule") { r -> r.path("/sets/**").uri("lb://mtg-collection-server") }
            .route("collectionModule") { r -> r.path("/collection/**").uri("lb://mtg-collection-server") }
            .build()
    }
}
