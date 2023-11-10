package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.page.model.PageCreated
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.UserCreated
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.time.Instant

class PageComponentDataSetTest extends BaseSpringTest {
    @Autowired
    PageComponents pageComponents;

    @Autowired
    Users users;

    @Autowired
    Pages pages

    @Autowired
    PasswordEncoder passwordEncoder

    def "should be able to set PageComponentData"() {
        given:
        def page = pages.trigger(new PageCreated("testPage1"))
        and:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def componentData = new HashMap<String, String>()
        def component = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                componentData),
                page))
        and:
        def newComponentData = new HashMap<String, String>()
        newComponentData.put("mando", "grogu")
        newComponentData.put("obiwan", "anakin")
        newComponentData.put("luke", "r2")
        when:
        def result = component.setComponentData(newComponentData, user, page)
        pageComponents.trigger(result.success)
        then:
        def savedComponent = pageComponents.findComponentsOfPage(page.id, component.id)
        savedComponent.id == component.id
        savedComponent.componentName == component.componentName
        savedComponent.componentData == newComponentData
    }

    def "should return error if user does not have access rights to modify page"() {
        given:
        def page = pages.trigger(new PageCreated("testPage1"))
        and:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.guestRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def componentData = new HashMap<String, String>()
        def component = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                componentData),
                page))
        and:
        def newComponentData = new HashMap<String, String>()
        newComponentData.put("mando", "grogu")
        newComponentData.put("obiwan", "anakin")
        newComponentData.put("luke", "r2")
        when:
        def result = component.setComponentData(newComponentData, user, page)
        then:
        result.wasFailure()
        result.fail.reason == PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name()
    }

    def "should override old pageComponent configuration"() {
        given:
        def page = pages.trigger(new PageCreated("testPage1"))
        and:
        def password = "password1"
        def roles = new HashSet<AccessRole>()
        roles.add(AccessRoleService.adminRole())
        def user = users.trigger(new UserCreated(new User(UUID.randomUUID(), "user-" + Instant.now().toString(), "user-" + Instant.now().toString() + "@mail.com", roles, null), passwordEncoder.encode(password)))
        and:
        def componentData = new HashMap<String, String>()
        componentData.put("rick", "morty")
        componentData.put("stan", "kyle")
        componentData.put("frodo", "bilbo")
        def component = pageComponents.trigger(new PageComponentAdded(new PageComponent(UUID.randomUUID(),
                PageComponentName.PARAGRAPH,
                componentData),
                page))
        and:
        def newComponentData = new HashMap<String, String>()
        newComponentData.put("mando", "grogu")
        newComponentData.put("obiwan", "anakin")
        newComponentData.put("luke", "r2")
        when:
        def result = component.setComponentData(newComponentData, user, page)
        pageComponents.trigger(result.success)
        then:
        def savedComponent = pageComponents.findComponentsOfPage(page.id, component.id)
        savedComponent.id == component.id
        savedComponent.componentName == component.componentName
        savedComponent.componentData == newComponentData
    }
}
