package com.tuit.ar.api;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;


public class Twitter {
	static boolean isSecure = false;
	static String BASE_URL = "api.twitter.com/1/";
	static private Twitter instance;

	private ArrayList<TwitterObserver> observers = new ArrayList<TwitterObserver>(); 
	private ArrayList<TwitterAccount> accounts; 

	private static String CONSUMER_KEY = "wLR28XpgOKbmpaFFvtMVA";
	private static String CONSUMER_SECRET = "WOMQjlVaeuTs0eVQfTqXRxfLD58gbGCQrZdDIrQUSU";
	public static String CALLBACK_URL = "tuitar://login";
	private OAuthProvider provider;
	private CommonsHttpOAuthConsumer consumer;

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

	private void createConsumerAndProvider() {
		if (consumer == null) consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);  
		if (provider == null) {
			provider = new CommonsHttpOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");

			// We are not sure if the provider is the same as before...
			provider.setOAuth10a(true);
		}
	}

	public void addAccount(TwitterAccount account) {
		this.accounts.add(account);
		account.serialize();
		notifyChanges();
	}

	public void removeAccount(TwitterAccount account) throws Exception {
		if (account.delete()) {
			this.accounts.remove(account);
			notifyChanges();
		} else {
			//FIXME: Subclassing exception seems to be Java standard
			throw new Exception("Unable to delete account");
		}
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

	public OAuthConsumer getConsumer() {
		createConsumerAndProvider();
		return consumer;
	}

	public OAuthProvider getProvider() {
		createConsumerAndProvider();
		return provider;
	}
}
