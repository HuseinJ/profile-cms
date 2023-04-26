package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class RefreshTokenTest extends BaseSpringTest {

    @Autowired
    Users users

    def "should create Refresh Token for user"() {
        given:
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>(), null), "password"))
        and:
        def token =  "someJWTTOKEN" + Instant.now().toEpochMilli()
        when:
        def refreshToken = users.trigger(new RefreshTokenCreated(user, new RefreshToken(token, Instant.now())))
        then:
        users.findRefreshTokenOfUser(user).getToken() == token
    }

    def "should find User by given refresh token"() {
        given:
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>(), null), "password"))
        and:
        def token =  "someJWTTOKEN" + Instant.now().toEpochMilli()
        when:
        def refreshTokenUser = users.trigger(new RefreshTokenCreated(user, new RefreshToken(token, Instant.now())))
        then:
        users.findUserByRefreshToken(refreshTokenUser.refreshToken.token).id == user.id
    }

    def "should not find refresh token if empty for user"() {
        when:
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>(), null), "password"))
        then:
        users.findRefreshTokenOfUser(user) == null
    }
}
