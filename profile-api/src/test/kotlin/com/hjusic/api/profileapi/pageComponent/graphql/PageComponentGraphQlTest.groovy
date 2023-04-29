package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
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
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.hasItems;

class PageComponentGraphQlTest extends BaseSpringTest {

    @Autowired
    Pages pages;

    @Autowired
    PageComponents pageComponents;

    @Autowired
    Users users;

    @Autowired
    SignInUser signInUser

    @Autowired
    PasswordEncoder passwordEncoder

    def "should be able to get pageComponents for page by id"() {
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
        def pageComponent3 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
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
                        pageComponents{
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
                .body("data.page.id", equalTo(page.id.toString()))
                .body("data.page.pageComponents", hasSize(3))
                .body("data.page.pageComponents.id", hasItems(pageComponent1.id.toString(), pageComponent2.id.toString(), pageComponent3.id.toString()))
    }

    def "should not have any pageComponents if page was newly created"() {
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
                        pageComponents{
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
                .body("data.page.id", equalTo(page.id.toString()))
                .body("data.page.pageComponents", hasSize(0))
    }
}
