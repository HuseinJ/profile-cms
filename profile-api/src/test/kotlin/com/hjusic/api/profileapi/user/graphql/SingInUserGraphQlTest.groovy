package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue

class SingInUserGraphQlTest extends BaseSpringTest {

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    JwtUtils jwtUtils;

    def "User should be able to sign in"() {
        given:
        def password = "password1"
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(password)))
        and:
        def query = """
                mutation{
                    signIn(name: "${user1.name}", password: "${password}"){
                        token
                        type
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
    }

    def "should get validation error if name is empty"() {
        given:
        def query = """
                mutation{
                    signIn(name: "", password: "password1"){
                        token
                        type
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
                .body("errors[0].message", containsString(ValidationErrorCode.EMPTY_VALUE.name()))
    }

    def "should get validation error if password is empty"() {
        given:
        def query = """
                mutation{
                    signIn(name: "user", password: ""){
                        token
                        type
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
                .body("errors[0].message", containsString(ValidationErrorCode.EMPTY_VALUE.name()))
    }

    def "should return error if user tries to signIn with wrong credentials"() {
        given:
        def password = "password1"
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(password)))
        and:
        def query = """
                mutation{
                    signIn(name: "${user1.name}", password: "password2"){
                        token
                        type
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
                .body("errors[0].message", containsString(ValidationErrorCode.WRONG_CREDENTIALS.name()))
    }
}

