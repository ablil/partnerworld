package io.ablil.configuration.persistence.repositories

import io.ablil.configuration.utils.RandomConfigurationUtils
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringJUnitConfig(CacheTestConfiguration::class, ConfigurationRepositoryDecorator::class)
class CacheLayerTests {

    @MockBean(name = "configurationRepository")
    lateinit var repository: ConfigurationRepository

    @Autowired
    lateinit var cacheManager: CacheManager

    @Autowired
    lateinit var repositoryDecorator: ConfigurationRepositoryDecorator

    @Test
    fun `load configuration from cache by shortname`() {
        whenever(repository.queryByShortnameAndStatus("lp012")).thenReturn(RandomConfigurationUtils.random("lp012"))
        repeat(4) { repositoryDecorator.queryByShortnameAndStatus("lp012") }
        verify(repository, times(1)).queryByShortnameAndStatus("lp012")
        assertNotNull(cacheManager.getCache("configurations")?.get("lp012"))
    }

    @Test
    fun `evict cache after deleting a configuration`() {
        repeat(4) { repositoryDecorator.queryByShortnameAndStatus("lp012") }
            .also { assertNotNull(cacheManager.getCache("configurations")?.get("lp012")) }

        repositoryDecorator.delete(RandomConfigurationUtils.random("lp012"))
        assertNull(cacheManager.getCache("configurations")?.get("lp012"))
    }
}

@TestConfiguration
@EnableAutoConfiguration
@EnableCaching
class CacheTestConfiguration {

    @Bean
    @Primary
    fun cacheManager(): CacheManager = ConcurrentMapCacheManager("configurations")

    @Bean
    fun repositoryDecorator(repository: ConfigurationRepository) =
        ConfigurationRepositoryDecorator(cacheManager(), repository)

}