import adapter from '@sveltejs/adapter-node';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  extensions: ['.svelte'],
  preprocess: [vitePreprocess()],
  
  vitePlugin: {
    inspector: true,
  },
  kit: {
    adapter: adapter({
      // default options are shown. You don't need to specify these
      out: 'build',
      precompress: false,
      envPrefix: {
        host: 'HOST',
        port: 'PORT'
      }
    })
  }
};

export default config;