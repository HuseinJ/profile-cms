package com.hjusic.api.profileapi.file.model

interface Files {
    fun trigger(fileEvent: FileEvent): File
}