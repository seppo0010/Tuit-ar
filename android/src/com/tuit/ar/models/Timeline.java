package com.tuit.ar.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.timeline.TimelineObserver;

abstract public class Timeline implements TwitterObserver {
	protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	protected String newestTweet = "";
	protected ArrayList<TimelineObserver> observers = new ArrayList<TimelineObserver>(); 

	protected Timeline() {
		super();
		Twitter.getInstance().addObserver(this);
	}

	abstract protected Options getTimeline();

	public void refresh() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("since_id", newestTweet);
		params.put("count", "5");
		try {
			Twitter.getInstance().requestUrl(this.getTimeline(), params, TwitterRequest.Method.GET);
		} catch (Exception e) {
			failedToUpdate();
		}
	}

	public void requestHasFinished(TwitterRequest request) {
		if (request.getUrl() != this.getTimeline()) return;
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
				finishedUpdate();
			}
		} catch (JSONException e) {
			failedToUpdate();
		}
	}

	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	public Collection<Tweet> getTweetsNewerThan(Tweet tweet) {
		return tweets.subList(0, tweets.indexOf(tweet));
	}

	protected void failedToUpdate() {
		for (TimelineObserver observer : observers) {
			observer.timelineUpdateHasFailed(this);
		}
    }

    protected void finishedUpdate() {
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
