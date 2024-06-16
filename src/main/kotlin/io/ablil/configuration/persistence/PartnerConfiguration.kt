package io.ablil.configuration.persistence

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
    val navigations: List<Navigation>,
    val status: ConfigurationStatus = ConfigurationStatus.ENABLED,

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
                            type = it.navigationType,
                            id = UUID.randomUUID().toString()
                        )

                        is CouponDto -> CouponNavigation(
                            label = it.label,
                            type = it.navigationType,
                            coupon = it.coupon,
                            id = UUID.randomUUID().toString()
                        )

                        else -> error("unknown type")
                    }
                }
            )
        }
    }
}

@Entity(name = "navigations")
//@DiscriminatorField(field = "type")
abstract class Navigation {
    abstract val label: String
    abstract val type: NavigationType
    abstract var id: String?
}

//@DiscriminatorValue("feed")
data class FeedNavigation(override val label: String, override val type: NavigationType, override var id: String?) :
    Navigation()

//@DiscriminatorValue("coupon")
data class CouponNavigation(
    override val label: String,
    override val type: NavigationType,
    val coupon: String,
    override var id: String?
) : Navigation()


enum class NavigationType {
    DYNAMIC, STATIC
}

enum class ConfigurationStatus {
    ENABLED, DISABLED
}