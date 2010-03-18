package com.tuit.ar.activities.timeline;

import android.view.Menu;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.request.Options;

public class Friends extends Timeline {
	protected Options getTimeline() {
		return Options.FRIENDS_TIMELINE;
	}

	protected static final int MENU_REFRESH = 0;   
	protected static final int MENU_REPLIES = 1;  

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
		menu.add(0, MENU_REFRESH, 0, R.string.refresh);  
		menu.add(0, MENU_REPLIES, 0, R.string.replies);  
		return true;  
	}  

	public void requestTweets() {
		try {
			Twitter.getInstance().requestUrl(Options.FRIENDS_TIMELINE);
		} catch (Exception e) {
			Toast.makeText(this, R.string.unableToFetchTimeline, Toast.LENGTH_SHORT);
		}
	}
}
