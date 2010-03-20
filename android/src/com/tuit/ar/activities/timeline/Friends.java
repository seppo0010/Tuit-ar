package com.tuit.ar.activities.timeline;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.Preferences;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.request.Options;

public class Friends extends Timeline {
	protected Options getTimeline() {
		return Options.FRIENDS_TIMELINE;
	}

	protected static final int MENU_REFRESH = 0;   
	protected static final int MENU_REPLIES = 1;
	protected static final int MENU_DIRECT = 2;
	protected static final int MENU_PREFERENCES = 3;

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
		menu.add(0, MENU_REFRESH, 0, R.string.refresh);  
		menu.add(0, MENU_REPLIES, 0, R.string.replies);  
		menu.add(0, MENU_DIRECT, 0, R.string.directMessages);  
		menu.add(0, MENU_PREFERENCES, 0, R.string.preferences);  
		return true;  
	}  

	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case MENU_REFRESH:
	        refresh();  
	        return true;  
	    case MENU_PREFERENCES:
			Intent intent = new Intent(this.getApplicationContext(), Preferences.class);
			this.startActivity(intent);		
	        return true;  
	    }
	    return false;
	}
	public void requestTweets() {
		try {
			Twitter.getInstance().requestUrl(Options.FRIENDS_TIMELINE);
		} catch (Exception e) {
			Toast.makeText(this, R.string.unableToFetchTimeline, Toast.LENGTH_SHORT);
		}
	}
}
