import { useGraphql } from "../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import { users } from "../store";
import { User } from "../User";

const changePasswordMutation = `
	mutation($oldPassword: String, $newPassword: String){
        changePassword(oldPassword: $oldPassword, newPassword: $newPassword)
    }  
`;

export const changePassword = async (oldPassword: string, newPassword: string) =>  {
    let response = await useGraphql(
        changePasswordMutation, 
        {
            oldPassword,
            newPassword
        }, 
        get(loggedInUser));

    if(response.errors) {
        throw "could not change password"
    }

    console.log(response)

    return true
}