import { useGraphql } from "../../utils/useGraphQl";
import { User } from "../../users/User";
import { LoggedInUser } from "../LoggedInUser";
import { setLoggedInUser} from "../store";
import { goto } from "$app/navigation";

const signInQuery = `
	mutation SignIn($name: String!, $password: String!) {
		signIn(name: $name, password: $password) {
		    token
			refreshToken
			type
			user {
				email
				name
			}
		}
	}
`;

export const signIn = async (name: String, password: String) =>  {
    let signInRequestData = await useGraphql(signInQuery, {
        name,
        password
    }, undefined)

    if(signInRequestData.errors) {
        console.log("error!! trigger error state")
    }

    const signInData = signInRequestData.data.signIn;
    const user = new User(signInData.user.name, signInData.user.email)
    const mappedLoggedInUser = new LoggedInUser(
        signInData.token, 
        signInData.refreshToken,
        user,
        signInData.type)

    setLoggedInUser(mappedLoggedInUser)
    goto("/")
}