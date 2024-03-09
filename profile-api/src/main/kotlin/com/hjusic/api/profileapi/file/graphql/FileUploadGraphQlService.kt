package com.hjusic.api.profileapi.file.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import graphql.schema.DataFetchingEnvironment
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@DgsComponent
class FileUploadGraphQlService {

    @DgsMutation
    @Throws(IOException::class)
    fun uploadFileWithMultipartPOST(dfe: DataFetchingEnvironment): Boolean {
        val file = dfe.getArgument<MultipartFile>("input")
        val content = String(file.bytes)
        System.out.println(content)
        return !content.isEmpty()
    }
}