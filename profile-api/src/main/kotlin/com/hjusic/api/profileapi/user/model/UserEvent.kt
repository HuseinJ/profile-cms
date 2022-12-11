package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.common.event.Event
import java.time.Instant

abstract class UserEvent(
    val user: User
) : Event(user.id, Instant.now())