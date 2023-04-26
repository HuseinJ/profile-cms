package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

import static org.hamcrest.Matchers.emptyOrNullString
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not
import static org.hamcrest.Matchers.notNullValue

class RefreshTokenGraphQlTest {

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    JwtUtils jwtUtils;

    def "User can signIn and refresh jwt token"() {
        given:
        def password = "password1"
        def name = "user" + Instant.now().toEpochMilli()
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), name, "user1@mail.com", new HashSet<AccessRole>(), null), passwordEncoder.encode(password)))
        and:
        def query = """
                mutation{
                    signIn(name: "${user1.name}", password: "${password}"){
                        token
                        type
                        refreshToken
                        user {
                            name
                            email
                        }
                    }
                }
                """
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.signIn.token", notNullValue())
                .body("data.signIn.type", equalTo("Bearer"))
                .body("data.signIn.user.name", equalTo(user1.name))
                .body("data.signIn.user.email", equalTo(user1.email))
                .body("data.signIn.refreshToken", not(emptyOrNullString()))
    }
}
