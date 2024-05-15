package com.hjusic.api.profileapi.file.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.file.model.File
import com.hjusic.api.profileapi.file.model.Files
import com.hjusic.api.profileapi.file.model.UnregisteredFile
import com.hjusic.api.profileapi.user.model.User
import org.springframework.web.multipart.MultipartFile

class UploadFile(
    var files: Files,
) {
    fun uploadFile(name: String, file: MultipartFile, callingUser: User): Either<ContextError, File> {
        if (name == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        if (file.isEmpty) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var newFile = UnregisteredFile.from(name, file)

        var possibleFileUpload = newFile.registerFileWithUser(callingUser)

        if (possibleFileUpload.wasFailure()) {
            return Either.wasFailure(ContextError(possibleFileUpload.fail!!.reason))
        }

        return Either.wasSuccess(files.trigger(possibleFileUpload.success!!))
    }
}