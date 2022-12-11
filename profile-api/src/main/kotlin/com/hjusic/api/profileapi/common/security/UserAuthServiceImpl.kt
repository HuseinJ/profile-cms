package com.hjusic.api.profileapi.common.security

import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntityRepository
import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseService
import com.hjusic.api.profileapi.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserAuthServiceImpl : UserAuthServices {

    @Autowired
    private val userDatabaseEntityRepository: UserDatabaseEntityRepository? = null

    @Autowired
    private val userDatabaseService: UserDatabaseService? = null

    override fun callingUser(): User {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val userDatabaseEntity = userDatabaseEntityRepository?.findByName(userDetails.username)?.orElseThrow{ UsernameNotFoundException("Usern does not exist")}
        return userDatabaseService?.map(userDatabaseEntity!!)!!
    }
}