package io.ablil.configuration.common

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

object MockMvcUtils {

    fun buildMockMvc(applicationContext: WebApplicationContext): MockMvc =
        MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(TenantHeaderConfigurer())
            .build()
}
