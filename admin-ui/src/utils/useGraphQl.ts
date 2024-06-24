
import type { LoggedInUser } from "../store/auth/LoggedInUser";

export const useGraphql = async (query: String, variables = {}, loggedInUser: LoggedInUser|undefined) => {

    var headers = {
        'Content-Type': 'application/json',
        'Authorization': ""
    }

    if(loggedInUser) {
        headers = {
            ...headers,
            'Authorization': buildAuthenticationHeader(loggedInUser)
        }
    }

    let response = await fetch('https://cms.hjusic.com/graphql', {
        method: 'POST',
        headers,
        body: JSON.stringify({
            query,
            variables
        })
    });

    return await response.json();
}

const buildAuthenticationHeader = (loggedInUser: LoggedInUser) => {
    return `${loggedInUser.authType} ${loggedInUser.jwtToken}`
}