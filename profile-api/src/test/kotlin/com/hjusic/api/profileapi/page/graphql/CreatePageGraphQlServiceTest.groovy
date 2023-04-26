package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
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
import static org.hamcrest.Matchers.containsString

class CreatePageGraphQlServiceTest extends BaseSpringTest{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Users users;

    @Autowired
    SignInUser signInUser;

    def "should be able to create a page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def pagename = "someName";
        and:
        def query = """
                mutation{
                    createPage(name: "${pagename}") {
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
                .body("data.createPage.name", equalTo(pagename))
    }

    def "should return error if user does not have access rights to create a page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.guestRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def pagename = "someName";
        and:
        def query = """
                mutation{
                    createPage(name: "${pagename}") {
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
                .body("errors[0].message", containsString(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_CREATE_PAGE.name()))

    }

    def "should return error if name is empty"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        userAuthServices.callingUser() >> user1
        and:
        def pagename = "";
        and:
        def query = """
                mutation{
                    createPage(name: "${pagename}") {
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
}
