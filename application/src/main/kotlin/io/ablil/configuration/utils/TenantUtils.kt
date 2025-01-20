package io.ablil.configuration.utils

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object TenantUtils {

    private val currentTenant = ThreadLocal<String>()

    fun getTenant(): String =
        (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)?.request?.getHeader("x-tenant")
            ?.also { currentTenant.set(it) }
            ?: currentTenant.get()
            ?: throw IllegalStateException("Missing tenant")


    fun run(tenant: String, callback: () -> Unit) = run {
        val originalTenant = currentTenant.get()

        currentTenant.set(tenant)
        callback.invoke()

        currentTenant.set(originalTenant)
    }
}