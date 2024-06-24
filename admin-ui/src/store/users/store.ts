import { writable } from 'svelte/store';
import { User } from './User';

export const users = writable<User[]>([]);

