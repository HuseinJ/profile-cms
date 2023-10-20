package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import io.restassured.http.Header
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant
import static org.hamcrest.Matchers.containsString

class UsersGraphQlTest extends BaseSpringTest {

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    SignInUser signInUser


    def "should return an error if the caller is logged in but does not have the correct accessrole"() {
        given:
        def password = "password1"
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", new HashSet<AccessRole>(), null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user1.name, password).getSuccess();
        and:
        def query = """
                query{
                    users {
                        name
                        email
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
                .body("errors[0].message", containsString("Access is denied"))
    }

    def "should return a list of users if the caller is logged in"() {
        given:
        def password = "password1"
        def accessRoleAdminSet = new HashSet<AccessRole>()
        accessRoleAdminSet.add(AccessRoleService.adminRole())

        def admin = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user2", "user2@mail.com", accessRoleAdminSet, null), passwordEncoder.encode(password)))
        def adminTokenTuple = signInUser.signInUser(admin.name, password).getSuccess()

        and:
        def query = """
                query{
                    users {
                        name
                        email
                    }
                }
                """
        and:
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("name", adminTokenTuple.user.name);
        expected.put("email", adminTokenTuple.user.email);
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8")).header(new Header("Authorization", "Bearer " + adminTokenTuple.token))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(200)
                .body("data.users", Matchers.hasItem(expected))
    }

    def "should return an error if the caller is not logged in"() {
        given:
        def query = """
                query{
                    users {
                        name
                        email
                    }
                }
                """
        when:
        def body =
                RestAssured.given().contentType("application/graphql")
                        .body(query.getBytes("UTF-8"))
        def result = body.when().post()
        then:
        result.then()
                .statusCode(200)
                .body("errors[0].message", containsString("Access is denied"))
    }
}
