package io.ablil.configuration.persistence.repositories

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository
import com.google.cloud.spring.data.datastore.repository.query.Query
import io.ablil.configuration.persistence.entities.ConfigurationStatus
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository("configurationRepository")
interface ConfigurationRepository : DatastoreRepository<PartnerConfiguration, Long> {


    fun findByShortnameAndStatus(
        shortname: String,
        status: ConfigurationStatus = ConfigurationStatus.ENABLED
    ): PartnerConfiguration?


    fun findAllByStatus(status: ConfigurationStatus = ConfigurationStatus.ENABLED, pageable: Pageable): Page<PartnerConfiguration>

    @Query("select * from configurations where (shortname = @shortname OR metadata.shortname = @shortname ) AND status = @status ")
    fun queryByShortnameAndStatus(
        @Param("shortname") shortname: String,
        @Param("status") status: ConfigurationStatus = ConfigurationStatus.ENABLED
    ): PartnerConfiguration?
}

@Repository
@Primary
class ConfigurationRepositoryDecorator(
    val cacheManager: CacheManager,
    @Qualifier("configurationRepository") private val repository: ConfigurationRepository
) :
    ConfigurationRepository by repository {

    @Cacheable("configurations", key = "#shortname", unless = "#result == null")
    override fun queryByShortnameAndStatus(shortname: String, status: ConfigurationStatus): PartnerConfiguration? {
        return repository.queryByShortnameAndStatus(shortname, status)
    }

    @CacheEvict("configurations", key = "#entity.shortname")
    override fun <S : PartnerConfiguration?> save(entity: S & Any): S & Any {
        return repository.save(entity).also { it.metadata?.forEach { m -> evictCache(m.shortname) } }
    }

    @CacheEvict("configurations", key = "#entity.shortname")
    override fun delete(entity: PartnerConfiguration) {
        repository.delete(entity).also { entity.metadata?.forEach { evictCache(it.shortname) } }
    }

    private fun evictCache(key: String) = cacheManager.getCache("configurations")?.evict(key)
}