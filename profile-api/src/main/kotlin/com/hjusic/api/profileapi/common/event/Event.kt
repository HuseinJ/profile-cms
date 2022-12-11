package com.hjusic.api.profileapi.common.event

import java.time.Instant
import java.util.UUID

open class Event (
    val aggregateId: UUID,
    val thrownAt: Instant,
    )