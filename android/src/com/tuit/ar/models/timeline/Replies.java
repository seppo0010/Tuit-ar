package com.tuit.ar.models.timeline;

import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Timeline;

public class Replies extends Timeline {

	static private Replies instance;

	@Override
	protected Options getTimeline() {
		return Options.REPLIES_TIMELINE;
	}

	public static Timeline getInstance() {
		if (instance == null) instance = new Replies();
		return instance;
	}
}
