package com.tuit.ar.activities.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tuit.ar.R;
import com.tuit.ar.activities.Preferences;
import com.tuit.ar.activities.Timeline;

public class Friends extends Timeline {

	protected static final int MENU_REFRESH = 0;   
	protected static final int MENU_REPLIES = 1;
	protected static final int MENU_DIRECT = 2;
	protected static final int MENU_PREFERENCES = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.friends));
	}

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
	    {
	        refresh();  
	        return true;
	    }
	    case MENU_REPLIES:
	    {
	    	Intent intent = new Intent(this.getApplicationContext(), Replies.class);
	    	this.startActivity(intent);		
	    	return true;
	    }
	    case MENU_PREFERENCES:
	    {
			Intent intent = new Intent(this.getApplicationContext(), Preferences.class);
			this.startActivity(intent);		
	        return true;
	    }
	    }
	    return false;
	}

	protected com.tuit.ar.models.Timeline getTimeline() {
		return com.tuit.ar.models.timeline.Friends.getInstance();
	}
}
