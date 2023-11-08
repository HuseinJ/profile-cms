package com.hjusic.api.profileapi.pageComponent.infrastucture

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntityRepository
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.application.CreatePageComponent
import com.hjusic.api.profileapi.pageComponent.application.RemovePageComponent
import com.hjusic.api.profileapi.pageComponent.application.SwitchPageComponents
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

    @Bean
    fun createPageComponent(pageComponents: PageComponents, pages: Pages): CreatePageComponent{
        return CreatePageComponent(pageComponents, pages)
    }

    @Bean
    fun removePageComponent(pageComponents: PageComponents, pages: Pages): RemovePageComponent {
        return RemovePageComponent(pageComponents, pages)
    }

    @Bean
    fun switchPageComponents(pageComponents: PageComponents, pages: Pages): SwitchPageComponents{
        return SwitchPageComponents(pageComponents, pages)
    }
}