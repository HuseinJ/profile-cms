<script lang="ts">
    import { onMount } from 'svelte';
	import { loadAllPages } from '../../store/pages/util/getPage';
    import { pages } from '../../store/pages/store';
    import Icon from '@iconify/svelte';
	import { get } from 'svelte/store';
	import { goto } from '$app/navigation';

    onMount(() => {
        loadAllPages()
    });
</script>

<div class="card w-full bg-initial rounded">
    <dl class="list-dl">
        {#if $pages.length > 0}
            {#each $pages as page}
            <div class="hover:bg-black hover:text-white" on:click={() => goto("/page/"+page.id)}>
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