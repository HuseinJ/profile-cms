package com.hjusic.api.profileapi.common.error

class ValidationError(
    reason: ValidationErrorCode
): ContextError(reason.name)