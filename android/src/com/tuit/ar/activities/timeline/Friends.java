package com.tuit.ar.activities.timeline;

import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.request.Options;

public class Friends extends Timeline {
	protected Options getTimeline() {
		return Options.FRIENDS_TIMELINE;
	}

	public void requestTweets() {
		try {
			Twitter.getInstance().requestUrl(Options.FRIENDS_TIMELINE);
		} catch (Exception e) {
			Toast.makeText(this, R.string.unableToFetchTimeline, Toast.LENGTH_SHORT);
		}
	}
}
