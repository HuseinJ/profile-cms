package com.hjusic.api.profileapi.file.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.user.model.User
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

class UnregisteredFile internal constructor(
    val uuid: UUID,
    name: String,
    val content: MultipartFile
): File(uuid, name) {

    companion object {
        fun from(name: String, content: MultipartFile): UnregisteredFile {
            return UnregisteredFile(UUID.randomUUID(), name, content)
        }
    }
    fun registerFileWithUser(
        callingUser: User,
    ): Either<DomainError, FileUploaded> {
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.UPLOAD_FILE }) {
            return Either.wasFailure(DomainError(FileDomainErrorCode.USER_IS_NOT_ALLOWED_TO_UPLOAD.name))
        }

        return Either.wasSuccess(FileUploaded(this, this.content));
    }
}