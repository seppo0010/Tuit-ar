package com.tuit.ar.models.timeline;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONTokener;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Timeline;

abstract public class Status extends Timeline {
	protected Status(TwitterAccount account) {
		super(account);
	}

	protected ArrayList<com.tuit.ar.models.Status> tweets = new ArrayList<com.tuit.ar.models.Status>();

	@Override
	public void requestHasFinished(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		try {
			JSONArray tweets = new JSONArray(new JSONTokener(request.getResponse()));
			int c = tweets.length();
			if (c > 0) {
				for (int i = c-1; i >= 0; i--) {
					com.tuit.ar.models.Status tweet = new com.tuit.ar.models.Status(tweets.getJSONObject(i));
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

	public ArrayList<com.tuit.ar.models.Status> getTweets() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<com.tuit.ar.models.Status> getTweetsNewerThan(com.tuit.ar.models.Status status) {
		// TODO Auto-generated method stub
		return null;
	}
}
