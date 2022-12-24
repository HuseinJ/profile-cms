import gql from "graphql-tag";

export const LOGGED_IN_USER = gql`
    query loggedInUser{
        loggedInUser {
            email
            name
        }
    }
`;

export const LOGIN_MUTATION = gql`
    mutation signIn($name: String, $password: String) {
        signIn(name: $name, password: $password) {
            token
            type
            user {
                email
                name
            }
        }
    }
`;