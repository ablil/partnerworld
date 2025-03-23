package io.ablil.configuration.utils

import io.ablil.configuration.persistence.entities.PartnerConfiguration
import org.apache.commons.lang3.RandomStringUtils

object RandomConfigurationUtils {

    fun random(): PartnerConfiguration =
        PartnerConfiguration(
            shortname = RandomStringUtils.randomAlphabetic(10),
            displayName = RandomStringUtils.randomAlphabetic(20),
            navigations = listOf(),
        )

    fun random(shortname: String): PartnerConfiguration = random().copy(shortname = shortname)
}
