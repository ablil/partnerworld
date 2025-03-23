package io.ablil.configuration

import io.ablil.configuration.common.MockMvcUtils
import io.ablil.configuration.common.TenantSetup
import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.RandomConfigurationUtils
import kotlin.test.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.web.context.WebApplicationContext

private const val BASE_PATH = "/v1/configurations"

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(TenantSetup::class)
class ConfigurationApplicationTests {

    @Autowired private lateinit var webApplicationContext: WebApplicationContext

    @Autowired lateinit var repository: ConfigurationRepository

    lateinit var mvc: MockMvc

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        mvc = MockMvcUtils.buildMockMvc(webApplicationContext)
    }

    @Test
    fun `fetch configuration`() {
        repository.save(RandomConfigurationUtils.random("lp012"))
        mvc.get("$BASE_PATH/lp012").andExpectAll { status { is2xxSuccessful() } }
    }

    @Test
    fun `fetch all configurations`() {
        mvc.get(BASE_PATH).andExpect { status { is2xxSuccessful() } }
    }

    @Test
    fun `delete configuration`() {
        repository.save(RandomConfigurationUtils.random("lp012"))
        mvc.delete("$BASE_PATH/lp012").andExpectAll { status { is2xxSuccessful() } }
        assertNull(repository.findByShortnameAndStatus("lp012"))
    }
}
