package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import java.util.*

class User (
    var id: UUID,
    var name: String,
    var email: String,
    var roles: Set<AccessRole>
    ) {

}