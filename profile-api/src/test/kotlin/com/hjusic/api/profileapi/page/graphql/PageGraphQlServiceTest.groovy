package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import io.restassured.http.Header
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasItems;

class PageGraphQlServiceTest extends BaseSpringTest{

    @Autowired
    Pages pages;

    @Autowired
    Users users;

    @Autowired
    SignInUser signInUser

    @Autowired
    PasswordEncoder passwordEncoder

    def "should be able to get page by id"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                query{
                    page(uuid: "${page.id}") {
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
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.page.name", equalTo(page.name))
                .body("data.page.id", equalTo(page.id.toString()))
    }

    def "should be able to get page by name"() {
        given:
        def pagename = "DeathStar"
        and:
        def page = pages.trigger(new PageCreated(pagename))
        and:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                query{
                    page(name: "${page.name}") {
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
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.page.name", equalTo(page.name))
                .body("data.page.id", equalTo(page.id.toString()))
    }

    def "should throw error if id is not valid"() {
        given:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                query{
                    page(uuid: "thisisnotauuid") {
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
                .statusCode(200)
                .body("errors[0].message", containsString(ValidationErrorCode.WRONG_FORMAT.name()))
    }

    def "should throw error if parameters are both empty"() {
        given:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                query{
                    page {
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
                .statusCode(200)
                .body("errors[0].message", containsString(ValidationErrorCode.EMPTY_VALUE.name()))
    }

    def "should be able to get all pages"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        def page2 = pages.trigger(new PageCreated("page2"))
        and:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user
        and:
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
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.pages.name", hasItems(page.name, page2.name));
    }
}
