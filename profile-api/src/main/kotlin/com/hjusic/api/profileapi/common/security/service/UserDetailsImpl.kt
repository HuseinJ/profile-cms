package com.hjusic.api.profileapi.common.security.service

import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
    private val id: UUID,
    private val username: String,
    private val password: String,
    private val authorities: MutableCollection<out GrantedAuthority>
) : UserDetails {
    companion object {
        fun build(user: UserDatabaseEntity): UserDetailsImpl {
            var authorities = user.roles.stream().map { role -> SimpleGrantedAuthority(role.id.name) }.toList()

            return UserDetailsImpl(user.id, user.name, user.password, authorities)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}