import type { PageComponent } from "./PageComponent"

export class Page {
    id: String
    name: String
    pageComponents: PageComponent[]

    constructor(id: String, name: String, pageComponents: PageComponent[]) {
        this.id = id
        this.name = name
        this.pageComponents = pageComponents
    }
}