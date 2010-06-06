package com.tuit.ar.activities.timeline;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tuit.ar.R;
import com.tuit.ar.activities.NewTweet;
import com.tuit.ar.activities.Timeline;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.models.Settings;
import com.tuit.ar.models.SettingsObserver;

abstract public class Status extends Timeline implements SettingsObserver {
	protected static final int TWEET_MENU_REPLY = 0;
	protected static final int TWEET_MENU_RETWEET = 1;
	protected static final int TWEET_MENU_RETWEET_MANUAL = 2;
	protected static final int TWEET_MENU_SHARE = 3;
	protected static final int TWEET_MENU_SHOW_PROFILE = 4;
	protected static final int TWEET_MENU_OPEN_LINKS = 5;
	protected static final int TWEET_MENU_ADD_TO_FAVORITES = 6;
	protected static final int TWEET_MENU_VIEW_CONVERSATION = 7;

	protected static final int MY_TWEET_MENU_REPLY = 0;
	protected static final int MY_TWEET_MENU_DELETE = 1;
	protected static final int MY_TWEET_MENU_SHARE = 2;
	protected static final int MY_TWEET_MENU_SHOW_PROFILE = 3;
	protected static final int MY_TWEET_MENU_OPEN_LINKS = 4;
	protected static final int MY_TWEET_MENU_ADD_TO_FAVORITES = 5;
	protected static final int MY_TWEET_MENU_VIEW_CONVERSATION = 6;

	protected ArrayList<com.tuit.ar.models.Status> tweets = new ArrayList<com.tuit.ar.models.Status>();
	protected ArrayList<com.tuit.ar.models.Status> filteredTweets = new ArrayList<com.tuit.ar.models.Status>();
	protected String[] filters = null;

	abstract protected com.tuit.ar.models.timeline.Status getTimeline();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.getInstance().addObserver(this);
		setFilters();
		addTweets(getTimeline().getTweets());
		
