package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
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

class DeletePageGraphQlServiceTest extends BaseSpringTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Users users;

    @Autowired
    Pages pages;

    @Autowired
    SignInUser signInUser;

    def "should be able to delete a page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def page = pages.trigger(new PageCreated("test"));
        and:
        def query = """
                mutation{
                    deletePage(id: "${page.id}") {
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
                .body("data.deletePage.name", equalTo(page.name))

        pages.findPageById(page.id).isEmpty()
    }

    def "should throw error if user does not have access right"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.guestRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def page = pages.trigger(new PageCreated("test"));
        and:
        def query = """
                mutation{
                    deletePage(id: "${page.id}") {
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
                .body("errors[0].message", containsString(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_DELETE_PAGE.name()))
        pages.findPageById(page.id).isPresent()
    }

    def "should throw error if page id is empty"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.guestRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def page = pages.trigger(new PageCreated("test"));
        and:
        def query = """
                mutation{
                    deletePage(id: "") {
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
        pages.findPageById(page.id).isPresent()
    }

    def "should throw error if page does not exist"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.guestRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def query = """
                mutation{
                    deletePage(id: "${UUID.randomUUID()}") {
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
                .body("errors[0].message", containsString(PageDomainErrorCode.NO_SUCH_PAGE_WITH_THIS_ID.name()))
    }
}
