package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.RefreshToken
import com.hjusic.api.profileapi.user.model.RefreshTokenCreated
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import spock.lang.Specification

import java.time.Instant

class RefreshTokenOfUserTest extends Specification{

    Users users = Mock()
    JwtUtils jwtUtils = Mock()
    UserDetailsService userDetailsService = Mock()


    def "should return error if empty value is passed in"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils, userDetailsService)
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser("")
        then:
        result.wasFailure()
        result.getFail().reason == ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should return wrong credentials if refresh token is expired"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils, userDetailsService)
        and:
        def token = "someToken"
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>(), new RefreshToken(token, Instant.now()))
        and:
        users.findUserByRefreshToken(token)>> user
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser(token)
        then:
        result.wasFailure()
        result.getFail().reason == ValidationErrorCode.EXPIRED_CREDENTIALS.name()
    }

    def "should generate new jwt token if jwt token is valid and not expired"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils, userDetailsService)
        and:
        def token = "someToken"
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>(), new RefreshToken(token, Instant.now().plusMillis(1000L)))
        and:
        users.findUserByRefreshToken(token)>> user
        and:
        userDetailsService.loadUserByUsername(_ as String) >> Mock(UserDetails.class)
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser(token)
        then:
        result.wasSuccess()
        1 * jwtUtils.generateJwtToken(_) >> "1234"
        result.getSuccess().user == user;
        result.getSuccess().token == "1234"
    }

    def "should create refresh token for user"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils, userDetailsService)
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>(), null)
        when:
        def result = refreshTokeOfUser.createRefreshTokenForUser(user)
        then:
        1 * users.trigger(_ as RefreshTokenCreated)
    }
}
