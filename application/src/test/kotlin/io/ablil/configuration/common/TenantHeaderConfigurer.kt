package io.ablil.configuration.common

import io.ablil.configuration.utils.DEFAULT_TENANT
import io.ablil.configuration.utils.TENANT_HEADER
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcConfigurer
import org.springframework.web.context.WebApplicationContext

class TenantHeaderConfigurer : MockMvcConfigurer {

    override fun beforeMockMvcCreated(
        builder: ConfigurableMockMvcBuilder<*>,
        context: WebApplicationContext,
    ): RequestPostProcessor {
        return RequestPostProcessor { request ->
            request.also { it.addHeader(TENANT_HEADER, DEFAULT_TENANT) }
        }
    }
}
