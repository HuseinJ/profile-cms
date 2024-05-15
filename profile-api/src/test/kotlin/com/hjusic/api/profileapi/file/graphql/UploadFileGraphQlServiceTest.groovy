package com.hjusic.api.profileapi.file.graphql

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.file.infrastructure.FileDatabaseEntityRepository
import com.hjusic.api.profileapi.file.infrastructure.FileState
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import io.restassured.RestAssured
import io.restassured.builder.MultiPartSpecBuilder
import io.restassured.http.Header
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder

import java.nio.file.Files
import java.time.Instant

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo

class UploadFileGraphQlServiceTest extends BaseSpringTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Users users;

    @Autowired
    SignInUser signInUser;

    @Autowired
    FileDatabaseEntityRepository fileDatabaseEntityRepository

    def "should be able to upload a File"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-"+ Instant.now().toString() +"@mail.com", roles, null), passwordEncoder.encode(password)))
        def userTokenTuple = signInUser.signInUser(user.name, password).getSuccess()

        and:
        userAuthServices.callingUser() >> user

        and:
        def tempFile = Files.createTempFile("test", ".txt")
        Files.write(tempFile, "Hello, World!".getBytes())
        def someFile = new MockMultipartFile("file", "test.txt", "text/plain", Files.readAllBytes(tempFile))

        and:
        def query = '''mutation($input: Upload!){uploadFileWithMultipartPOST(input: $input) {id name}}'''
        when:

        def body = RestAssured.given()
                .header("Authorization", "Bearer " + userTokenTuple.token)
                .multiPart("operations", "{ \"query\": \"$query\", \"variables\": { \"input\": null } }")
                .multiPart("map", "{\"0\":[\"variables.input\"]}")
                .multiPart("0", tempFile.toFile())

        def result = body.when().post()

        then:
        result.then()
                .statusCode(200)
                .body("errors", equalTo(null))
                .body("data.uploadFileWithMultipartPOST.name", equalTo(tempFile.toFile().name))

        var files = fileDatabaseEntityRepository.findAll()
        files.size() == 1
        files.get(0).name == tempFile.toFile().name
        files.get(0).state == FileState.REGISTERED
        files.get(0).fileContent == tempFile.toFile().readBytes()
    }
}
