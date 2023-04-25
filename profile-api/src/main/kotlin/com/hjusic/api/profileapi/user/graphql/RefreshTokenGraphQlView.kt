package com.hjusic.api.profileapi.user.graphql

class RefreshTokenGraphQlView private constructor(accessToken: String, refreshToken:String, type: String) {

    companion object{
        fun from(accessToken: String, refreshToken: String): RefreshTokenGraphQlView{
            return RefreshTokenGraphQlView(accessToken, refreshToken, "Bearer");
        }
    }
}