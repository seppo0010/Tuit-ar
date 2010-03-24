package com.tuit.ar.api;

import java.util.ArrayList;
import java.util.HashMap;

import com.tuit.ar.api.request.Options;

public class Twitter {
	static boolean isSecure = false;
	static String BASE_URL = "boludeo.delapalo.net/api/";
	static private Twitter instance;

	private ArrayList<TwitterObserver> observers = new ArrayList<TwitterObserver>(); 
	private String username;
	private String password;

	public static Twitter getInstance() {
		if (instance == null)
			instance = new Twitter();
		return instance;
	}
	private Twitter() {
		super();
	}

	public void requestUrl(Options login) throws Exception { requestUrl(login, null, TwitterRequest.Method.GET); }

	public void requestUrl(Options login, HashMap<String, String> params, TwitterRequest.Method get) throws Exception {
		startedRequest(new TwitterRequest(login, params, get));
		
	}
	String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	String getPassword() { return password; }
	public void setPassword(String username) { this.password = username; }
	public void clearCredentials() {
		this.setUsername(null);
		this.setPassword(null);
	}

	public void addObserver(TwitterObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(TwitterObserver observer) {
		observers.remove(observer);
	}

	public void startedRequest(TwitterRequest twitterRequest) {
		for (TwitterObserver observer : observers) {
			observer.requestHasStarted(twitterRequest);
		}
	}
	public void finishedRequest(TwitterRequest twitterRequest) {
		for (TwitterObserver observer : observers) {
			observer.requestHasFinished(twitterRequest);
		}
	}
}
