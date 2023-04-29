package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponentAdded
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

class RemovePageComponentGraphQlServiceTest extends BaseSpringTest {

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

    def "should be able to remove a component from a Page"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
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
                    removePageComponent(pageId: "${page.id.toString()}", pageComponentId: "${pageComponent1.id.toString()}"){
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
                .body("errors", equalTo(null))
                .body("data.removePageComponent.pageid", equalTo(page.id.toString()))
                .body("data.removePageComponent.id", equalTo(pageComponent1.id.toString()))

        pageComponents.findComponentsOfPage(page.id).first().id == pageComponent2.id
    }

    def "should throw validation error if pageid is not valid"() {
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
                    removePageComponent(pageId: "not valid uuid ", pageComponentId: "${UUID.randomUUID().toString()}"){
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

    def "should throw validation error if component id is not valid"() {
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
                    removePageComponent(pageId: "${UUID.randomUUID().toString()}", pageComponentId: "not valid comonent id"){
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

    def "should throw validation error if component id is empty"() {
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
                    removePageComponent(pageId: "${UUID.randomUUID().toString()}", pageComponentId: ""){
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

    def "should throw validation error if page id is empty"() {
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
                    removePageComponent(pageId: "", pageComponentId: "${UUID.randomUUID().toString()}"){
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

    def "should throw security exception if user does not have acess right to modify Page"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
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
                    removePageComponent(pageId: "${page.id.toString()}", pageComponentId: "${pageComponent1.id.toString()}"){
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

    def "should throw error if page does not exist"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
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
                    removePageComponent(pageId: "${UUID.randomUUID().toString()}", pageComponentId: "${pageComponent1.id.toString()}"){
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
                .body("errors[0].message", containsString("given page is not valid"));
    }

    def "should throw error if component id does not exist"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
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
                    removePageComponent(pageId: "${page.id.toString()}", pageComponentId: "${UUID.randomUUID().toString()}"){
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
                .body("errors[0].message", containsString("given pageComponent is not valid"));
    }

}
