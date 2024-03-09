package com.hjusic.api.profileapi.file.graphql

import com.hjusic.api.profileapi.file.model.File
import com.hjusic.api.profileapi.user.graphql.UserGraphQlView
import com.hjusic.api.profileapi.user.model.User

class FileGraphQlView private constructor(
    val id: String,
    val name: String
) {
    companion object{
        fun from(file: File): FileGraphQlView {
            return FileGraphQlView(file.id.toString(), file.name)
        }
    }
}