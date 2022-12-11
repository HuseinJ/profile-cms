package com.hjusic.api.profileapi.common.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfiguration {

    @Bean
    fun eventPublisher(applicationEventPublisher: ApplicationEventPublisher): EventPublisher {
        return EventPublisher(applicationEventPublisher)
    }
}