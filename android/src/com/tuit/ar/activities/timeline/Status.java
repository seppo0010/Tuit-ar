package com.tuit.ar.activities.timeline;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tuit.ar.R;
import com.tuit.ar.activities.NewTweet;
import com.tuit.ar.activities.Preferences;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;

abstract public class Status extends Timeline {
	protected static final int TWEET_MENU_REPLY = 0;
	protected static final int TWEET_MENU_RETWEET_MANUAL = 1;
	protected static final int TWEET_MENU_SHARE = 2;
	protected static final int TWEET_MENU_SHOW_PROFILE = 3;
	protected static final int TWEET_MENU_OPEN_LINKS = 4;

	protected static final int MY_TWEET_MENU_REPLY = 0;
	protected static final int MY_TWEET_MENU_DELETE = 1;
	protected static final int MY_TWEET_MENU_SHARE = 2;
	protected static final int MY_TWEET_MENU_SHOW_PROFILE = 3;
	protected static final int MY_TWEET_MENU_OPEN_LINKS = 4;

	ArrayList<com.tuit.ar.models.Status> tweets;

	abstract protected com.tuit.ar.models.timeline.Status getTimeline();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = getTimeline().getTweets();
		this.setListAdapter(timelineAdapter = new TimelineAdapter<com.tuit.ar.models.Status>(this, tweets));
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
		final com.tuit.ar.models.Status tweet = (com.tuit.ar.models.Status) tweets.get(position);
		// FIXME: use user id instead of username!
		final boolean mine = tweet.getUsername().equals(Twitter.getInstance().getDefaultAccount().getUsername());
		new AlertDialog.Builder(this).
		setTitle(getString(R.string.executeAction)).
		setItems(mine ? R.array.myTweetOptions : R.array.tweetOptions, mine ?
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case MY_TWEET_MENU_REPLY:
						{
							Intent intent = new Intent(getApplicationContext(), NewTweet.class);
							intent.putExtra("reply_to_id", tweet.getId());
							intent.putExtra("reply_to_username", tweet.getUsername());
							intent.putExtra("default_text", "@" + tweet.getUsername() + " ");
							startActivity(intent);
							break;
						}
						case MY_TWEET_MENU_SHARE:
						{
							shareTweet(tweet);
							break;
						}
						case MY_TWEET_MENU_SHOW_PROFILE:
						{
							showProfile(tweet.getUser());
							break;
						}
						case MY_TWEET_MENU_OPEN_LINKS:
						{
							openLinksInBrowser(tweet);
							break;
						}
						}
					}
				} :
			new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TWEET_MENU_REPLY:
				{
					Intent intent = new Intent(getApplicationContext(), NewTweet.class);
					intent.putExtra("reply_to_id", tweet.getId());
					intent.putExtra("reply_to_username", tweet.getUsername());
					intent.putExtra("default_text", "@" + tweet.getUsername() + " ");
					startActivity(intent);
					break;
				}
				case TWEET_MENU_RETWEET_MANUAL:
				{
					Intent intent = new Intent(getApplicationContext(), NewTweet.class);
					intent.putExtra("reply_to_id", tweet.getId());
					intent.putExtra("reply_to_username", tweet.getUsername());
					intent.putExtra("default_text", "RT @" + tweet.getUsername() + ": " + tweet.getMessage());
					startActivity(intent);
					break;
				}
				case TWEET_MENU_SHARE:
				{
					shareTweet(tweet);
					break;
				}
				case TWEET_MENU_SHOW_PROFILE:
				{
					showProfile(tweet.getUser());
					break;
				}
				case TWEET_MENU_OPEN_LINKS:
				{
					openLinksInBrowser(tweet);
					break;
				}
				}
			}
		}).show();
	}

	protected void shareTweet(com.tuit.ar.models.Status tweet) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); 
		// FIXME: no sprintf... this will do it, for now
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareSubject).replace("%s", tweet.getUsername()));
		intent.putExtra(Intent.EXTRA_TEXT, tweet.getMessage());
		startActivity(Intent.createChooser(intent, getString(R.string.shareChooserTitle)));
	}

}
