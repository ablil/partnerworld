package io.ablil.configuration.persistence

import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreAuditing
import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreRepositories
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*
import org.springframework.boot.CommandLineRunner

@Configuration
@EnableDatastoreRepositories
@EnableDatastoreAuditing
class DatastoreConfiguration {

    val logger by logger()

    @Component
    inner class AuditorProvider() : AuditorAware<String> {

        override fun getCurrentAuditor(): Optional<String> {
            return Optional.of("system")
        }

    }

    @Bean
    fun databaseSeeder(repository: ConfigurationRepository): CommandLineRunner = CommandLineRunner {
        repository.deleteAll()
        repository.saveAll(List(10) { PartnerConfiguration.createRandom() })
        logger.info("created 10 random configurations")
    }

}