package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.user.application.SignUpUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

import static org.hamcrest.Matchers.containsString

import static org.hamcrest.Matchers.equalTo

class ChangePasswordGraphQlTest extends BaseSpringTest{

    @Autowired
    SignUpUser signUpUser;

    @Autowired
    Users users;

    @Autowired
    PasswordEncoder passwordEncoder;

    def "should be able to change the password"() {
        given:
            def newPassword = "somethingNew"
            def oldPassword = "somethingOld"
            def name = "user1-change-pw" + Instant.now().toEpochMilli()
        and:
            users.trigger(new UserCreated(new User(UUID.randomUUID(), name, name + "@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(oldPassword)))
        and:
            userAuthServices.callingUser() >> users.findByName(name)
        and:
        def query = """
                mutation{
                    changePassword(newPassword: "${newPassword}", oldPassword: "${oldPassword}")
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
    }

    def "should return error if newPassword is empty"() {
        given:
        def oldPassword = "somethingOld"
        def newPassword = ""
        def name = "user1-change-pw" + Instant.now().toEpochMilli()
        and:
        users.trigger(new UserCreated(new User(UUID.randomUUID(), name, name + "@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(oldPassword)))
        and:
        userAuthServices.callingUser() >> users.findByName(name)
        and:
        def query = """
                mutation{
                    changePassword(newPassword: "${newPassword}", oldPassword: "${oldPassword}")
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

    def "should return error if oldPassword is empty"() {
        given:
        def newPassword = "somethingNew"
        def oldPassword = ""
        def name = "user1-change-pw" + Instant.now().toEpochMilli()
        and:
        users.trigger(new UserCreated(new User(UUID.randomUUID(), name, name + "@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(oldPassword)))
        and:
        userAuthServices.callingUser() >> users.findByName(name)
        and:
        def query = """
                mutation{
                    changePassword(newPassword: "${newPassword}", oldPassword: "${oldPassword}")
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

    def "should return error if user tries to log in with old password after password change"() {
        given:
        def newPassword = "somethingNew"
        def oldPassword = "somethingOld"
        def name = "user1-change-pw" + Instant.now().toEpochMilli()
        and:
        users.trigger(new UserCreated(new User(UUID.randomUUID(), name, name + "@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(oldPassword)))
        and:
        userAuthServices.callingUser() >> users.findByName(name)
        and:
        def query = """
                mutation{
                    changePassword(newPassword: "${newPassword}", oldPassword: "${oldPassword}")
                }
                """
        and:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()

        when:
         query = """
            mutation{
                signIn(name: "${name}", password: "${oldPassword}"){
                    token
                    type
                    user {
                        name
                        email
                    }
                }
            }
         """
        body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        result = body.when().post()

        then:
        result.then()
                .statusCode(200)
                .body("errors[0].message", containsString(ValidationErrorCode.WRONG_CREDENTIALS.name()))
    }

    def "should not return error if user tries to log in with new password after password change"() {
        given:
        def newPassword = "somethingNew"
        def oldPassword = "somethingOld"
        def name = "user1-change-pw" + Instant.now().toEpochMilli()
        and:
        users.trigger(new UserCreated(new User(UUID.randomUUID(), name, name + "@mail.com", new HashSet<AccessRole>()), passwordEncoder.encode(oldPassword)))
        and:
        userAuthServices.callingUser() >> users.findByName(name)
        and:
        def query = """
                mutation{
                    changePassword(newPassword: "${newPassword}", oldPassword: "${oldPassword}")
                }
                """
        and:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()

        when:
        query = """
            mutation{
                signIn(name: "${name}", password: "${newPassword}"){
                    token
                    type
                    user {
                        name
                        email
                    }
                }
            }
         """
        body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        result = body.when().post()

        then:
        result.then()
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.signIn.user.name", equalTo(name))
    }
}
