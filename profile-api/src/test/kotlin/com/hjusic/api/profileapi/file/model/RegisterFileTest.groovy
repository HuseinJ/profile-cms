package com.hjusic.api.profileapi.file.model

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.user.model.User
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class RegisterFileTest extends Specification{

    def "should throw error if user is not allowed to register the file"() {
        given:
        def guestroles = new HashSet<AccessRole>()
        and:
        def guestUser = new User(UUID.randomUUID(), "rando", "m@mail.com", guestroles, null)
        and:
        def fileToBeRegistered = new UnregisteredFile(UUID.randomUUID(), "naaaameeeee", Mock(MultipartFile.class))
        when:
        def result = fileToBeRegistered.registerFileWithUser(guestUser)
        then:
        result.wasFailure()
        result.getFail().reason == FileDomainErrorCode.USER_IS_NOT_ALLOWED_TO_UPLOAD.toString()
    }

    def "should be able to register file"() {
        given:
        def adminroles = new HashSet<AccessRole>()
        adminroles.add(AccessRoleService.adminRole())
        and:
        def adminUser = new User(UUID.randomUUID(), "rando", "m@mail.com", adminroles, null)
        and:
        def fileToBeRegistered = new UnregisteredFile(UUID.randomUUID(), "naaaameeeee", Mock(MultipartFile.class))
        when:
        def result = fileToBeRegistered.registerFileWithUser(adminUser)
        then:
        result.wasSuccess()
        result.success.aggregateId == fileToBeRegistered.id
    }
}
