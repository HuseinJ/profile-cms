package com.hjusic.api.profileapi.accessRole.model

import com.hjusic.api.profileapi.common.event.Event
import java.time.Instant
import java.util.*

abstract class AccessRoleEvent: Event(UUID.randomUUID(), Instant.now())