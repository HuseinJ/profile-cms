package com.hjusic.api.profileapi.pageComponent.infrastucture

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntityRepository
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PageComponentConfiguration {

    @Bean
    fun pageComponents(
        eventPublisher: EventPublisher,
        pageDatabaseEntityRepository: PageDatabaseEntityRepository
    ): PageComponents {
        return PageComponentsDatabaseService(eventPublisher, pageDatabaseEntityRepository)
    }
}