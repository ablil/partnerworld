package io.ablil.configuration.persistence

import com.google.cloud.spring.data.datastore.core.mapping.DiscriminatorField
import com.google.cloud.spring.data.datastore.core.mapping.DiscriminatorValue
import com.google.cloud.spring.data.datastore.core.mapping.Entity
import io.ablil.configuration.web.ConfigurationDto
import io.ablil.configuration.web.CouponDto
import io.ablil.configuration.web.FeedDto
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.*

@Entity(name = "configurations")
data class PartnerConfiguration(
    val shortname: String,
    val displayName: String,
    val iconUrl: String = "https://placehold.co/50",
    val status: ConfigurationStatus = ConfigurationStatus.ENABLED,
    var navigations: List<Navigation>?,

    @LastModifiedDate
    var updatedAt: Date = Date.from(Instant.now()),
    @LastModifiedBy
    var updatedBy: String = "auditor",
    @Id
    var id: Long? = null
) {
    companion object {
        fun from(dto: ConfigurationDto): PartnerConfiguration {
            return PartnerConfiguration(
                shortname = dto.shortname,
                displayName = dto.displayName,
                navigations = dto.navigations.map {
                    when (it) {
                        is FeedDto -> FeedNavigation(
                            label = it.label,
                            identifier = UUID.randomUUID().toString(),
                            type = NavigationType.STATIC
                        )

                        is CouponDto -> CouponNavigation(
                            label = it.label,
                            coupon = it.coupon,
                            identifier = UUID.randomUUID().toString(),
                            type = NavigationType.STATIC
                        )

                        else -> error("unknown type")
                    }
                }
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