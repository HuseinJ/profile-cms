package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.user.model.User
import io.restassured.RestAssured

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.equalTo

class LoggedInUserGraphQlTest extends BaseSpringTest{

    def "should return the logged in User"() {
        given:
        def query = """
                query{
                    loggedInUser{
                        name
                        email
                    }
                }
                """
        and:
            userAuthServices.callingUser() >> new User(UUID.randomUUID(), "user1", "user1@mail.com", new HashSet<AccessRole>(), null)
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.loggedInUser.name", equalTo("user1"))
                .body("data.loggedInUser.email", equalTo("user1@mail.com"))
    }

    def "should return error if no user is i security context"() {
        given:
        def query = """
                query{
                    loggedInUser{
                        name
                        email
                    }
                }
                """
        and:
        userAuthServices.callingUser() >> null
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(200)
                .body("errors[0].message", containsString("No user logged in"))
    }
}
