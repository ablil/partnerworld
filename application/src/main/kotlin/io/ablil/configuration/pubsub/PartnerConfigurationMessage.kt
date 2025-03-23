package io.ablil.configuration.pubsub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PartnerConfigurationMessage
@JsonCreator
constructor(
    @JsonProperty("displayName") val displayName: String,
    @JsonProperty("shortName") val shortName: String,
    @JsonProperty("tenant") val tenant: String,
)
