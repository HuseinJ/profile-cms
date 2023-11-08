package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

class PageComponentSwitchedTest extends BaseSpringTest{

    @Autowired
    PageComponents pageComponents;

    @Autowired
    Users users;

    @Autowired
    Pages pages

    @Autowired
    PasswordEncoder passwordEncoder

    def "Should be able to switch page components in user has access right to modify page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent3 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent4 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        when:
        def event1 = page.switchComponents(pageComponent1, pageComponent4, user)
        def event2 = page.switchComponents(pageComponent2, pageComponent3, user)
        pageComponents.trigger(event1.getSuccess())
        pageComponents.trigger(event2.getSuccess())
        then:
        event1.wasSuccess()
        event2.wasSuccess()

        pageComponents.findComponentsOfPage(page.id, pageComponent1.id).order == 4
        pageComponents.findComponentsOfPage(page.id, pageComponent2.id).order == 3
        pageComponents.findComponentsOfPage(page.id, pageComponent3.id).order == 2
        pageComponents.findComponentsOfPage(page.id, pageComponent4.id).order == 1
    }

    def "Should not be able to switch page components in user does not have access right to modify page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.guestRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent3 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        def pageComponent4 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                page))
        when:
        def event1 = page.switchComponents(pageComponent1, pageComponent4, user)
        def event2 = page.switchComponents(pageComponent2, pageComponent3, user)
        then:
        event1.wasFailure()
        event2.wasFailure()

        pageComponents.findComponentsOfPage(page.id, pageComponent1.id).order == 1
        pageComponents.findComponentsOfPage(page.id, pageComponent2.id).order == 2
        pageComponents.findComponentsOfPage(page.id, pageComponent3.id).order == 3
        pageComponents.findComponentsOfPage(page.id, pageComponent4.id).order == 4
    }
}
