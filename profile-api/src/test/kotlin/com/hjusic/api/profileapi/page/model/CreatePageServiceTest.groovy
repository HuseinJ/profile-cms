package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.user.model.User
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class CreatePageServiceTest extends BaseSpringTest{

    @Autowired
    CreatePageService createPageService;

    @Autowired
    Pages pages;

    def "should return create Page event"() {
        given:
            def pageName = "page-" + Instant.now().toEpochMilli()
        and:
            def roles = new HashSet<AccessRole>();
            roles.add(AccessRoleService.adminRole());
        and:
            def callingUser = new User(UUID.randomUUID(), pageName, pageName + "@mail.com", roles)
        when:
            def result = createPageService.createPage(callingUser, pageName)
        then:
            result.wasSuccess()
    }

    def "should return error is user does not have create Page rights"() {
        given:
        def pageName = "page-" + Instant.now().toEpochMilli()
        and:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.guestRole());
        and:
        def callingUser = new User(UUID.randomUUID(), pageName, pageName + "@mail.com", roles)
        when:
        def result = createPageService.createPage(callingUser, pageName)
        then:
        result.wasFailure()
    }

    def "Should be able to fetch created page from the database"() {
        given:
        def pageName = "page-" + Instant.now().toEpochMilli()
        and:
        def roles = new HashSet<AccessRole>();
        roles.add(AccessRoleService.adminRole());
        and:
        def callingUser = new User(UUID.randomUUID(), pageName, pageName + "@mail.com", roles)
        when:
        def createdPage = pages.trigger(createPageService.createPage(callingUser, pageName).getSuccess())
        then:
        def dbpage = pages.findPageById(createdPage.id).get()
        dbpage.id == createdPage.id;
        dbpage.name == createdPage.name
        dbpage instanceof UnpublishedPage
    }
}
