import { useGraphql } from "../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import type { Page } from "../Page";

const assignHomePageMutation = `
	mutation($pageid: String){
        assignHomePage(id: $pageid){
            id
            name
        }
    }  
`;

export const assignHomePage = async (pageid: String): Promise<Page> => {
    let response = await useGraphql(
        assignHomePageMutation, 
        {pageid}, 
        get(loggedInUser));

    if(response.errors) {
        throw "page component could not be created"
    }

    console.log(response)

    return response.data.assignHomePage
}