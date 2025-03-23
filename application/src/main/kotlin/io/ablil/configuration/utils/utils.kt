package io.ablil.configuration.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> T.logger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(this::class.java) }
