package com.hjusic.api.profileapi.file.model

import java.util.*

interface Files {
    fun getFile(fileUUID: UUID): Optional<File>
    fun trigger(fileEvent: FileEvent): File
}