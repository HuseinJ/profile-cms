package com.hjusic.api.profileapi.common.event

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import java.time.Instant

class EventPublisherTest extends Specification{

    def "should publish a given event"() {
        given:
            def applicationEventPublisher = Mock(ApplicationEventPublisher.class)
        and:
            def eventPublisher = new EventPublisher(applicationEventPublisher);
        when:
            eventPublisher.publish(new Event(UUID.randomUUID(), Instant.now()))
        then:
            1 * applicationEventPublisher.publishEvent(_)
    }
}
