package com.hjusic.api.profileapi.file.application

import com.hjusic.api.profileapi.common.error.*
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.file.model.File
import com.hjusic.api.profileapi.file.model.FileDomainErrorCode
import com.hjusic.api.profileapi.file.model.Files
import java.util.UUID

class GetFile(
    private var files: Files
) {
    fun getFile(fileId: String): Either<ContextError, File> {
        if (fileId == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var fileUUID = UUID.fromString(fileId)

        var potentialFile = files.getFile(fileUUID)

        if(potentialFile.isEmpty){
            return Either.wasFailure(DomainError(FileDomainErrorCode.FILE_DOES_NOT_EXIST.name))
        }

        return Either.wasSuccess(potentialFile.get())
    }
}