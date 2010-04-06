package com.tuit.ar.models.timeline;

import java.util.HashMap;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Timeline;

public class Replies extends Timeline {
	static private HashMap<TwitterAccount, Replies> instances = new HashMap<TwitterAccount, Replies>(); 

	protected Replies(TwitterAccount account) {
		super(account);
	}

	@Override
	protected Options getTimeline() {
		return Options.REPLIES_TIMELINE;
	}

	public static Timeline getInstance(TwitterAccount account) {
		if (instances.containsKey(account) == false) instances.put(account, new Replies(account));
		return instances.get(account);
	}
}
