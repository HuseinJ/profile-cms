<script lang="ts">
    let oldPassword: string;
    let newPassword: string;
    let newPasswortRepeated: string;
</script>

<div>
    <h1>home</h1>
    <div class="mt-5 card p-4">
        <div class="w-1/3">
            <label class="block text-sm font-bold mb-1">Old Password</label>
            <input class="input w-full px-4 py-2 border rounded-lg" type="text" placeholder="Enter key" bind:value={oldPassword} on:input={() => {}} />
        </div>
        <div class="w-1/3">
            <label class="block text-sm font-bold mb-1">New Password</label>
            <input class="input w-full px-4 py-2 border rounded-lg" type="text" placeholder="Enter key" bind:value={newPassword} on:input={() => {}} />
        </div>
        <div class="w-1/3">
            <label class="block text-sm font-bold mb-1">New Password Repeated</label>
            <input class="input w-full px-4 py-2 border rounded-lg" type="text" placeholder="Enter key" bind:value={newPasswortRepeated} on:input={() => {}} />
        </div>
    </div>
</div>