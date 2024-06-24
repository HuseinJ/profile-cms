<script lang="ts">
import { onMount } from 'svelte';
import { loadUsers } from '../../store/users/util/loadUsers';

// Check if the user is logged in on mount
onMount(() => {
    loadUsers()
});

</script>
