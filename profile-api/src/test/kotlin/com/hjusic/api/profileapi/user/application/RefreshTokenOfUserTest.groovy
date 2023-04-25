package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.RefreshToken
import com.hjusic.api.profileapi.user.model.RefreshTokenCreated
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import java.time.Instant

class RefreshTokenOfUserTest extends Specification{

    Users users = Mock()
    JwtUtils jwtUtils = Mock()

    def "should return error if empty value is passed in"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils)
        and:
       def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>())
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser("", user)
        then:
        result.wasFailure()
        result.getFail().reason == ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should return wrong credentials if refresh token does not match"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils)
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>())
        and:
        users.findRefreshTokenOfUser(user) >> new RefreshToken(UUID.randomUUID(), "NOTTHESAMETOKEN", Instant.now())
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser("1234", user)
        then:
        result.wasFailure()
        result.getFail().reason == ValidationErrorCode.WRONG_CREDENTIALS.name()
    }

    def "should return wrong credentials if refresh token is expired"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils)
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>())
        and:
        def token = "someToken"
        and:
        users.findRefreshTokenOfUser(user) >> new RefreshToken(UUID.randomUUID(), token, Instant.now())
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser(token, user)
        then:
        result.wasFailure()
        result.getFail().reason == ValidationErrorCode.EXPIRED_CREDENTIALS.name()
    }

    def "should generate new jwt token if jwt token is valid and not expired"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils)
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>())
        and:
        def token = "someToken"
        and:
        users.findRefreshTokenOfUser(user) >> new RefreshToken(UUID.randomUUID(), token, Instant.now().plusMillis(100000L))
        and:
        def contextMock = Mock(SecurityContext.class) as SecurityContext
        contextMock.getAuthentication() >> Mock(Authentication.class)
        SecurityContextHolder.setContext(contextMock)
        when:
        def result = refreshTokeOfUser.refreshTokenOfUser(token, user)
        then:
        result.wasSuccess()
        1 * jwtUtils.generateJwtToken(_) >> "1234"
        result.getSuccess().user == user;
        result.getSuccess().token == "1234"
    }

    def "should create refresh token for user"() {
        given:
        def refreshTokeOfUser = new RefreshTokenOfUser(users, jwtUtils)
        and:
        def user = new User(UUID.randomUUID(), "name", "mail@mail.com", new HashSet<AccessRole>())
        when:
        def result = refreshTokeOfUser.createRefreshTokenForUser(user)
        then:
        1 * users.trigger(_ as RefreshTokenCreated)
    }
}
