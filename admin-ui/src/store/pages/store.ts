import { writable } from 'svelte/store';
import { Page } from './Page';

export const pages = writable<Page[]>([]);