package com.tuit.ar.api;

import java.util.ArrayList;


public class Twitter {
	static boolean isSecure = false;
	static String BASE_URL = "api.twitter.com/1/";
	static private Twitter instance;

	private ArrayList<TwitterObserver> observers = new ArrayList<TwitterObserver>(); 
	private ArrayList<TwitterAccount> accounts; 

	public static Twitter getInstance() {
		if (instance == null)
			instance = new Twitter();
		return instance;
	}

	private Twitter() {
		super();
		accounts = TwitterAccount.fetchAllFromCache();
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
		account.serialize();
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

	public void setDefaultAccount(TwitterAccount account) {
		TwitterAccount previousDefault = this.getDefaultAccount();
		if (previousDefault == account) return;
		this.accounts.remove(account);
		this.accounts.set(0, account);
		this.accounts.add(previousDefault);
	}
}
