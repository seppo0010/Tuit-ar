package com.tuit.ar.models;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.api.request.UniqueRequestException;
import com.tuit.ar.models.timeline.TimelineObserver;

abstract public class Timeline implements TwitterAccountRequestsObserver {
	protected long newestTweet = 0;
	protected ArrayList<TimelineObserver> observers = new ArrayList<TimelineObserver>();
	protected static int MAX_SIZE = 100;
	protected final TwitterAccount account;

	protected Timeline(TwitterAccount account) {
		this.account = account;
		account.addRequestObserver(this);
	}

	abstract protected Options getTimeline();

	public void refresh() {
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		if (newestTweet > 0) nvps.add(new BasicNameValuePair("since_id", String.valueOf(newestTweet)));
		nvps.add(new BasicNameValuePair("count", "25"));
		try {
			account.requestUrl(this.getTimeline(), nvps, TwitterRequest.Method.GET);
		} catch (UniqueRequestException e) {
			// I wanna ignore this exceptions
		} catch (Exception e) {
			failedToUpdate();
		}
	}

	public void requestHasStarted(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		startedUpdate();
	}

	abstract public void requestHasFinished(TwitterRequest request);

	protected void startedUpdate() {
		for (TimelineObserver observer : observers) {
			observer.timelineRequestStarted(this);
		}
    }

	protected void failedToUpdate() {
		for (TimelineObserver observer : observers) {
			observer.timelineUpdateHasFailed(this);
		}
    }

    protected void finishedUpdate() {
		for (TimelineObserver observer : observers) {
			observer.timelineRequestFinished(this);
		}
    }

    protected void timelineChanged() {
		for (TimelineObserver observer : observers) {
			observer.timelineHasChanged(this);
		}
    }

    public void addObserver(TimelineObserver observer) {
    	this.observers.add(observer);
    }

    public void removeObserver(TimelineObserver observer) {
    	this.observers.remove(observer);
    }
}
