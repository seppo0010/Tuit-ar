package com.tuit.ar.api;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.tuit.ar.api.request.Options;

public class TwitterAccount implements TwitterAccountObserver {
	private oauth.signpost.AbstractOAuthConsumer consumer;
	private String username;

	private ArrayList<TwitterAccountObserver> observers = new ArrayList<TwitterAccountObserver>(); 

	public TwitterAccount(oauth.signpost.AbstractOAuthConsumer _consumer) throws Exception {
		consumer = _consumer;
		this.addObserver(this);
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

	public void startedRequest(TwitterRequest twitterRequest) {
		for (TwitterAccountObserver observer : observers) {
			observer.requestHasStarted(twitterRequest);
		}
	}
	public void finishedRequest(TwitterRequest twitterRequest) {
		for (TwitterAccountObserver observer : observers) {
			observer.requestHasFinished(twitterRequest);
		}
	}

	@Override
	public void requestHasFinished(TwitterRequest request) {
		if (request.getUrl() == Options.LOGIN) {
			String response = request.getResponse();
			try {
				JSONObject data = new JSONObject(new JSONTokener(response));
				this.setUsername(data.getString("username"));
			} catch (JSONException e) {
			}
		}
	}

	@Override
	public void requestHasStarted(TwitterRequest request) {
	}

	public oauth.signpost.AbstractOAuthConsumer getConsumer() {
		return consumer;
	}
}
