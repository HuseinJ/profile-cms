package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageCreated
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
import static org.hamcrest.Matchers.hasItems

class SwitchPageComponentsGraphQlTest extends BaseSpringTest{
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

    def "should be able to switch page components"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent3 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent4 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
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
                    switchComponents(pageId: "${page.id.toString()}", firstComponentId: "${pageComponent1.id.toString()}", secondComponentId: "${pageComponent4.id.toString()}"){
                        id
                        name
                        pageComponents {
                            id
                            name
                        }
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
                .body("data.switchComponents.id", equalTo(page.id.toString()))
                .body("data.switchComponents.pageComponents.id", hasItems(pageComponent1.id.toString(), pageComponent2.id.toString(), pageComponent3.id.toString(), pageComponent4.id.toString()))

        pageComponents.findComponentsOfPage(page.id, pageComponent1.id).order == 4
        pageComponents.findComponentsOfPage(page.id, pageComponent2.id).order == 2
        pageComponents.findComponentsOfPage(page.id, pageComponent3.id).order == 3
        pageComponents.findComponentsOfPage(page.id, pageComponent4.id).order == 1
    }

    def "should throw validation error if any parameter is invalid"() {
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
                    switchComponents(pageId: "${pageid}", firstComponentId: "${fcId}", secondComponentId: "${scId}"){
                        id
                        name
                        pageComponents {
                            id
                            name
                        }
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
                .body("errors[0].message", containsString(errorType))
        where:

        pageid  |   fcId    |   scId | errorType
        ""      |   ""      |   ""  |   ValidationErrorCode.EMPTY_VALUE.name()
        ""      |   ""      |   randomUUID()    |   ValidationErrorCode.EMPTY_VALUE.name()
        ""      |   randomUUID()    | ""    |   ValidationErrorCode.EMPTY_VALUE.name()
        randomUUID()     |   ""      |   "" |   ValidationErrorCode.EMPTY_VALUE.name()
        randomUUID()      |   ""      |   randomUUID()  |   ValidationErrorCode.EMPTY_VALUE.name()
        ""      |   randomUUID()    | randomUUID()  |   ValidationErrorCode.EMPTY_VALUE.name()
        randomUUID()      |   randomUUID()    | ""  |   ValidationErrorCode.EMPTY_VALUE.name()
        randomUUID()     |   ""      |   randomUUID()   |   ValidationErrorCode.EMPTY_VALUE.name()
        "never"     |   "gonna"      |   "give"   |   ValidationErrorCode.WRONG_FORMAT.name()
        randomUUID()     |   "you up"      |   "never gonna let"   |   ValidationErrorCode.WRONG_FORMAT.name()
        "you down"    |   randomUUID()     |   "never gonna"   |   ValidationErrorCode.WRONG_FORMAT.name()
        "turn around"    |   randomUUID()     |   randomUUID()    |   ValidationErrorCode.WRONG_FORMAT.name()
        randomUUID()    |   randomUUID()     |   "and-desert-you"   |   ValidationErrorCode.WRONG_FORMAT.name()
        randomUUID()    |   randomUUID()     |   randomUUID()  |   ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should throw error if page and pageComponent do not match"() {
        given:
        def page1 = pages.trigger(new PageCreated("page1"))
        def page2 = pages.trigger(new PageCreated("page2"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page1))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page1))
        def pageComponent3 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page2))
        def pageComponent4 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page2))
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
                    switchComponents(pageId: "${page2.id.toString()}", firstComponentId: "${pageComponent1.id.toString()}", secondComponentId: "${pageComponent3.id.toString()}"){
                        id
                        name
                        pageComponents {
                            id
                            name
                        }
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
                .body("errors[0].message", containsString("given pageComponent is not valid"))
    }

    private String randomUUID(){
        return UUID.randomUUID().toString()
    }
}
