package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.application.RefreshTokenOfUser
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.http.Header
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.equalTo

@ActiveProfiles("testjwt")
class RefreshTokenGraphQlTest extends BaseSpringTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Users users;

    @Autowired
    Pages pages;

    @Autowired
    SignInUser signInUser;

    def "should return an error if jwt token of user expires"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        sleep(2000)
        and:
        userAuthServices.callingUser() >> user
        def query = """
                query{
                    pages {
                        id
                        name
                    }
                }
                """
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8")).header(new Header("Authorization", "Bearer " + userTokenTuple.token))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(401)
    }

    def "should be able to refresh a expired JWT-Token"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        sleep(2000)
        and:
        userAuthServices.callingUser() >> user
        def query = """
            mutation {    
                refreshToken(refreshToken: "${userTokenTuple.refreshToken}"){
                    token
                    type
                    user {
                        name
                        email
                    }
                    refreshToken
                }
            }
                """
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def refreshResult = body.when().post()

        def jsonSlurper = new JsonSlurper()
        def jsonResponse = jsonSlurper.parseText(refreshResult.getBody().asString())

        def token = jsonResponse.data.refreshToken.token
        when:
        query = """
                query{
                    pages {
                        id
                        name
                    }
                }
                """
        def resultBody =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8")).header(new Header("Authorization", "Bearer " + token))
        def result = resultBody.when().post()
        then:
        result.then()
                .statusCode(200)
    }

    def "should not refresh if refreshToken is expired"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        sleep(6000)
        and:
        userAuthServices.callingUser() >> user
        def query = """
            mutation {    
                refreshToken(refreshToken: "${userTokenTuple.refreshToken}"){
                    token
                    type
                    user {
                        name
                        email
                    }
                    refreshToken
                }
            }
                """

        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def refreshResult = body.when().post()

        then:
        refreshResult.then()
                .statusCode(200)
                .body("errors[0].message", containsString(ValidationErrorCode.EXPIRED_CREDENTIALS.name()))

    }
}
