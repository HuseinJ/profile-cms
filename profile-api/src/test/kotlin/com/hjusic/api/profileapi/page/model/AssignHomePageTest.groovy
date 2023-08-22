package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntityRepository
import com.hjusic.api.profileapi.page.infrastructure.PageEntityType
import com.hjusic.api.profileapi.user.model.User
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class AssignHomePageTest extends BaseSpringTest {

    @Autowired
    Pages pages

    @Autowired
    CreatePageService createPageService

    @Autowired
    PageDatabaseEntityRepository pageDatabaseEntityRepository

    def "should return error if user is not allowed to assign home page"() {
        given:
        def adminroles = new HashSet<AccessRole>()
        adminroles.add(AccessRoleService.adminRole())
        and:
        def adminUser = new User(UUID.randomUUID(), "rando", "m@mail.com", adminroles, null)
        and:
        def guestroles = new HashSet<AccessRole>()
        guestroles.add(AccessRoleService.guestRole())
        and:
        def guestUser = new User(UUID.randomUUID(), "rando", "m@mail.com", guestroles, null)
        and:
        def unpublishedPage = pages.trigger(createPageService.createPage(adminUser, "test").getSuccess())
        when:
        def result = ((UnpublishedPage) unpublishedPage).assignHomePage(guestUser)
        then:
        result.wasFailure()
        result.getFail().reason == PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_ASSIGN_HOMEPAGE.name()
    }

    def "should Assign HomePage if user is allowed to assign"() {
        given:
        def adminroles = new HashSet<AccessRole>()
        adminroles.add(AccessRoleService.adminRole())
        and:
        def adminUser = new User(UUID.randomUUID(), "rando", "m@mail.com", adminroles, null)
        and:
        def unpublishedPage = pages.trigger(createPageService.createPage(adminUser, "test23").getSuccess())
        when:
        def result = ((UnpublishedPage) unpublishedPage).assignHomePage(adminUser)
        then:
        result.wasSuccess()
    }

    def "should allow only one HomePAge To exist"() {
        given:
        def adminroles = new HashSet<AccessRole>()
        adminroles.add(AccessRoleService.adminRole())
        and:
        def adminUser = new User(UUID.randomUUID(), "rando", "m@mail.com", adminroles, null)
        and:
        def unpublishedPage1 = pages.trigger(createPageService.createPage(adminUser, "test23"+ Instant.now()).getSuccess())
        def unpublishedPage2 = pages.trigger(createPageService.createPage(adminUser, "test23"+ Instant.now()).getSuccess())
        def unpublishedPage3 = pages.trigger(createPageService.createPage(adminUser, "test23"+ Instant.now()).getSuccess())
        def unpublishedPage4 = pages.trigger(createPageService.createPage(adminUser, "test23"+ Instant.now()).getSuccess())
        and:
        def result1 = ((UnpublishedPage) unpublishedPage1).assignHomePage(adminUser)
        def result2 = ((UnpublishedPage) unpublishedPage2).assignHomePage(adminUser)
        def result3 = ((UnpublishedPage) unpublishedPage3).assignHomePage(adminUser)
        def result4 = ((UnpublishedPage) unpublishedPage4).assignHomePage(adminUser)
        when:
        pages.trigger(result1.getSuccess())
        pages.trigger(result2.getSuccess())
        pages.trigger(result3.getSuccess())
        pages.trigger(result4.getSuccess())
        then:
        var homePages = pageDatabaseEntityRepository.findByPageType(PageEntityType.HOME_PAGE);
        homePages.size() == 1
        homePages.first().name == unpublishedPage4.name
        homePages.first().id == unpublishedPage4.id
    }
}
