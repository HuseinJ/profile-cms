import { User } from "../users/User";

export class LoggedInUser {
    jwtToken: String;
    refreshToken: String;
    authType: String;
    user: User

    constructor(jwtToken: String, refreshToken: String, user: User, authType = "Bearer") {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.authType = authType;
        this.user = user;
    }
}