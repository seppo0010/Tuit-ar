package com.tuit.ar.activities.timeline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.models.Settings;

public class Friends extends com.tuit.ar.activities.timeline.Status {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.friends));
		Settings settings = Settings.getInstance();
		if (!settings.getSharedPreferences(this).contains(Settings.LAZY_MODE)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.firstRun);
			builder.setMessage(R.string.firstRunMessage);
			builder.setPositiveButton(R.string.yes, new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					getTimeline().refresh();
					Settings settings = Settings.getInstance();
					Editor editor = settings.getSharedPreferences(Friends.this).edit();
					editor.putBoolean(Settings.LAZY_MODE, false);
					editor.commit();
				}
			});
			builder.setNegativeButton(R.string.no, new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Settings settings = Settings.getInstance();
					Editor editor = settings.getSharedPreferences(Friends.this).edit();
					editor.putBoolean(Settings.LAZY_MODE, true);
					editor.commit();
				}
			});
			builder.show();
		}
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_NEW_TWEET, 0, R.string.newTweet);
		menu.add(0, MENU_REFRESH, 0, R.string.refresh);  
		menu.add(0, MENU_REPLIES, 0, R.string.replies);  
		menu.add(0, MENU_DIRECT, 0, R.string.directMessages);  
		menu.add(0, MENU_FAVORITES, 0, R.string.favorites);  
		menu.add(0, MENU_PREFERENCES, 0, R.string.preferences);
		menu.add(0, MENU_MY_PROFILE, 0, R.string.profile);
		return true;  
	}  

	protected com.tuit.ar.models.timeline.Status getTimeline() {
		return com.tuit.ar.models.timeline.Friends.getInstance(Twitter.getInstance().getDefaultAccount());
	}
}
