package io.ablil.configuration.web

import io.ablil.configuration.persistence.entities.ConfigurationMetadata
import io.ablil.configuration.persistence.entities.CouponNavigation
import io.ablil.configuration.persistence.entities.FeedNavigation
import io.ablil.configuration.persistence.entities.Navigation
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import java.math.BigDecimal
import java.util.UUID
import org.apache.commons.lang3.StringUtils
import org.openapitools.model.AnyNavigationDto
import org.openapitools.model.ConfigurationDto
import org.openapitools.model.ConfigurationMetadataDto
import org.openapitools.model.NavigationTypeDto

object ConfigurationMapper {

    fun to(configuration: PartnerConfiguration): ConfigurationDto =
        ConfigurationDto(
            id = BigDecimal.valueOf(requireNotNull(configuration.id)),
            shortName = configuration.shortname,
            displayName = configuration.displayName,
            iconUrl = configuration.iconUrl,
            metadata = configuration.metadata?.map { to(it) },
            navigations = configuration.navigations?.map { to(it) },
        )

    fun to(metadata: ConfigurationMetadata): ConfigurationMetadataDto =
        ConfigurationMetadataDto(shortName = metadata.shortname, description = metadata.description)

    fun to(navigation: Navigation): AnyNavigationDto =
        object : AnyNavigationDto {
            override val label: String
                get() = navigation.label

            override val identifier: UUID
                get() = UUID.fromString(navigation.identifier)

            override val type: NavigationTypeDto
                get() = NavigationTypeDto.valueOf(navigation.type.name)

            override val coupon: String
                get() = if (navigation is CouponNavigation) navigation.coupon else StringUtils.EMPTY

            override val navigationType: AnyNavigationDto.NavigationType
                get() =
                    when (navigation) {
                        is FeedNavigation -> AnyNavigationDto.NavigationType.FEED
                        is CouponNavigation -> AnyNavigationDto.NavigationType.COUPON
                        else -> error("unknown navigation 🤔")
                    }
        }
}
