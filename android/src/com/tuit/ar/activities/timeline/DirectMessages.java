package com.tuit.ar.activities.timeline;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.NewDirectMessage;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;

public class DirectMessages extends Timeline {
	ArrayList<com.tuit.ar.models.DirectMessage> tweets;

	protected static final int DM_MENU_NEW_DIRECT_MESSAGE = 0;
	protected static final int DM_MENU_DELETE = 1;
	protected static final int DM_MENU_SHOW_PROFILE = 2;
	protected static final int DM_MENU_OPEN_LINKS = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.directMessages));

		tweets = getTimeline().getTweets();
		this.setListAdapter(timelineAdapter = new TimelineAdapter<com.tuit.ar.models.DirectMessage>(this, tweets));
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
		menu.add(0, MENU_NEW_DIRECT_MESSAGE, 0, R.string.newDirectMessage);  
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

	protected void onListItemClick (ListView l, View v, int position, long id) {
		final com.tuit.ar.models.DirectMessage tweet = (com.tuit.ar.models.DirectMessage) tweets.get(position);
		new AlertDialog.Builder(this).
		setTitle(getString(R.string.executeAction)).
		setItems(R.array.dmOptions,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DM_MENU_NEW_DIRECT_MESSAGE:
						{
							Intent intent = new Intent(getApplicationContext(), NewDirectMessage.class);
							intent.putExtra("to_user", tweet.getUsername());
							startActivity(intent);
							break;
						}
						case DM_MENU_DELETE:
						{
							new AlertDialog.Builder(DirectMessages.this)
					        .setMessage(R.string.confirmDeleteDm)
					        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					            	try {
					            		tweet.deleteFromServer();
					            		tweet.delete();
					            	} catch (Exception e) {
					            		Toast.makeText(DirectMessages.this, getString(R.string.unableToDeleteDm), Toast.LENGTH_SHORT).show();
					            	}
					            }
					        })
					        .setNegativeButton(R.string.no, null)
					        .show();
							break;
						}
						case DM_MENU_SHOW_PROFILE:
						{
							showProfile(tweet.getSender());
							break;
						}
						case DM_MENU_OPEN_LINKS:
						{
							openLinksInBrowser(tweet);
							break;
						}
						}
					}
				}
		).show();
	}
}
