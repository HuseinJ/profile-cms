package com.hjusic.api.profileapi

import com.hjusic.api.profileapi.common.security.UserAuthServices
import org.junit.After
import org.junit.jupiter.api.AfterAll
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@SpringBootTest
class BaseSpringTest extends Specification{

    @Shared
    static MongoDBContainer mongoDBContainer = new      MongoDBContainer("mongo:4.0.18-xenial")

    @SpringBean
    UserAuthServices userAuthServices = Mock()

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        mongoDBContainer.start()
        registry.add("spring.data.mongodb.uri", { -> mongoDBContainer.replicaSetUrl })
    }
}
