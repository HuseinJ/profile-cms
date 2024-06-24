import { useGraphql } from "../../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import { users } from "../store";

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

    console.log(usersRequestData)

    users.set([])
}