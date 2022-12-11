package com.hjusic.api.profileapi.common.event

import org.springframework.context.ApplicationEventPublisher

class EventPublisher(
    val applicationPublisher: ApplicationEventPublisher
) {
    fun publish(event: Event) {
        applicationPublisher.publishEvent(event)
    }
}