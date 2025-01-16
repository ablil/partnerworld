package io.ablil.configuration.persistence.entities

import com.google.cloud.spring.data.datastore.core.mapping.DiscriminatorField
import com.google.cloud.spring.data.datastore.core.mapping.DiscriminatorValue
import com.google.cloud.spring.data.datastore.core.mapping.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.*
import org.apache.commons.lang3.RandomStringUtils

@Entity(name = "configurations")
data class PartnerConfiguration(
    val shortname: String,
    val displayName: String,
    val iconUrl: String = "https://placehold.co/50",
    val status: ConfigurationStatus = ConfigurationStatus.ENABLED,
    var navigations: List<Navigation>?,
    var metadata: List<ConfigurationMetadata>? = null,

    @LastModifiedDate
    var updatedAt: Date = Date.from(Instant.now()),
    @LastModifiedBy
    var updatedBy: String = "auditor",
    @Id
    var id: Long? = null
) {
    companion object {

        fun createRandom(): PartnerConfiguration {
            return PartnerConfiguration(
                shortname = RandomStringUtils.randomAlphanumeric(5),
                displayName = RandomStringUtils.randomAlphanumeric(10),
                navigations = listOf(
                    FeedNavigation(
                        label = RandomStringUtils.randomAlphanumeric(5),
                        identifier = UUID.randomUUID().toString(),
                        type = NavigationType.STATIC
                    ),CouponNavigation(
                        label = RandomStringUtils.randomAlphanumeric(5),
                        identifier = UUID.randomUUID().toString(),
                        type = NavigationType.STATIC,
                        coupon = "CPN"
                    )
                ),
                metadata = listOf(
                    ConfigurationMetadata(
                        shortname = RandomStringUtils.randomAlphanumeric(5),
                        description = RandomStringUtils.randomAlphanumeric(10),
                    )
                )
            )
        }
    }
}


@Entity(name = "navigation")
@DiscriminatorField(field = "navigation_type")
abstract class Navigation(
    open val label: String,
    open val type: NavigationType,
    open val identifier: String,
)

@DiscriminatorValue("feed")
data class FeedNavigation(
    override val label: String,
    override val identifier: String,
    override val type: NavigationType,
) : Navigation(label, type, identifier)

@DiscriminatorValue("coupon")
data class CouponNavigation(
    val coupon: String,
    override val label: String,
    override val identifier: String,
    override val type: NavigationType
) : Navigation(label, type, identifier)

enum class NavigationType {
    DYNAMIC, STATIC
}

enum class ConfigurationStatus {
    ENABLED, DISABLED
}

@Entity
data class ConfigurationMetadata(val shortname: String, val description: String?)