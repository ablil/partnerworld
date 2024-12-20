package io.ablil.configuration.web

import io.ablil.configuration.persistence.entities.ConfigurationMetadata
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.ConfigurationRepository
import io.ablil.configuration.utils.RandomConfigurationUtils
import io.ablil.configuration.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/configurations")
class ConfigurationController(val repository: ConfigurationRepository){

    val logger by logger()

    @PostMapping("/random")
    fun createRandomConfiguration(): PartnerConfiguration = repository.save(RandomConfigurationUtils.random().also {
        it.metadata = listOf(ConfigurationMetadata(it.shortname, "x${it.shortname}"))
    })

    @PostMapping
    fun createConfiguration(@RequestBody body: ConfigurationDto): ResponseEntity<PartnerConfiguration> {
        repository.queryByShortnameAndStatus(body.shortname)
            ?.let { return ResponseEntity.status(HttpStatus.CONFLICT).build() }
        return ResponseEntity.created(URI.create("/configurations/${body.shortname}"))
            .body(repository.save(PartnerConfiguration.from(body)))
    }

    @DeleteMapping("/{shortname}")
    fun deleteConfiguration(@PathVariable shortname: String) {
        repository.queryByShortnameAndStatus(shortname)?.also { repository.delete(it) }
            ?: logger.warn("configuration {} not found", shortname)
    }

}