package com.tuit.ar.api;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.tuit.ar.api.request.Options;

public class TwitterAccount implements TwitterAccountRequestsObserver {
	private oauth.signpost.AbstractOAuthConsumer consumer;
	private String username;

	private ArrayList<TwitterAccountObserver> observers = new ArrayList<TwitterAccountObserver>(); 
	private ArrayList<TwitterAccountRequestsObserver> requestsObservers = new ArrayList<TwitterAccountRequestsObserver>(); 

	public TwitterAccount(oauth.signpost.AbstractOAuthConsumer _consumer) throws Exception {
		consumer = _consumer;
		this.addRequestObserver(this);
		this.requestUrl(Options.LOGIN);
	}

	public void requestUrl(Options login) throws Exception { requestUrl(login, null, TwitterRequest.Method.GET); }

	public void requestUrl(Options login, ArrayList<NameValuePair> params, TwitterRequest.Method get) throws Exception {
		startedRequest(new TwitterRequest(this, login, params, get));
		
	}
	public String getUsername() { return username; }
	private void setUsername(String username) { this.username = username; }

	public void addObserver(TwitterAccountObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(TwitterAccountObserver observer) {
		observers.remove(observer);
	}

	public void accountChanged() {
		for (TwitterAccountObserver observer : observers) {
			observer.accountHasChanged(this);
		}
	}

	public void addRequestObserver(TwitterAccountRequestsObserver observer) {
		requestsObservers.add(observer);
	}

	public void removeRequestObserver(TwitterAccountRequestsObserver observer) {
		requestsObservers.remove(observer);
	}

	public void startedRequest(TwitterRequest twitterRequest) {
		for (TwitterAccountRequestsObserver observer : requestsObservers) {
			observer.requestHasStarted(twitterRequest);
		}
	}

	public void finishedRequest(TwitterRequest twitterRequest) {
		for (TwitterAccountRequestsObserver  observer : requestsObservers) {
			observer.requestHasFinished(twitterRequest);
		}
	}

	public void requestHasFinished(TwitterRequest request) {
		if (request.getUrl() == Options.LOGIN) {
			String response = request.getResponse();
			try {
				JSONObject data = new JSONObject(new JSONTokener(response));
				this.setUsername(data.getString("screen_name"));
				accountChanged();
			} catch (JSONException e) {
			}
		}
	}

	public void requestHasStarted(TwitterRequest request) {
	}

	public oauth.signpost.AbstractOAuthConsumer getConsumer() {
		return consumer;
	}
}
