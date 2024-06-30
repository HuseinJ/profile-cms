import { useGraphql } from "../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import type { Page } from "../Page";

const createNewPageMutation = `
	mutation($name: String){
        createPage(name: $name){
            id
            name
            pageComponents{
                id
                name
                pageid
                componentData{
                    key
                    value
                }
            }
        }
    }  
`;

export const createNewPage = async (pageName: String): Promise<Page> => {
    let response = await useGraphql(
        createNewPageMutation, 
        {name: pageName}, 
        get(loggedInUser));

    if(response.errors) {
        throw "page component could not be created"
    }

    console.log(response)

    return response.data.createPage
}