package io.ablil.configuration.persistence

import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreAuditing
import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreRepositories
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@Configuration
@EnableDatastoreRepositories
@EnableDatastoreAuditing
class DatastoreConfiguration {

    @Component
    @RequestScope
    inner class AuditorProvider(var request: HttpServletRequest): AuditorAware<String> {

        override fun getCurrentAuditor(): Optional<String> {
            return Optional.of(request.getHeader("x-auditor") ?: "n/a")
        }

    }

}