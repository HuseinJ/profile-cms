package com.hjusic.api.profileapi.file.model

import com.hjusic.api.profileapi.common.event.Event
import java.time.Instant

abstract class FileEvent internal constructor(
    val file: File
): Event(file.id, Instant.now()) {
}