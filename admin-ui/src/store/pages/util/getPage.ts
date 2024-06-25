
import { get } from "svelte/store";
import { pages } from "../store"
import { useGraphql } from "../../utils/useGraphQl";
import { loggedInUser } from "../../auth/store";
import { ComponentData } from "../ComponentData";
import { PageComponent } from "../PageComponent";
import { Page } from "../Page";


const loadPagesQuery = `
	query{
        pages {
            id
            name
            pageComponents {
                id
                pageid
                name
                componentData {
                    key
                    value
                }
            }
        }
    }
`;

export const loadAllPages = async () =>  {
    if(get(pages).length != 0){
        return;
    }    
    
    let pagesRequestData = await useGraphql(loadPagesQuery, {}, get(loggedInUser))

    if(pagesRequestData.errors) {
        console.log("error!! trigger error state")
    }

    console.log(pagesRequestData)

    var mappedPages = pagesRequestData.data!.pages.map( (page: Page) => {
        var pageComponents = page.pageComponents.map((pageComponent: PageComponent) => {
            var componentData = pageComponent.componentData.map(
                (componentDataSet: ComponentData) => new ComponentData(componentDataSet.key, componentDataSet.value))
            return new PageComponent (pageComponent.id, pageComponent.name, pageComponent.componentData, pageComponent.pageid)
        })

        return new Page(page.id, page.name, pageComponents)
    })

    pages.set(mappedPages)
}