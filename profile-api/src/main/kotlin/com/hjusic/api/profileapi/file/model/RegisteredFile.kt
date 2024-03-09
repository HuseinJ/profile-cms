package com.hjusic.api.profileapi.file.model

import java.util.UUID
class RegisteredFile internal constructor(
    id: UUID,
    name: String
): File(id, name) {

    companion object{
        fun from(id: UUID, name: String): RegisteredFile {
            return RegisteredFile(id, name)
        }
    }
}