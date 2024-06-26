<script lang="ts">
	import '../app.postcss';
	import { AppRail, AppRailAnchor } from '@skeletonlabs/skeleton';
	import { onMount } from 'svelte';
	import { loggedInUser, logoutUser } from '../store/auth/store';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Icon from '@iconify/svelte';

	let showAppBar = true;

	// Check if the user is logged in on mount
	onMount(() => {
		if (!$loggedInUser) {
			goto("/login");
		}
	});

	// Reactive statement to update showAppBar based on the current URL
	$: showAppBar = !($page.url.pathname.includes("/login"));

	let currentTile: number = 0;
</script>

<div class="flex h-dvh">
	{#if showAppBar}
	<AppRail class="fixed flex-shrink-0" height="h-dvh">
		<svelte:fragment slot="lead">
			<AppRailAnchor href="/" selected={$page.url.pathname === '/'}>
				<Icon class="p-4 size-full text-inherit" icon="material-symbols-light:home" />
			</AppRailAnchor>
		</svelte:fragment>
		<!-- --- -->
		<AppRailAnchor href="/users" selected={$page.url.pathname === '/users'}>
			<Icon class="p-4 size-full text-inherit" icon="material-symbols-light:user-attributes-rounded" />
		</AppRailAnchor>
		<AppRailAnchor href="/pages" selected={$page.url.pathname === '/pages'}>
			<Icon class="p-4 size-full text-inherit" icon="icon-park-outline:page" />
		</AppRailAnchor>
		<!-- --- -->
		<svelte:fragment slot="trail">
			<AppRailAnchor href="https://github.com/HuseinJ/profile-cms" target="_blank" title="Git">
				<Icon class="p-4 size-full" icon="logos:github-icon" />
			</AppRailAnchor>
			<div on:click={logoutUser}>
				<AppRailAnchor>
					<Icon class="p-4 size-full" icon="material-symbols:logout" />
				  </AppRailAnchor>
			</div>
		</svelte:fragment>
	</AppRail>
	{/if}
	
	<div class="flex-1 p-8 ml-20">
		<slot />
	</div>
	
</div>





