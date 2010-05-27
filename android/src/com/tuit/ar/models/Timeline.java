package com.tuit.ar.models;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.api.request.UniqueRequestException;
import com.tuit.ar.models.timeline.TimelineObserver;

abstract public class Timeline implements TwitterAccountRequestsObserver {
	protected ArrayList<ListElement> tweets = new ArrayList<ListElement>();
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

	public void requestHasFinished(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		try {
			JSONArray tweets = new JSONArray(new JSONTokener(request.getResponse()));
			int c = tweets.length();
			if (c > 0) {
				for (int i = c-1; i >= 0; i--) {
					Status tweet = new Status(tweets.getJSONObject(i));
					//is_home INTEGER, is_reply INTEGER, belongs_to_user
					tweet.setBelongsToUser(account.getUser().getId());
					// FIXME: some kind of way to recognize this with more abstraction?
					tweet.setHome(request.getUrl().equals(Options.FRIENDS_TIMELINE));
					tweet.setReply(request.getUrl().equals(Options.REPLIES_TIMELINE));
					tweet.replace();
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

	public ArrayList<ListElement> getTweets() {
		return tweets;
	}

	public Collection<ListElement> getTweetsNewerThan(ListElement listElement) {
		return tweets.subList(0, tweets.indexOf(listElement));
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
