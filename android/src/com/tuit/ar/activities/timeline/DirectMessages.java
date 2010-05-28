package com.tuit.ar.activities.timeline;

import java.util.ArrayList;

import android.os.Bundle;

import com.tuit.ar.R;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;

public class DirectMessages extends Timeline {
	ArrayList<com.tuit.ar.models.DirectMessage> tweets;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.directMessages));

		tweets = getTimeline().getTweets();
		this.setListAdapter(timelineAdapter = new TimelineAdapter<com.tuit.ar.models.DirectMessage>(this, tweets));
	}

	protected com.tuit.ar.models.timeline.DirectMessages getTimeline() {
		return com.tuit.ar.models.timeline.DirectMessages.getInstance(Twitter.getInstance().getDefaultAccount());
	}

	public void timelineHasChanged(com.tuit.ar.models.Timeline timeline) {
		if (tweets.size() == 0)
			tweets.addAll(getTimeline().getTweets());
		else
			tweets.addAll(0, getTimeline().getTweetsNewerThan(tweets.get(0)));
		if (isVisible)
			timelineAdapter.notifyDataSetChanged();
	}
}
