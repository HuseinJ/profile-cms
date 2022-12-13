package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.user.model.User

class UserGraphQlView private constructor(
    val name: String,
    val email: String
){
    companion object{
        fun from(user: User): UserGraphQlView{
            return UserGraphQlView(user.name, user.email)
        }
    }
}