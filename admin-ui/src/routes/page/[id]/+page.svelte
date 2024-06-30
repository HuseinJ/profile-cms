<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
    import { loadPage } from '../../../store/pages/util/getPage';
    import { Page } from '../../../store/pages/Page';
    import { ComponentData } from '../../../store/pages/ComponentData';
	import type { PageComponent } from '../../../store/pages/PageComponent';
	import { setPageComponentData } from '../../../store/pages/util/setPageComponentData';
	import { removePageComponent } from '../../../store/pages/util/removePageComponent';
	import { createPageComponent } from '../../../store/pages/util/createPageComponent';
	import { PageComponentName } from '../../../store/pages/PageComponentName';

    let loadedPage: Page | undefined;
    let id: string | undefined;
    let selectedComponentType: PageComponentName = PageComponentName.PARAGRAPH;
  
    onMount(() => {
        const fetchPageData = async () => {
            const params = get(page).params;
            id = params.id;
            loadedPage = await loadPage(id);
        };
  
        fetchPageData().catch(error => {
            console.error("Failed to load the page:", error);
        });
    });

    function getEnumValues() {
        return Object.values(PageComponentName).map(value => ({
            value,
            label: value.charAt(0) + value.slice(1).toLowerCase()
        }));
    }

    function toggleEdited(cdata: ComponentData) {
        cdata.isEdited = true;
    }

    function saveChanges(pageComponent: PageComponent) {
        if (setPageComponentData(pageComponent)) {
            pageComponent.componentData.forEach(cdata => cdata.isEdited = false)
            loadedPage = {...loadedPage};
        }
    }

    function deleteKeyValuePair(pageComponentIndex: number, index: number) {
        if (loadedPage && loadedPage.pageComponents.length > pageComponentIndex) {
            const pageComponent = loadedPage.pageComponents[pageComponentIndex];
            if (pageComponent.componentData.length > index) {
                // Splice out the item from componentData array
                const deletedItem = pageComponent.componentData.splice(index, 1);
                console.log("Deleted item:", deletedItem);

                // Update loadedPage to reflect the change
                loadedPage.pageComponents[pageComponentIndex].componentData = [...pageComponent.componentData];
            }

            saveChanges(pageComponent)
        }
    }

    function addKeyValuePair(pageComponentIndex: number) {
        if (loadedPage && loadedPage.pageComponents.length > pageComponentIndex){
            loadedPage.pageComponents[pageComponentIndex].componentData = [
                ...loadedPage.pageComponents[pageComponentIndex].componentData,
                new ComponentData("", "", true)
            ];
        }
    }

    async function deletePageComponent(pageComponent: PageComponent) {
        if (loadedPage && await removePageComponent(pageComponent)) {
            loadedPage.pageComponents = loadedPage.pageComponents.filter(pc => pc.id !== pageComponent.id);
            loadedPage = { ...loadedPage };
        }
    }

    function hasEditedComponentData(pageComponent: any): boolean {
        return pageComponent.componentData.some((cdata: ComponentData) => cdata.isEdited);
    }

    async function addPageComponent(selectedType: PageComponentName) {
        if (loadedPage) {
            const component = await createPageComponent(selectedComponentType, id!)
            loadedPage.pageComponents.push(component)
            loadedPage = { ...loadedPage };
        }
    }

</script>
  
{#if loadedPage}
    <div class="card bg-initial rounded">
        <h1>Page with ID: {id}</h1>
    </div>

    {#each loadedPage.pageComponents as pageComponent, pindex }
    <div class="mt-5 card p-4">
        <div class="flex justify-between items-center">
            <div>
                <label class="block mb-2 font-bold">Page Component: {pageComponent.name}</label>
                <label class="block mb-2">Id: {pageComponent.id}</label>
            </div>
            <button class="btn-delete text-red-500 bg-transparent border border-solid border-red-500 rounded-md px-3 py-1 hover:bg-red-500 hover:text-white" on:click={() => deletePageComponent(pageComponent)}>Delete Component</button>
        </div>

        {#each pageComponent.componentData as cdata, index}
            <div class="mb-4 flex items-center">
                <div class="flex-1">
                    <div class="flex mb-2">
                        <div class="w-1/3">
                            <label class="block text-sm font-bold mb-1">Key</label>
                            <input class="input w-full px-4 py-2 border rounded-lg" type="text" placeholder="Enter key" bind:value={cdata.key} on:input={() => toggleEdited(cdata)} />
                        </div>
                        <div class="w-2/3 ml-4">
                            <label class="block text-sm font-bold mb-1">Value</label>
                            <input class="input w-full px-4 py-2 border rounded-lg" type="text" placeholder="Enter value" bind:value={cdata.value} on:input={() => toggleEdited(cdata)} />
                        </div>
                    </div>
                </div>
                <div class="ml-4">
                    <button class="btn-delete text-red-500 bg-transparent border border-solid border-red-500 rounded-md px-3 py-1 hover:bg-red-500 hover:text-white mr-4 mt-3" on:click={() => deleteKeyValuePair(pindex, index)}>Delete</button>
                </div>
            </div>
        {/each}

        <div class="mt-4">
            <button class="btn-add bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600" on:click={() => addKeyValuePair(pindex)}>Add Key-Value Pair</button>
            {#if hasEditedComponentData(pageComponent)}
                <button class="btn-save bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600" on:click={() => saveChanges(pageComponent)}>Save</button>
            {/if}
        </div>
    </div>
{/each}

<div class="card bg-initial rounded mt-4 p-4">
    <h1>Add Component:</h1>
    <label class="label">
        <span>Select</span>
        <select class="select" bind:value={selectedComponentType}>
            {#each getEnumValues() as { value, label }}
                <option value={value}>{label}</option>
            {/each}
        </select>
    </label>
    <button class="btn-save bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600" on:click={() => addPageComponent(selectedComponentType)}>Add</button>
</div>

{:else}
    <p>Loading...</p>
{/if}
