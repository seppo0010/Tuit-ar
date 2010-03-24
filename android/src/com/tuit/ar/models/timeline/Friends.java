package com.tuit.ar.models.timeline;

import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Timeline;

public class Friends extends Timeline {
	static private Friends instance;

	@Override
	protected Options getTimeline() {
		return Options.FRIENDS_TIMELINE;
	}

	public static Timeline getInstance() {
		if (instance == null) instance = new Friends();
		return instance;
	}

}
