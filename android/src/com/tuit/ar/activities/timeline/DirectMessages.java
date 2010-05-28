package com.tuit.ar.activities.timeline;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;

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

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
		menu.add(0, MENU_NEW_TWEET, 0, R.string.newTweet);  
		menu.add(0, MENU_REFRESH, 0, R.string.refresh);  
		menu.add(0, MENU_FRIENDS, 0, R.string.friends);  
		menu.add(0, MENU_REPLIES, 0, R.string.replies);  
		menu.add(0, MENU_PREFERENCES, 0, R.string.preferences);  
		return true;  
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
