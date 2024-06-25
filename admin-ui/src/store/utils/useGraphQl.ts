
import type { LoggedInUser } from "../auth/LoggedInUser";
import { goto } from "$app/navigation";
import { logoutUser } from "../auth/store";

type GraphQLResponse<T> = {
    data?: T;
    errors?: any[];
};

export const useGraphql = async <T>(
    query: string,
    variables: Record<string, any> = {},
    loggedInUser?: LoggedInUser
): Promise<GraphQLResponse<T>> => {

    var headers: Record<string, string> = {
        'Content-Type': 'application/json',
        'Authorization': ""
    };

    if (loggedInUser) {
        headers = {
            ...headers,
            'Authorization': buildAuthenticationHeader(loggedInUser)
        }
    }

    try {
        let response = await fetch('https://cms.hjusic.com/graphql', {
            method: 'POST',
            headers,
            body: JSON.stringify({
                query,
                variables
            })
        });

        if (!response.ok) {
            if (response.status === 401) {
                console.error('Unauthorized: You need to log in again.');
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        let data: GraphQLResponse<T> = await response.json();
        return data;

    } catch (error) {
        logoutUser()
        goto("/login")
        throw error
    }
};

const buildAuthenticationHeader = (loggedInUser: LoggedInUser) => {
    return `${loggedInUser.authType} ${loggedInUser.jwtToken}`
}