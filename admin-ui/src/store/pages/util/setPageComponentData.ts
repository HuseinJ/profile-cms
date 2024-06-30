import { get } from "svelte/store";
import { loggedInUser } from "../../auth/store";
import { useGraphql } from "../../utils/useGraphQl";
import type { PageComponent } from "../PageComponent";

const setPageComponentDataMutation = `
	mutation($pageId: String, $pageComponentId: String, $componentData: [ComponentDataInput]){
  setPageComponentData(pageId: $pageId, pageComponentId: $pageComponentId, componentData: $componentData){
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


export const setPageComponentData = async (pageComponent: PageComponent): Boolean =>  {
    const result = await useGraphql(
        setPageComponentDataMutation, 
        {
            "pageId": pageComponent.pageid, 
            "pageComponentId": pageComponent.id, 
            "componentData": pageComponent.componentData.map(cData => ({
                key: cData.key, 
                value: cData.value
            }))
        }, 
        get(loggedInUser)
    );
    if(result.errors) {
        console.log("error!! trigger error state")
        return false;
    }

    return true;
}