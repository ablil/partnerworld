package io.ablil.configuration.persistence.repositories

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository
import com.google.cloud.spring.data.datastore.repository.query.Query
import io.ablil.configuration.persistence.entities.ConfigurationStatus
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ConfigurationRepository : DatastoreRepository<PartnerConfiguration, Long> {


    fun findByShortnameAndStatus(
        shortname: String,
        status: ConfigurationStatus = ConfigurationStatus.ENABLED
    ): PartnerConfiguration?


    @Query("select * from configurations where (shortname = @shortname OR metadata.shortname = @shortname ) AND status = @status ")
    fun queryByShortnameAndStatus(
        @Param("shortname") shortname: String,
        @Param("status") status: ConfigurationStatus = ConfigurationStatus.ENABLED
    ): PartnerConfiguration?
}