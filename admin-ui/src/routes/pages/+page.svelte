<script lang="ts">
    import { onMount } from 'svelte';
	import { loadAllPages } from '../../store/pages/util/getPage';
    import { pages } from '../../store/pages/store';
    import Icon from '@iconify/svelte';
	import { get } from 'svelte/store';

    onMount(() => {
        loadAllPages()
        console.log("here", get(pages))
    });
    </script>

<div class="w-full bg-current rounded">
    <dl class="list-dl">
        {#if $pages.length > 0}
            {#each $pages as page}
            <div class="hover:bg-black text-black hover:text-white">
                <span class="w-20 badge bg-primary-500">
                    <Icon class="size-full text-inherit" icon="emojione-v1:page" />
                </span>
                <span class="flex-auto">
                    <dt>{page.name}</dt>
                    <dd>{page.id}</dd>
                </span>
            </div>
            {/each}
        {/if}
    </dl>
</div>