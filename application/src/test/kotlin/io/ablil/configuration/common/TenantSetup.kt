package io.ablil.configuration.common

import io.ablil.configuration.utils.DEFAULT_TENANT
import io.ablil.configuration.utils.TenantUtils
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TenantSetup : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(p0: ExtensionContext?) {
        TenantUtils.currentTenant.set(DEFAULT_TENANT)
    }

    override fun afterEach(p0: ExtensionContext?) {
        TenantUtils.currentTenant.set(null)
    }
}
