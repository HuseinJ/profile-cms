package com.hjusic.api.profileapi.common.security.infrastructure

import com.hjusic.api.profileapi.common.security.service.UserDetailsServiceImpl
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.security.Security
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthTokenFilter: OncePerRequestFilter() {

    @Autowired
    private val jwtUtils: JwtUtils? = null;

    @Autowired
    private val userDetailsServiceImpl: UserDetailsServiceImpl? = null;

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt: String = parseJwt(request)
            if (jwt != "" && jwtUtils?.validateJwtToken(jwt)!!) {
                val username: String = jwtUtils?.getUserNameFromJwtToken(jwt)
                val userDetails: UserDetails = userDetailsServiceImpl?.loadUserByUsername(username)!!
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e)
            response.status = HttpStatus.UNAUTHORIZED.value()
            throw SecurityException(e.message)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String{
        val headerAuth: String = request.getHeader("Authorization") ?: return "";

        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else ""

    }
}