<script lang="ts">
	import '../app.postcss';
	import { AppBar } from '@skeletonlabs/skeleton';
	import { onMount } from 'svelte';
	import { loggedInUser } from '../store/auth/store';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';

	let showAppBar = true;

	// Check if the user is logged in on mount
	onMount(() => {
		if (!$loggedInUser) {
			goto("/login");
		}
	});

	// Reactive statement to update showAppBar based on the current URL
	$: showAppBar = !($page.url.pathname.includes("/login"));
</script>

{#if showAppBar}
	<AppBar gridColumns="grid-cols-3" slotDefault="place-self-center" slotTrail="place-content-end">
		<svelte:fragment slot="lead">(icon)</svelte:fragment>
		(title)
		<svelte:fragment slot="trail">(actions)</svelte:fragment>
	</AppBar>
{/if}

<slot />



