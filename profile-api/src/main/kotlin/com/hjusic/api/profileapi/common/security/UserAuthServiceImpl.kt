package com.hjusic.api.profileapi.common.security

import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntityRepository
import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseService
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import graphql.schema.validation.AppliedDirectiveArgumentsAreValid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import kotlin.reflect.typeOf

class UserAuthServiceImpl : UserAuthServices {

    @Autowired
    private val userDatabaseEntityRepository: UserDatabaseEntityRepository? = null

    @Autowired
    private val users: Users? = null

    override fun callingUser(): User {
        if(SecurityContextHolder.getContext().authentication.principal is String){
            throw SecurityException("User not in context")
        }

        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userDatabaseEntity = userDatabaseEntityRepository?.findByName(userDetails.username)?.orElseThrow{ UsernameNotFoundException("Usern does not exist")}
        return users?.map(userDatabaseEntity!!)!!
    }
}