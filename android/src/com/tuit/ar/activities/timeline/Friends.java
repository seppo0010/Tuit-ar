package com.tuit.ar.activities.timeline;

import android.os.Bundle;
import android.view.Menu;

import com.tuit.ar.R;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;

public class Friends extends Timeline {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.friends));
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_NEW_TWEET, 0, R.string.newTweet);
		menu.add(0, MENU_REFRESH, 0, R.string.refresh);  
		menu.add(0, MENU_REPLIES, 0, R.string.replies);  
//		menu.add(0, MENU_DIRECT, 0, R.string.directMessages);  
		menu.add(0, MENU_PREFERENCES, 0, R.string.preferences);  
		return true;  
	}  

	protected com.tuit.ar.models.Timeline getTimeline() {
		return com.tuit.ar.models.timeline.Friends.getInstance(Twitter.getInstance().getDefaultAccount());
	}
}
