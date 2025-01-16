package io.ablil.configuration.persistence.entities

import com.google.cloud.spring.data.datastore.core.mapping.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity(name = "configurations_history")
data class ConfigurationHistoryChange(
    val configuration: String?,
    val configurationId: Long,
    val eventType: EventType,
    @LastModifiedDate
    var auditedAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedBy
    var auditedBy: String = "n/a",
    @Id
    var id: Long? = null
)

enum class EventType {
    CREATE_UPDATE, DELETE
}