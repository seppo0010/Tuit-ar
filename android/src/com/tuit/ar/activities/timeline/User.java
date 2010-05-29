package com.tuit.ar.activities.timeline;

import android.os.Bundle;
import android.view.MenuItem;

import com.tuit.ar.api.Twitter;

public class User extends Status {
	static private com.tuit.ar.models.User user;

	static public void setUser(com.tuit.ar.models.User _user) {
		user = _user;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(user.getScreenName());
	}

	@Override
	protected com.tuit.ar.models.timeline.Status getTimeline() {
		return new com.tuit.ar.models.timeline.User(Twitter.getInstance().getDefaultAccount(), user);
	}

	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case MENU_REFRESH:
	    {
	        refresh();
	        return true;
	    }
	    }
	    return false;
	}
}
