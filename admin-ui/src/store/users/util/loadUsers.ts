import { useGraphql } from "../../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import { users } from "../store";
import { User } from "../User";

const loadUsersMutation = `
	query{
        users {
            name
            email
        }
    }
`;

export const loadUsers = async () =>  {
    console.log("oipen")
    let usersRequestData = await useGraphql(loadUsersMutation, {}, get(loggedInUser))

    if(usersRequestData.errors) {
        console.log("error!! trigger error state")
    }

    users.set(
        usersRequestData.data.users.map((user:any) => new User(user.name, user.email))
    )
}