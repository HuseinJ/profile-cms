<script lang="ts">
import { onMount } from 'svelte';
import { loadUsers } from '../../store/users/util/loadUsers';
import { users } from '../../store/users/store';

// Check if the user is logged in on mount
onMount(() => {
    loadUsers()
    console.log("users", $users)
});



</script>
<div class="w-full">
    <dl class="list-dl">
        {#if $users.length > 0}
            {#each $users as user}
            <div>
                <span class="badge bg-primary-500">💀</span>
                <span class="flex-auto">
                    <dt>{user.name}</dt>
                    <dd>{user.email}</dd>
                </span>
            </div>
            {/each}
        {/if}
    </dl>
</div>

