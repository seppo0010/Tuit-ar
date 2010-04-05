package com.tuit.ar.models;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.timeline.TimelineObserver;

abstract public class Timeline implements TwitterAccountRequestsObserver {
	protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	protected String newestTweet = "";
	protected ArrayList<TimelineObserver> observers = new ArrayList<TimelineObserver>();
	static private int MAX_SIZE = 100;

	protected Timeline() {
		super();
		Twitter.getInstance().getDefaultAccount().addRequestObserver(this);
	}

	abstract protected Options getTimeline();

	public void refresh() {
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		if (newestTweet.equals("") == false) nvps.add(new BasicNameValuePair("since_id", newestTweet));
		nvps.add(new BasicNameValuePair("count", "25"));
		try {
			Twitter.getInstance().getDefaultAccount().requestUrl(this.getTimeline(), nvps, TwitterRequest.Method.GET);
		} catch (Exception e) {
			failedToUpdate();
		}
	}

	public void requestHasStarted(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		startedUpdate();
	}

	public void requestHasFinished(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		try {
			JSONArray tweets = new JSONArray(new JSONTokener(request.getResponse()));
			int c = tweets.length();
			if (c > 0) {
				for (int i = c-1; i >= 0; i--) {
					Tweet tweet = new Tweet(tweets.getJSONObject(i));
					if (i == 0)
						newestTweet = tweet.getId();
					this.tweets.add(0, tweet);
				}

				if (this.tweets.size() > MAX_SIZE) {
					this.tweets.subList(MAX_SIZE, this.tweets.size()).clear();
				}
				timelineChanged();
			}
		} catch (Exception e) {
			failedToUpdate();
		}
		finishedUpdate();
	}

	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	public Collection<Tweet> getTweetsNewerThan(Tweet tweet) {
		return tweets.subList(0, tweets.indexOf(tweet));
	}

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
