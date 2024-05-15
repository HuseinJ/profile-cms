package com.hjusic.api.profileapi.file.model

import java.util.UUID
class RegisteredFile internal constructor(
    id: UUID,
    name: String,
    var content: ByteArray
): File(id, name) {

    companion object{
        fun from(id: UUID, name: String, content: ByteArray): RegisteredFile {
            return RegisteredFile(id, name, content)
        }
    }
}