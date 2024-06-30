<script lang="ts">
    import { onMount } from 'svelte';
	import { loadAllPages } from '../../store/pages/util/getPage';
    import { pages } from '../../store/pages/store';
    import Icon from '@iconify/svelte';
	import { goto } from '$app/navigation';
	import { createNewPage } from '../../store/pages/util/createNewPage';
	import { get } from 'svelte/store';
    import { deletePage as deletePageApi } from '../../store/pages/util/deletePage';
    import { assignHomePage as assignHomePageApi } from '../../store/pages/util/assignHomePage';

    let name = '';

    onMount(() => {
        loadAllPages()
    });

    async function createPage(name: string) {
        await createNewPage(name); // Assuming savePage function saves the page
        loadAllPages(true);
    }

    async function deletePage(id: String) {
       await deletePageApi(id)
       loadAllPages(true);
    }

    async function assignHomePage(id: String) {
       await assignHomePageApi(id)
       loadAllPages(true);
    }
</script>

<div class="card w-full bg-initial rounded mt-4 p-4">
    <dl class="list-dl">
        {#if $pages.length > 0}
            {#each $pages as page (page.id)}
            <div class="flex items-center hover:bg-black hover:text-white">
                <span class="w-20 badge bg-primary-500">
                    <Icon class="size-full text-inherit" icon="emojione-v1:page" />
                </span>
                <span class="flex-auto"  on:click={() => goto("/page/"+page.id)}>
                    <dt>{page.name}</dt>
                    <dd>{page.id}</dd>
                </span>
                <button class="btn-add bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600" on:click={(event) => assignHomePage(page.id)}>
                    Make Home Page
                </button>
                <button class="btn-delete text-red-500 bg-transparent border border-solid border-red-500 rounded-md px-3 py-1 hover:bg-red-500 hover:text-white mr-4" on:click={(event) => deletePage(page.id)}>
                    Delete
                </button>
            </div>
            {/each}
        {/if}
    </dl>
</div>

<div class="card bg-initial rounded mt-4 p-4">
    <h1>Add Page:</h1>
    <div class="flex items-center">
        <label class="label flex-auto">
            <span>Name:</span>
            <input class="input w-full" type="text" placeholder="Enter page name" bind:value={name} />
        </label>
        <button class="btn-save bg-blue-500 text-white px-4 py-2 rounded-md ml-4 mt-7 hover:bg-blue-600" on:click={() => createPage(name)}>Add</button>
    </div>
</div>