		this.setListAdapter(timelineAdapter = new TimelineAdapter<com.tuit.ar.models.Status>(this, filteredTweets));
	}

	public void settingsHasChanged(Settings settings) {
		setFilters();
		timelineAdapter.notifyDataSetChanged();
	}

	protected void setFilters() {
		Settings settings = Settings.getInstance();
		String filters = settings.getSharedPreferences(this).getString(Settings.FILTER, "");
		String[] newFilters;
		if (filters.equals("")) newFilters = new String[]{};
		else newFilters = filters.split("\n");
		if (newFilters.equals(this.filters)) return;
		this.filters = newFilters;
		filteredTweets.clear();
		addTweets(this.tweets);
	}

	protected void addTweet(com.tuit.ar.models.Status tweet) {
		// Not sure why this happen.
		if (tweet == null) return;
		if (this.tweets.contains(tweet) == false) this.tweets.add(0, tweet);
		if (tweetShouldDisplay(tweet)) {
			filteredTweets.add(0, tweet);
		}
	}

	protected void addTweets(ArrayList<com.tuit.ar.models.Status> tweets) {
		if (this.tweets.contains(tweets) == false) this.tweets.addAll(0, tweets);
		for (int i = tweets.size() - 1; i >= 0; i--) {
			addTweet(tweets.get(i));
		}
	}

	protected void addTweets(Collection<com.tuit.ar.models.Status> tweets) {
		if (this.tweets.contains(tweets) == false) this.tweets.addAll(0, tweets);
		com.tuit.ar.models.Status[] tweetsToIterate = tweets.toArray(new com.tuit.ar.models.Status[]{});
		for (int i = tweetsToIterate.length - 1; i >= 0; i--) {
			addTweet(tweetsToIterate[i]);
		}
	}

	protected boolean tweetShouldDisplay(com.tuit.ar.models.Status tweet) {
		String text = tweet.getText();
		for (String filter : filters) {
			if (text.contains(filter)) return false;
		}
		return true;
	}

	public void timelineHasChanged(com.tuit.ar.models.Timeline timeline) {
		if (filteredTweets.size() == 0)
			addTweets(getTimeline().getTweets());
		else
			addTweets(getTimeline().getTweetsNewerThan(tweets.get(0)));

		if (isVisible)
			timelineAdapter.notifyDataSetChanged();
	}

	protected void onListItemClick (ListView l, View v, int position, long id) {
		final com.tuit.ar.models.Status tweet = (com.tuit.ar.models.Status) filteredTweets.get(position);
		// FIXME: use user id instead of username!
		final boolean mine = tweet.getUsername().equals(Twitter.getInstance().getDefaultAccount().getUsername());
	
		CharSequence[] options = getResources().getTextArray(mine ? R.array.myTweetOptions : R.array.tweetOptions);
		ArrayList<CharSequence> opts = new ArrayList<CharSequence>(options.length);
		for (int i = 0; i < options.length; i++) {
			if (((mine && i == MY_TWEET_MENU_ADD_TO_FAVORITES) || (!mine && i == TWEET_MENU_ADD_TO_FAVORITES)) && tweet.isFavorited())
				opts.add(getString(R.string.removeFromFavorites));
			else
				opts.add(options[i]);
		}
		if (tweet.getInReplyToStatusId() > 0) {
			opts.add(getString(R.string.viewConversation));
		}

		new AlertDialog.Builder(this).
		setTitle(getString(R.string.executeAction)).
		setItems(opts.toArray(new CharSequence[]{}), mine ?
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
							ArrayList<String> users = tweet.getNamedUsers();
							if (users.size() > 0) {
								String sender = "@"+tweet.getUsername();
								if (users.contains(sender)) {
									users.remove(sender);
								}
								users.add(0, sender);
								selectUser(users);
							} else {
								showProfile(tweet.getUser());
							}
							break;
						}
						case MY_TWEET_MENU_OPEN_LINKS:
						{
							openLinksInBrowser(tweet);
							break;
						}
						case MY_TWEET_MENU_ADD_TO_FAVORITES:
						{
							if (tweet.isFavorited()) tweet.removeFromFavorites();
							else tweet.addToFavorites();
							break;
						}
						case MY_TWEET_MENU_VIEW_CONVERSATION:
						{
							ViewConversation.status = tweet;
							Intent intent = new Intent(Status.this, ViewConversation.class);
							startActivity(intent);
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
				case TWEET_MENU_RETWEET:
				{
			        new AlertDialog.Builder(Status.this)
			        .setMessage(R.string.confirmRetweet)
			        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	try {
			            		tweet.retweet();
			            	} catch (Exception e) {
			            	}
			            }
			        })
			        .setNegativeButton(R.string.no, null)
			        .show();
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
					ArrayList<String> users = tweet.getNamedUsers();
					if (users.size() > 0) {
						String sender = "@"+tweet.getUsername();
						if (users.contains(sender)) {
							users.remove(sender);
						}
						users.add(0, sender);
						selectUser(users);
					} else {
						showProfile(tweet.getUser());
					}
					break;
				}
				case TWEET_MENU_OPEN_LINKS:
				{
					openLinksInBrowser(tweet);
					break;
				}
				case TWEET_MENU_ADD_TO_FAVORITES:
				{
					if (tweet.isFavorited()) tweet.removeFromFavorites();
					else tweet.addToFavorites();
					break;
				}
				case TWEET_MENU_VIEW_CONVERSATION:
				{
					ViewConversation.status = tweet;
					Intent intent = new Intent(Status.this, ViewConversation.class);
					startActivity(intent);
					break;
				}
				}
			}
		}).show();
	}

	protected void selectUser(final ArrayList<String> users) {
		new AlertDialog.Builder(this).
		setTitle(getString(R.string.executeAction)).
		setItems(users.toArray(new CharSequence[]{}), new OnClickListener() {
			public void onClick(DialogInterface arg0, int pos) {
				showProfile(users.get(pos).substring(1));
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
