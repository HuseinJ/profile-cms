import { useGraphql } from "../../utils/useGraphQl";
import { PageComponent } from "../PageComponent";
import { loggedInUser } from "../../auth/store";
import { get } from 'svelte/store';

const removePageComponentMutation = `
	mutation($pageId: String, $pageComponentId: String){
        removePageComponent(pageComponentId: $pageComponentId, pageId: $pageId) {
  	        id
            pageid
            name
            componentData{
            key
            value
            }
        }
    }  
`;

export const removePageComponent = async (pageComponent: PageComponent): Promise<Boolean> => {
    let response = await useGraphql(
        removePageComponentMutation, 
        {pageComponentId: pageComponent.id, pageId: pageComponent.pageid}, 
        get(loggedInUser));

    if(response.errors) {
        console.log("error!! trigger error state")
        return false;
    }
    return true
}