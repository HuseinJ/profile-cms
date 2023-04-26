package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.application.RefreshTokenOfUser
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import io.restassured.http.Header
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

import static org.hamcrest.Matchers.emptyOrNullString
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasItems
import static org.hamcrest.Matchers.not
import static org.hamcrest.Matchers.notNullValue

class RefreshTokenGraphQlTest extends BaseSpringTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Users users;

    @Autowired
    Pages pages;

    @Autowired
    SignInUser signInUser;

    def "should return an error if jwt token of user expires"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()
        sleep(2000)
        and:
        userAuthServices.callingUser() >> user
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
    }
}
