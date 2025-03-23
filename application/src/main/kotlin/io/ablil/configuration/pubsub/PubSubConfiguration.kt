package io.ablil.configuration.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter
import com.google.cloud.spring.pubsub.support.converter.PubSubMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PubSubConfiguration {

    @Bean
    fun pubSubMessageConverter(): PubSubMessageConverter =
        JacksonPubSubMessageConverter(ObjectMapper())

    @Bean
    fun subscribeToTopic(
        pubsubTemplate: PubSubTemplate,
        @Value("\${custom.pubsub.subscription}") subscription: String,
        partnerConfigurationMessageConsumer: PartnerConfigurationMessageConsumer,
    ): CommandLineRunner = CommandLineRunner {
        pubsubTemplate.subscribeAndConvert(
            subscription,
            partnerConfigurationMessageConsumer,
            PartnerConfigurationMessage::class.java,
        )
    }
}
