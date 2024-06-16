package io.ablil.configuration.persistence

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository

interface PartnerConfigurationRepository: DatastoreRepository<PartnerConfiguration, Long> {

    fun findByShortname(shortname: String): PartnerConfiguration

    fun existsByShortnameAndStatus(shortname: String, status: ConfigurationStatus): Boolean

    fun findByShortnameAndStatus(shortname: String, status: ConfigurationStatus = ConfigurationStatus.ENABLED): PartnerConfiguration?

    fun deleteByShortnameAndStatus(shortname: String, status: ConfigurationStatus = ConfigurationStatus.ENABLED): Int
}