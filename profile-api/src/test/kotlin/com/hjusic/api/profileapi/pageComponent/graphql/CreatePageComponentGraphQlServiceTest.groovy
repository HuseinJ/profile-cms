package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponentName
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
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

class CreatePageComponentGraphQlServiceTest extends BaseSpringTest {

    @Autowired
    Pages pages

    @Autowired
    PageComponents pageComponents

    @Autowired
    Users users

    @Autowired
    SignInUser signInUser

    @Autowired
    PasswordEncoder passwordEncoder

    def "should be able to create a pageComponent for a page"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: PARAGRAPH, pageId: "${page.id}"){
                        id
                        name
                        pageid
                    }
                }
                """
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8")).header(new Header("Authorization", "Bearer " + userTokenTuple.token))
        def result = body.when().post()
        then:
        def createdComponent = pageComponents.findComponentsOfPage(page.id).first()
        createdComponent.componentName == PageComponentName.PARAGRAPH
        result.then()
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.createPageComponent.pageid", equalTo(page.id.toString()))
                .body("data.createPageComponent.pageid", equalTo(createdComponent.pageid.toString()))
                .body("data.createPageComponent.id", equalTo(createdComponent.id.toString()))
    }

    def "should throw validation error if pageid is not valid"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: PARAGRAPH, pageId: "some random id which i snot a uuid lol"){
                        id
                        name
                        pageid
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

    def "should throw validation error if pageid is empty"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: PARAGRAPH, pageId: ""){
                        id
                        name
                        pageid
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

    def "should throw validation error if componentName is not valid"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: THISISNOTACOMPONENT, pageId: "${page.id}"){
                        id
                        name
                        pageid
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
                .body("errors[0].message", containsString("Validation error of type WrongType"))
    }

    def "should throw validation error if page does not exist"() {
        given:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: PARAGRAPH, pageId: "${UUID.randomUUID().toString()}"){
                        id
                        name
                        pageid
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

    def "should throw security error if user is not authorized to modify page"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.guestRole())
        def password = "supersecrettestpassword"
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        and:
        userAuthServices.callingUser() >> user
        and:
        def query = """
                mutation{
                    createPageComponent(name: PARAGRAPH, pageId: "${page.id}"){
                        id
                        name
                        pageid
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
                .body("errors[0].message", containsString(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name()))
    }
}
