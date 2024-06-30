import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';
import { PageComponentName } from "../PageComponentName";
import { useGraphql } from "../../utils/useGraphQl";
import type { PageComponent } from "../PageComponent";

const createPageComponentMutation = `
	mutation($pageId: String, $name: PageCompnentName){
        createPageComponent(name: $name, pageId: $pageId){
            id
            name
            pageid
            componentData {
                key
                value
            }
        }
    }  
`;

export const createPageComponent = async (pageComponentName: PageComponentName, pageId: String): Promise<PageComponent>  => {
    let response = await useGraphql(
        createPageComponentMutation, 
        {name: pageComponentName, pageId}, 
        get(loggedInUser));

    if(response.errors) {
        throw "page component could not be created"
    }
    
    return response.data!.createPageComponent
}