package com.hjusic.api.profileapi.file.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.file.application.UploadFile
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import graphql.schema.DataFetchingEnvironment
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.lang.Exception


@DgsComponent
class FileUploadGraphQlService(
    val uploadFile: UploadFile,
    val userAuthServices: UserAuthServices
) {

    @DgsMutation
    @Throws(IOException::class)
    fun uploadFileWithMultipartPOST(dfe: DataFetchingEnvironment): FileGraphQlView {
        val file = dfe.getArgument<MultipartFile>("input")
        var loggedInUser = userAuthServices.callingUser()

        val registeredFile = uploadFile.uploadFile(file.name, file, loggedInUser)

        if(registeredFile.wasFailure()) {
            throw Exception(registeredFile.fail!!.reason)
        }

        val content = String(file.bytes)
        System.out.println(content)
        return FileGraphQlView.from(registeredFile.success!!)
    }
}