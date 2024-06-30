
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

const loadPageQuery = `
	query($uuid: String){
        page(uuid: $uuid) {
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

export const loadAllPages = async (force = false) =>  {
    if(!force && get(pages).length != 0){
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
                (componentDataSet: ComponentData) => new ComponentData(componentDataSet.key, componentDataSet.value, false))
            return new PageComponent (pageComponent.id, pageComponent.name, componentData, pageComponent.pageid)
        })

        return new Page(page.id, page.name, pageComponents)
    })

    pages.set(mappedPages)
}

export const loadPage = async (uuid: string): Promise<Page> =>  {
    
    let pageRequestData = await useGraphql(loadPageQuery, {'uuid': uuid}, get(loggedInUser))

    if(pageRequestData.errors) {
        console.log("error!! trigger error state")
    }

    var mappedPageComponent = pageRequestData.data.page.pageComponents.map((pageComponent: PageComponent) => {
        var componentData = pageComponent.componentData.map(
            (componentDataSet: ComponentData) => new ComponentData(componentDataSet.key, componentDataSet.value, false))
        return new PageComponent (pageComponent.id, pageComponent.name, componentData, pageComponent.pageid)
    })

    var mappedPage = new Page(
        pageRequestData.data.page.id,
        pageRequestData.data.page.name,
        mappedPageComponent
    )

    return mappedPage

}