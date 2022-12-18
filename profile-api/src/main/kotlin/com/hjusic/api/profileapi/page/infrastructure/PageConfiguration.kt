package com.hjusic.api.profileapi.page.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.model.Pages
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PageConfiguration {

    @Bean
    fun Pages(eventPublisher: EventPublisher, pageDatabaseEntityRepository: PageDatabaseEntityRepository): Pages {
        return PagesDatabaseService(eventPublisher, pageDatabaseEntityRepository)
    }
}