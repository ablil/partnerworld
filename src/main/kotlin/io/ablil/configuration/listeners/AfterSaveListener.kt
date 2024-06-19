package io.ablil.configuration.listeners

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.spring.data.datastore.core.mapping.event.AfterSaveEvent
import io.ablil.configuration.persistence.entities.ConfigurationHistoryChange
import io.ablil.configuration.persistence.entities.EventType
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.ConfigurationHistoryRepository
import io.ablil.configuration.utils.logger
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class AfterSaveListener(val repository: ConfigurationHistoryRepository, val objectMapper: ObjectMapper) :
    ApplicationListener<AfterSaveEvent> {

    val logger by logger()


    override fun onApplicationEvent(event: AfterSaveEvent) {
        logger.debug("after save event {}", event)
        val changes = event.targetEntities.filterIsInstance<PartnerConfiguration>().map {
            ConfigurationHistoryChange(
                eventType = EventType.CREATE_UPDATE,
                configuration = objectMapper.writeValueAsString(it),
                configurationId = requireNotNull(it.id)
            )
        }
        repository.saveAll(changes)
    }
}