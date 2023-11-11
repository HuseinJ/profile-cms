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
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasItems

class SetPageComponentDataGraphQlSeriveTest extends BaseSpringTest{
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

    def "should be able to set component data"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
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

        and:
        def query = """
                mutation{
                     setPageComponentData(componentData: [{key: "rick", value: "stan"}, {key: "morty", value: "kyle"}, {key: "summer", value: "kenny"}], pageComponentId: "${pageComponent1.id.toString()}", pageId: "${page.id.toString()}"){
                        id
                        name
                        pageid
                        componentData {
                            key
                            value
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
                .body("data.setPageComponentData.pageid", equalTo(page.id.toString()))
                .body("data.setPageComponentData.id", equalTo(pageComponent1.id.toString()))
                .body("data.setPageComponentData.name", equalTo(pageComponent1.componentName.name()))

        def component = pageComponents.findComponentsOfPage(page.id, pageComponent1.id)
        component.componentData.size() == 3
        component.componentData.get("rick") == "stan"
        component.componentData.get("morty") == "kyle"
        component.componentData.get("summer") == "kenny"
    }

    def "should throw validation exception"() {
        given:
        def page = pages.trigger(new PageCreated("page1"))
        and:
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
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

        and:
        def query = """
                mutation{
                     setPageComponentData(componentData: [${componentData}], pageComponentId: "${componentId}", pageId: "${pageid}"){
                        id
                        name
                        pageid
                        componentData {
                            key
                            value
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
        pageid  |   componentId |   componentData   |   errorType
        ""  |   ""  |   ""  |   ValidationErrorCode.EMPTY_VALUE.name()
        ""  |   UUID.randomUUID().toString()   |   ""  |   ValidationErrorCode.EMPTY_VALUE.name()
        UUID.randomUUID().toString()  |   ""  |   ""  |   ValidationErrorCode.EMPTY_VALUE.name()
        UUID.randomUUID().toString()  |   UUID.randomUUID().toString()  |   ""  |   ValidationErrorCode.EMPTY_VALUE.name()
    }
}
