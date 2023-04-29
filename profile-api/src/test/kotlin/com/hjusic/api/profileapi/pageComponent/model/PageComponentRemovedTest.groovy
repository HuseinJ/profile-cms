package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

class PageComponentRemovedTest extends BaseSpringTest{

    @Autowired
    PageComponents pageComponents
    @Autowired
    Pages pages
    @Autowired
    PasswordEncoder passwordEncoder
    @Autowired
    Users users
    @Autowired
    SignInUser signInUser

    def "should be able to remove component of page with user who has access right"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
        def pageComponent2 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
        when:
        pageComponents.trigger(pageComponent1.removePageComponent(user1).getSuccess())
        then:
        def components = pageComponents.findComponentsOfPage(page.id)
        components.size() == 1
        components.first().id == pageComponent2.id
    }

    def "should throw error if user without permission tries to delete component"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.guestRole())
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        def pageComponent1 = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>(),
                UUID.randomUUID()),
                page))
        when:
        def result = pageComponent1.removePageComponent(user1)
        then:
        result.getSuccess() == null
        result.getFail().reason == PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name()
    }
}
