export class ComponentData {
    key: String
    value: String
    isEdited: Boolean

    constructor(key: String, value: String, isEdited: Boolean){
        this.key = key
        this.value = value
        this.isEdited = isEdited
    }
}