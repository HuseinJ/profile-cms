package com.hjusic.api.profileapi.user.model

class RefreshTokenCreated(user: User, val refreshToken: RefreshToken) : UserEvent(user)  {
}