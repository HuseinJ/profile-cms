package com.hjusic.api.profileapi.file.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Document
class FileDatabaseEntity(
    @Id
    val id: UUID,
    val name: String,
    val fileContent: ByteArray,
    val state: FileState){
}