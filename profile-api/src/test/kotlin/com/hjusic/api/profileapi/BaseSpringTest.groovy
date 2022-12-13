package com.hjusic.api.profileapi

import com.hjusic.api.profileapi.common.security.UserAuthServices
import io.restassured.RestAssured
import net.minidev.json.JSONObject
import org.junit.runner.RunWith
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseSpringTest extends Specification{

    @LocalServerPort
    protected int serverPort;

    @Shared
    static MongoDBContainer mongoDBContainer = new      MongoDBContainer("mongo:4.0.18-xenial")

    @SpringBean
    UserAuthServices userAuthServices = Mock()

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        mongoDBContainer.start()
        registry.add("spring.data.mongodb.uri", { -> mongoDBContainer.replicaSetUrl })
    }

    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/graphql"
        RestAssured.port = serverPort;
    }

    private static String graphqlToJson(String payload)
    {
        JSONObject json = new JSONObject();
        json.put("query",payload);
        return  json.toString();
    }
}
