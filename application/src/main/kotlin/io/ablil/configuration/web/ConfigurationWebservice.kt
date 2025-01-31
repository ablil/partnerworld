package io.ablil.configuration.web

import io.ablil.configuration.persistence.entities.ConfigurationStatus
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

    override fun deleteConfiguration(shortname: String, xTenant: String): ResponseEntity<Unit> {
        logger.debug("Deleting configuration $shortname")
        val configuration = repository.findByShortnameAndStatus(shortname) ?: return ResponseEntity.noContent().build()
        repository.save(configuration.copy(status = ConfigurationStatus.DISABLED))
        return ResponseEntity.noContent().build()
    }

    override fun getAllConfigurations(page: Int, size: Int, xTenant: String): ResponseEntity<GetAllConfigurations200ResponseDto> {
        logger.info("Got request to fetch configurations, page={}, size={}", page, size)
        val result = repository.findAllByStatus(ConfigurationStatus.ENABLED, PageRequest.of(page - 1, size))
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

    // TODO: do something
    override fun getConfiguration(shortname: String, xTenant: String): ResponseEntity<ConfigurationDto> {
        logger.info("Got request to get configuration {}", shortname)
        return repository.findByShortnameAndStatus(shortname)?.let { ResponseEntity.ok(ConfigurationMapper.to(it)) }
            ?: ResponseEntity.notFound().build()
    }

}