package com.tuit.ar.models.timeline;

import java.util.HashMap;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Status;

public class Favorites extends com.tuit.ar.models.timeline.Status {
	static private HashMap<TwitterAccount, Favorites> instances = new HashMap<TwitterAccount, Favorites>(); 

	protected Favorites(TwitterAccount account) {
		super(account);
		tweets = Status.select("favorited = 1 AND belongs_to_user = ?", new String[] { String.valueOf(account.getUser().getId()) }, null, null, "id DESC", null);
		if (tweets.size() > 0) {
			newestTweet = tweets.get(0).getId();
		}
	}

	@Override
	protected Options getTimeline() {
		return Options.FAVORITES_TIMELINE;
	}

	public static com.tuit.ar.models.timeline.Status getInstance(TwitterAccount account) {
		if (instances.containsKey(account) == false) instances.put(account, new Favorites(account));
		return instances.get(account);
	}
}
