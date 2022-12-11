package com.hjusic.api.profileapi.common.security.service

import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    @Autowired val userDatabaseEntityRepository: UserDatabaseEntityRepository
): UserDetailsService {

    @Override
    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        var user = userDatabaseEntityRepository.findByName(username!!)
            .orElseThrow{ UsernameNotFoundException("User Not Found with username: " + username) }
        return UserDetailsImpl.build(user)
    }
}