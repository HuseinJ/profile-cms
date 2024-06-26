<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
    import { loadPage } from '../../../store/pages/util/getPage';
    import { Page } from '../../../store/pages/Page';
    import { ComponentData } from '../../../store/pages/ComponentData';
	import type { PageComponent } from '../../../store/pages/PageComponent';

    let loadedPage: Page | undefined;
    let id: string | undefined;
  
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

    // Function to toggle the edited state of a ComponentData item
    function toggleEdited(cdata: ComponentData) {
        cdata.isEdited = true;
    }

    // Function to save changes for a ComponentData item
    function saveChanges(pageComponent: PageComponent) {
        // Perform save operation (e.g., call an API to update the data)
        console.log("Saving changes for:", pageComponent);
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

    function hasEditedComponentData(pageComponent: any): boolean {
        return pageComponent.componentData.some((cdata: ComponentData) => cdata.isEdited);
    }
</script>
  
{#if loadedPage}
    <div class="card bg-initial rounded">
        <h1>Page with ID: {id}</h1>
    </div>

    {#each loadedPage.pageComponents as pageComponent, pindex }
        <div class="mt-5 card p-4">
            <label class="block mb-2 font-bold">Page Component: {pageComponent.name}</label>
            <label class="block mb-2">Id: {pageComponent.id}</label>
        
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
{:else}
    <p>Loading...</p>
{/if}
