package io.ablil.configuration.web

import io.ablil.configuration.persistence.entities.ConfigurationStatus
import io.ablil.configuration.persistence.entities.PartnerConfiguration
import io.ablil.configuration.persistence.repositories.PartnerConfigurationRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/configurations")
class ConfigurationController(val repository: PartnerConfigurationRepository) {

    @GetMapping
    fun getAll(): List<PartnerConfiguration> = repository.findAll().toList()

    @GetMapping("/{shortname}")
    fun getConfiguration(@PathVariable shortname: String): ResponseEntity<PartnerConfiguration?> {
        val configuration = repository.findByShortnameAndStatus(shortname)
        return configuration.let { ResponseEntity.ofNullable(it) }
    }

    @PostMapping
    fun createConfiguration(@RequestBody body: ConfigurationDto): ResponseEntity<PartnerConfiguration> {
        if (repository.existsByShortnameAndStatus(body.shortname, ConfigurationStatus.ENABLED)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

        return ResponseEntity.created(URI.create("/configurations/${body.shortname}"))
            .body(repository.save(PartnerConfiguration.from(body)))
    }

    @DeleteMapping("/{shortname}")
    fun deleteConfiguration(
        @PathVariable shortname: String,
        @RequestParam("hard", defaultValue = "false") isHardDelete: Boolean
    ) {
        if (isHardDelete) {
            repository.deleteByShortnameAndStatus(shortname)
        } else {
            repository.findByShortnameAndStatus(shortname)
                ?.copy(status = ConfigurationStatus.DISABLED)
                ?.also(repository::save)
        }
    }

}