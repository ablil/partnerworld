package io.ablil.configuration.web

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.ablil.configuration.persistence.NavigationType

data class ConfigurationDto(
    val shortname: String,
    val displayName: String,
    val navigations: List<NavigationDto>
)


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    Type(value = FeedDto::class, name = "feed"),
    Type(value = CouponDto::class, name = "coupon")
)
abstract class NavigationDto {
    abstract val label: String
    abstract val navigationType: NavigationType
}

data class FeedDto(override val label: String, override val navigationType: NavigationType): NavigationDto()

data class CouponDto(override val label: String, override val navigationType: NavigationType, val coupon: String): NavigationDto()