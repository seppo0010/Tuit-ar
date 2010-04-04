package com.tuit.ar.api;

import java.util.ArrayList;


public class Twitter {
	static boolean isSecure = false;
	static String BASE_URL = "boludeo.delapalo.net/api/";
	static private Twitter instance;

	private ArrayList<TwitterObserver> observers = new ArrayList<TwitterObserver>(); 
	private ArrayList<TwitterAccount> accounts = new ArrayList<TwitterAccount>(); 

	public static Twitter getInstance() {
		if (instance == null)
			instance = new Twitter();
		return instance;
	}
	private Twitter() {
		super();
	}

	public void addObserver(TwitterObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(TwitterObserver observer) {
		observers.remove(observer);
	}

	protected void notifyChanges() {
		for (TwitterObserver observer : observers) {
			observer.accountListHasChanged(this);
		}
	}

	public void addAccount(TwitterAccount account) {
		this.accounts.add(account);
		notifyChanges();
	}

	public void removeAccount(TwitterAccount account) {
		this.accounts.remove(account);
		notifyChanges();
	}

	public ArrayList<TwitterAccount> getAccounts() {
		return this.accounts;
	}
	public TwitterAccount getDefaultAccount() {
		if (this.accounts.size() == 0) return null;
		return this.accounts.get(0);
	}

}
