package io.ablil.configuration.listeners

import com.google.cloud.spring.data.datastore.core.mapping.event.AfterDeleteEvent
import io.ablil.configuration.persistence.entities.ConfigurationHistoryChange
import io.ablil.configuration.persistence.entities.EventType
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.ConfigurationHistoryRepository
import io.ablil.configuration.utils.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["custom.auditing.enabled"], havingValue = "true")
class AfterDeleteListener(val historyRepository: ConfigurationHistoryRepository) :
    ApplicationListener<AfterDeleteEvent> {

    val logger by logger()

    override fun onApplicationEvent(event: AfterDeleteEvent) {
        logger.debug("after delete event {}", event)
        if (isSupportedEntity(event)) {
            val configurationIds: List<Long> = extractConfigurationIds(event)
            val changes =
                configurationIds.map {
                    ConfigurationHistoryChange(
                        configurationId = it,
                        configuration = null,
                        eventType = EventType.DELETE,
                    )
                }
            historyRepository.saveAll(changes)
        }
    }

    private fun extractConfigurationIds(event: AfterDeleteEvent): List<Long> {
        return event.keys.map { it.id }
    }

    private fun isSupportedEntity(event: AfterDeleteEvent): Boolean {
        return PartnerConfiguration::class
            .java
            .isAssignableFrom(event.optionalTargetEntityClass.get())
    }
}
