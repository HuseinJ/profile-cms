package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.DomainErrorCode
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

class PageComponentAddedTest extends BaseSpringTest {

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

    def "newly created page should not have any components"() {
        when:
        def page = pages.trigger(new PageCreated("testPage1"))
        then:
        pageComponents.findComponentsOfPage(page.id).size() == 0
    }

    def "should be able to add component to the page"() {
        given:
        def page = pages.trigger(new PageCreated("testPage1"))
        and:
        def componentData = new HashMap<String, String>()
        componentData.put("key1", "value1")
        componentData.put("key2", "value2")
        componentData.put("key3", "value3")
        when:
        def component = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                componentData),
                page))

        then:
        def componentList = pageComponents.findComponentsOfPage(page.id)
        componentList.size() == 1
        componentList.first().componentName == component.componentName
        componentList.first().componentData.containsKey("key1")
        componentList.first().componentData.containsKey("key2")
        componentList.first().componentData.containsKey("key3")
        componentList.first().componentData.get("key1") == "value1"
        componentList.first().pageid == page.id
    }

    def "should be able to add multiple component to the page with ASC order"() {
        given:
        def page = pages.trigger(new PageCreated("testPage1Order"))
        and:
        def componentData = new HashMap<String, String>()
        componentData.put("key1", "value1")
        componentData.put("key2", "value2")
        componentData.put("key3", "value3")
        when:
        pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                componentData),
                page))
        pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.HEADER,
                componentData),
                page))

        then:
        def componentList = pageComponents.findComponentsOfPage(page.id)
        componentList.size() == 2
        def c1 = componentList.stream().find { it -> it.componentName == PageComponentName.PARAGRAPH}
        def c2 = componentList.stream().find { it -> it.componentName == PageComponentName.HEADER}

        c1 != null && c2 != null
        ((PageComponent) c1).order == 1
        ((PageComponent) c2).order == 2
    }

    def "should throw error if user does not have access right to add component to Page"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.guestRole())
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        when:
        def result = page.addComponent(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                user1)
        then:
        result.fail.reason == PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name()
    }

    def "should be able to add component to page with user who has access right"() {
        given:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user1 = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def page = pages.trigger(new PageCreated("test"))
        when:
        def result = page.addComponent(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                new HashMap<String, String>()),
                user1)
        then:
        result.success != null
        result.success.pageComponent.componentName == PageComponentName.PARAGRAPH
    }
}
