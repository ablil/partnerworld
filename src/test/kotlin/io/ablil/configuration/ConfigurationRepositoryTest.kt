package io.ablil.configuration

import io.ablil.configuration.persistence.entities.ConfigurationMetadata
import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.RandomConfigurationUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull


@SpringBootTest // TODO: is there a better to load smaller context ?
class ConfigurationRepositoryTest {

    @Autowired
    lateinit var repository: ConfigurationRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `fetch configuration by shortname or alternative shortname`() {
        val config1 = RandomConfigurationUtils.random("lp012").let { repository.save(it) }
        assertNotNull(repository.queryByShortnameAndStatus("lp012"))

        val config2 = RandomConfigurationUtils.random("lp022")
            .copy(metadata = listOf(ConfigurationMetadata("lp99", null))).let { repository.save(it) }
        assertNotNull(config2.metadata)
        assertNotNull(repository.queryByShortnameAndStatus("lp99"))
    }
}

