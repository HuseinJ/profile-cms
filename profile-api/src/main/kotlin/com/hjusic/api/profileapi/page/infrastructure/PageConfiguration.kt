package com.hjusic.api.profileapi.page.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.application.CreatePage
import com.hjusic.api.profileapi.page.model.CreatePageService
import com.hjusic.api.profileapi.page.model.Pages
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PageConfiguration {

    @Bean
    fun pages(eventPublisher: EventPublisher, pageDatabaseEntityRepository: PageDatabaseEntityRepository): Pages {
        return PagesDatabaseService(eventPublisher, pageDatabaseEntityRepository)
    }

    @Bean
    fun createPageService(): CreatePageService {
        return CreatePageService();
    }

    @Bean
    fun createPage(pages: Pages, createPageService: CreatePageService, userAuthServices: UserAuthServices): CreatePage {
        return CreatePage(pages, createPageService, userAuthServices);
    }
}