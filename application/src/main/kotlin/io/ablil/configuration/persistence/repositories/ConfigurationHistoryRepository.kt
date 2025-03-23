package io.ablil.configuration.persistence.repositories

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository
import io.ablil.configuration.persistence.entities.ConfigurationHistoryChange

interface ConfigurationHistoryRepository : DatastoreRepository<ConfigurationHistoryChange, Long>
