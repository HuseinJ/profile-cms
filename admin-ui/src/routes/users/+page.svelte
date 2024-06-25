<script lang="ts">
import { onMount } from 'svelte';
import { loadUsers } from '../../store/users/util/loadUsers';
import { users } from '../../store/users/store';
import Icon from '@iconify/svelte';


// Check if the user is logged in on mount
onMount(() => {
    loadUsers()
});



</script>
<div class="w-full bg-current rounded">
    <dl class="list-dl">
        {#if $users.length > 0}
            {#each $users as user}
            <div class="hover:bg-black text-black hover:text-white">
                <span class="w-20 badge bg-primary-500">
                    <Icon class="size-full text-inherit" icon="mdi:user" />
                </span>
                <span class="flex-auto">
                    <dt>{user.name}</dt>
                    <dd>{user.email}</dd>
                </span>
            </div>
            {/each}
        {/if}
    </dl>
</div>

