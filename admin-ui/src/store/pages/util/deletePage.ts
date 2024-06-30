import { useGraphql } from "../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import type { Page } from "../Page";

const deletePageMutation = `
	mutation($pageid: String){
        deletePage(id: $pageid){
            id
            name
        }
    }  
`;

export const deletePage = async (pageid: String): Promise<Page> => {
    let response = await useGraphql(
        deletePageMutation, 
        {pageid}, 
        get(loggedInUser));

    if(response.errors) {
        throw "page component could not be created"
    }

    console.log(response)

    return response.data.deletePage
}