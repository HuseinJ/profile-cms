package com.hjusic.api.profileapi.file.graphql

import com.hjusic.api.profileapi.file.model.Files
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import org.springframework.security.access.annotation.Secured

@DgsComponent
class FileGraphQlService(
    private val files: Files
) {
    @DgsQuery
    @Secured
    fun files(): List<FileGraphQlView>? {
        return null
    }
}