package io.ablil.configuration.pubsub

import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.TenantUtils
import java.util.function.Consumer
import net.logstash.logback.argument.StructuredArguments.keyValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PartnerConfigurationMessageConsumer(
    private val configurationRepository: ConfigurationRepository
) : Consumer<ConvertedBasicAcknowledgeablePubsubMessage<PartnerConfigurationMessage>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun accept(
        message: ConvertedBasicAcknowledgeablePubsubMessage<PartnerConfigurationMessage>
    ) {
        logger.info("processing message", keyValue("message", message))
        val configuration = message.payload

        TenantUtils.run(configuration.tenant) {
            configurationRepository
                .save(
                    PartnerConfiguration.createRandom()
                        .copy(
                            shortname = configuration.shortName,
                            displayName = configuration.displayName,
                        )
                )
                .also { logger.info("created configuration $it") }
        }
        message.ack()
    }
}
