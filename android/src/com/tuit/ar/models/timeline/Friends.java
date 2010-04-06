package com.tuit.ar.models.timeline;

import java.util.HashMap;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Timeline;

public class Friends extends Timeline {
	static private HashMap<TwitterAccount, Friends> instances = new HashMap<TwitterAccount, Friends>(); 

	protected Friends(TwitterAccount account) {
		super(account);
	}

	@Override
	protected Options getTimeline() {
		return Options.FRIENDS_TIMELINE;
	}

	public static Timeline getInstance(TwitterAccount account) {
		if (instances.containsKey(account) == false) instances.put(account, new Friends(account));
		return instances.get(account);
	}

}
