package com.tuit.ar.activities.timeline;

import android.os.Bundle;

import com.tuit.ar.R;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;

public class DirectMessages extends Timeline {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.directMessages));
	}

	protected com.tuit.ar.models.Timeline getTimeline() {
		return com.tuit.ar.models.timeline.DirectMessages.getInstance(Twitter.getInstance().getDefaultAccount());
	}
}
