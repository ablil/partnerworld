package io.ablil.configuration

import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.RandomConfigurationUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc
class ConfigurationApplicationTests {

    @Autowired
    lateinit var repository: ConfigurationRepository

    @Autowired
    lateinit var mvc: MockMvc

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `fetch configuration`() {
        repository.save(RandomConfigurationUtils.random("lp012"))
        mvc.get("/configurations/lp012").andExpectAll { status { is2xxSuccessful() } }
    }

    @Test
    fun `delete configuration`() {
        repository.save(RandomConfigurationUtils.random("lp012"))
        mvc.delete("/configurations/lp012").andExpectAll { status { is2xxSuccessful() } }
    }

    @Test
    fun `create configuration`() {
        mvc.post("/configurations") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "displayName": "Edeka",
                  "shortname": "lp012",
                  "navigations": [
                    {
                      "type": "feed",
                      "label": "my feed",
                      "navigationType": "DYNAMIC"
                    },
                    {
                      "type": "coupon",
                      "label": "my coupon",
                      "navigationType": "DYNAMIC",
                      "coupon": "voucher"
                    }
                  ]
                }""".trimIndent()
        }.andExpectAll { status { is2xxSuccessful() } }
        assertNotNull(repository.findByShortnameAndStatus("lp012"))
    }

    @Test
    fun `attempt to create an existing configuration`() {
        repository.save(RandomConfigurationUtils.random("lp012"))
        mvc.post("/configurations") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "displayName": "Edeka",
                  "shortname": "lp012",
                  "navigations": [
                    {
                      "type": "feed",
                      "label": "my feed",
                      "navigationType": "DYNAMIC"
                    },
                    {
                      "type": "coupon",
                      "label": "my coupon",
                      "navigationType": "DYNAMIC",
                      "coupon": "voucher"
                    }
                  ]
                }""".trimIndent()
        }.andExpectAll { status { is4xxClientError() } }
    }
}
