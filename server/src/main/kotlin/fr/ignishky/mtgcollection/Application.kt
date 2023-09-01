package fr.ignishky.mtgcollection

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["fr.ignishky.framework", "fr.ignishky.mtgcollection"])
@EnableJpaRepositories(basePackages = ["fr.ignishky.framework", "fr.ignishky.mtgcollection"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
