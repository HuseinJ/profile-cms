import type { ComponentData } from "./ComponentData";
import type { PageComponentName } from "./PageComponentName";

export class PageComponent {
    id: String
    name: PageComponentName
    componentData: ComponentData[]
    pageid: String


    constructor(id: String, name: PageComponentName, componentData: ComponentData[], pageid: String){
        this.id = id;
        this.name = name
        this.componentData = componentData
        this.pageid = pageid
    }
}