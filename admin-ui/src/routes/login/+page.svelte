<script lang="ts">
    import { signIn } from '../../store/auth/util/signIn';

	let name = '';
	let password = '';
</script>

<div class="flex items-center justify-center min-h-screen w-full">
	<div class="bg-white p-8 rounded-lg shadow-lg max-w-sm w-full">
		<label class="block mb-4">
			<span class="text-gray-700">name</span>
			<input 
				class="form-input mt-1 block w-full rounded border-gray-300" 
				type="text" 
				placeholder="Input"
				bind:value={name}
			/>
		</label>
		<label class="block mb-4">
			<span class="text-gray-700">Password</span>
			<input 
				class="form-input mt-1 block w-full rounded border-gray-300" 
				type="password" 
				placeholder="Input"
				bind:value={password}
			/>
		</label>
		<button 
			type="button" 
			class="btn variant-filled-primary w-full py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600"
			on:click={() => signIn(name, password)}
		>
			<span>(icon)</span>
			<span>Login</span>
		</button>
	</div>
</div>
