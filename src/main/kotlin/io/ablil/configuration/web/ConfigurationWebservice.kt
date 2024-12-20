package io.ablil.configuration.web

import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.logger
import io.ablil.oas.ConfigurationApi
import org.openapitools.model.ConfigurationDto
import org.openapitools.model.GetAllConfigurations200ResponseDto
import org.openapitools.model.PaginationDto
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfigurationWebservice(
    val repository: ConfigurationRepository
) : ConfigurationApi {
    val logger by logger()

    override fun getAllConfigurations(page: Int, size: Int): ResponseEntity<GetAllConfigurations200ResponseDto> {
        logger.info("Got request to fetch configurations, page={}, size={}", page, size)
        val result = repository.findAll(PageRequest.of(page - 1, size))
        return ResponseEntity.ok(
            GetAllConfigurations200ResponseDto(
                pagination = PaginationDto(
                    page = result.number + 1,
                    propertySize = result.size,
                    totalPages = result.totalPages,
                    totalItems = result.totalElements.toInt()
                ),
                configuration = result.content.map { ConfigurationMapper.to(it) }
            )
        )
    }

    override fun getConfiguration(shortname: String): ResponseEntity<ConfigurationDto> {
        logger.info("Got request to get configuration {}", shortname)
        return repository.findByShortnameAndStatus(shortname)?.let { ResponseEntity.ok(ConfigurationMapper.to(it)) }
            ?: ResponseEntity.notFound().build()
    }

}