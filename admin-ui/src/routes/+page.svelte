<script lang="ts">
	import { changePassword } from "../store/users/util/changePassword";

    let oldPassword: string;
    let newPassword: string;
    let newPasswortRepeated: string;

    async function triggerChangePassword() {
        console.log(oldPassword, newPassword)
        if(newPassword === newPasswortRepeated){
            await changePassword(oldPassword, newPassword)
            return
        }
        alert("passwords do not match")
        
    }
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
        <button 
			type="button" 
			class="btn variant-filled-primary w-full py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600"
			on:click={triggerChangePassword}
		>
			<span>(icon)</span>
			<span>Login</span>
		</button>
    </div>
</div>