import { writable } from 'svelte/store';
import { LoggedInUser } from './LoggedInUser';
import { goto } from '$app/navigation';

const LOGGED_IN_USER = "loggedInUser"

export const loggedInUser = writable<LoggedInUser|undefined>(undefined);

// Controlled setter for the loggedInUser store
export const setLoggedInUser = (user: LoggedInUser) => {
    localStorage.setItem(LOGGED_IN_USER, JSON.stringify(user));
    loggedInUser.set(user);
};

export const logoutUser = () => {
    localStorage.removeItem(LOGGED_IN_USER)
    loggedInUser.set(undefined)
    goto("/login")
}

// Load the user from localStorage when the store is initialized
if (typeof window !== 'undefined') {
	const storedUser = localStorage.getItem('loggedInUser');
	if (storedUser) {
		loggedInUser.set(JSON.parse(storedUser));
	}
}