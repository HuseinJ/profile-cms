package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntityRepository
import com.hjusic.api.profileapi.page.model.CreatePageService
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.page.model.UnpublishedPage
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

class AssignHomePageGraphQlServiceTest extends BaseSpringTest {

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    Users users

    @Autowired
    Pages pages

    @Autowired
    CreatePageService createPageService

    @Autowired
    SignInUser signInUser

    @Autowired
    PageDatabaseEntityRepository pageDatabaseEntityRepository

    def "should be able to assign a page as Homepage"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def page = pages.trigger(createPageService.createPage(user, "test-assign+" + Instant.now()).getSuccess())
        and:
        def query = """
                mutation{
                    assignHomePage(id: "${page.id.toString()}") {
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
                .body("data.assignHomePage.name", equalTo(page.name))
    }

    def "should return homepage in query after it is assigned"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def page = pages.trigger(createPageService.createPage(user, "test-assign+" + Instant.now()).getSuccess())
        pages.trigger(((UnpublishedPage) page).assignHomePage(user).getSuccess())
        and:
        def query = """
                query{
                    home {
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
                .body("data.home.name", equalTo(page.name))
    }

    def "should return throw error in query if no homepage is set"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        pageDatabaseEntityRepository.deleteAll()
        and:
        def query = """
                query{
                    home {
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
                .body("errors[0].message", containsString("No Homepage Set"))
    }

    def "should return error if invalid uuid is passed"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    assignHomePage(id: "this.in.not.a.uuid") {
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

    def "should return error if invalid uuid value is empty"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    assignHomePage(id: "") {
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

    def "should return error if page is already a homepage"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def page = pages.trigger(createPageService.createPage(user, "test-assign+" + Instant.now()).getSuccess())
        pages.trigger(((UnpublishedPage) page).assignHomePage(user).getSuccess())
        and:
        def query = """
                mutation{
                    assignHomePage(id: "${page.id.toString()}") {
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
                .body("errors[0].message", containsString(ValidationErrorCode.ACTION_NOT_ALLOWED_FOR_OBJECT.name()))
    }
}
