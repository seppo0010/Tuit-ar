package com.tuit.ar.models.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONTokener;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.DirectMessage;
import com.tuit.ar.models.Timeline;

public class DirectMessages extends Timeline {
	static private HashMap<TwitterAccount, DirectMessages> instances = new HashMap<TwitterAccount, DirectMessages>();
	private ArrayList<DirectMessage> tweets; 

	protected DirectMessages(TwitterAccount account) {
		super(account);
		tweets = com.tuit.ar.models.DirectMessage.select("belongs_to_user = ?", new String[] { String.valueOf(account.getUser().getId()) }, null, null, "id DESC", null);
		if (tweets.size() > 0) {
			newestTweet = tweets.get(0).getId();
		}
	}

	@Override
	protected Options getTimeline() {
		return Options.DIRECT_MESSAGES;
	}

	public static com.tuit.ar.models.timeline.DirectMessages getInstance(TwitterAccount account) {
		if (instances.containsKey(account) == false) instances.put(account, new DirectMessages(account));
		return instances.get(account);
	}

	public void requestHasFinished(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		try {
			JSONArray tweets = new JSONArray(new JSONTokener(request.getResponse()));
			int c = tweets.length();
			if (c > 0) {
				for (int i = c-1; i >= 0; i--) {
					DirectMessage tweet = new DirectMessage(tweets.getJSONObject(i));
					tweet.setBelongsToUser(account.getUser().getId());
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
			failedToUpdate(e.getLocalizedMessage());
		}
		finishedUpdate();
	}

	public ArrayList<DirectMessage> getTweets() {
		return tweets;
	}

	public Collection<DirectMessage> getTweetsNewerThan(DirectMessage directMessage) {
		return tweets.subList(0, tweets.indexOf(directMessage));
	}

	public void deleteTweet(DirectMessage directMessage) {
		tweets.remove(directMessage);
		timelineChanged();
	}
}
