package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import spock.lang.Specification

class ChangePasswordTest extends Specification{

    def "should return change Password event"() {
        given:
        def user = new User(UUID.randomUUID(), "name", "mail", new HashSet<AccessRole>());
        and:
        def newPassword = "newPassword";
        when:
        def changePasswordEvent = user.changePassword(newPassword);
        then:
        changePasswordEvent != null;
        changePasswordEvent.user == user;
        changePasswordEvent.newPassword == newPassword;
    }
}
