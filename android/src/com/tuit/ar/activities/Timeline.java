package com.tuit.ar.activities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.timeline.Friends;
import com.tuit.ar.activities.timeline.Replies;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.models.Tweet;
import com.tuit.ar.models.timeline.TimelineObserver;
import com.tuit.ar.services.Updater;

abstract public class Timeline extends ListActivity implements TimelineObserver {
	protected static final int MENU_NEW_TWEET = 0;   
	protected static final int MENU_REFRESH = 1;   
	protected static final int MENU_FRIENDS = 2;
	protected static final int MENU_REPLIES = 3;
	protected static final int MENU_DIRECT = 4;
	protected static final int MENU_PREFERENCES = 5;

	protected static final int TWEET_MENU_REPLY = 0;
	protected static final int TWEET_MENU_RETWEET_MANUAL = 1;
	protected static final int TWEET_MENU_SHARE = 2;
	protected static final int TWEET_MENU_OPEN_LINKS = 3;
	protected static final int TWEET_MENU_OPEN_LINKS_IN_BAKGROUND = 4;

	protected static final int MY_TWEET_MENU_REPLY = 0;
	protected static final int MY_TWEET_MENU_DELETE = 1;
	protected static final int MY_TWEET_MENU_SHARE = 2;
	protected static final int MY_TWEET_MENU_OPEN_LINKS = 3;
	protected static final int MY_TWEET_MENU_OPEN_LINKS_IN_BAKGROUND = 4;

	static private int notificationID = 1;

	HashMap<String, WebView> webs = new HashMap<String, WebView>();
	WebView activeWeb = null;
	FrameLayout webContainer = null;
	
	ArrayList<Tweet> tweets;
	TimelineAdapter timelineAdapter;
	protected boolean isVisible;
	protected String newestTweet = "";

	abstract protected com.tuit.ar.models.Timeline getTimeline();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.timeline);

		tweets = getTimeline().getTweets(); 

		this.setListAdapter(timelineAdapter = new TimelineAdapter(this));

		getTimeline().addObserver(this);
		getTimeline().refresh();
		this.startService(new Intent(this, Updater.class));
	}

	protected void onNewIntent(Intent intent) {
		String url = intent.getStringExtra("url");
		if (url != null) {
			if (webs.containsKey(url)) {
				if (activeWeb != null) {
					removeActiveWeb();
				}
				activeWeb = webs.get(url);
				if (webContainer == null)
				{
					webContainer = new FrameLayout(this);
					addContentView(webContainer, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				}
				webContainer.addView(activeWeb);
				webs.remove(url);

				int notificationID = intent.getIntExtra("notificationID", 0);
				if (notificationID > 0) {
					String ns = Context.NOTIFICATION_SERVICE;
					final NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
					mNotificationManager.cancel(notificationID);
				}
			}
		}
	}

	private void removeActiveWeb() {
		webContainer.removeView(activeWeb);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (activeWeb != null) {
				removeActiveWeb();
	        	return true;
			}
        }
        return false;
	}

	protected void onResume() {
    	super.onResume();
    	onNewIntent(getIntent());
    	isVisible = true;
    	timelineAdapter.notifyDataSetChanged();
    }

    protected void onPause() {
    	super.onPause();
    	isVisible = false;
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		getTimeline().removeObserver(this);
	}

	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case MENU_REFRESH:
	    {
	        refresh();
	        return true;
	    }
	    case MENU_FRIENDS:
	    {
	    	Intent intent = new Intent(this.getApplicationContext(), Friends.class);
	    	this.startActivity(intent);		
	    	return true;
	    }
	    case MENU_NEW_TWEET:
	    {
	    	Intent intent = new Intent(this.getApplicationContext(), NewTweet.class);
	    	this.startActivity(intent);		
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

	protected void onListItemClick (ListView l, View v, int position, long id) {
		final Tweet tweet = tweets.get(position);
		// FIXME: use user id instead of username!
		final boolean mine = tweet.getUsername().equals(Twitter.getInstance().getDefaultAccount().getUsername());
		new AlertDialog.Builder(this).
		setTitle(getString(R.string.executeAction)).
		setItems(mine ? R.array.myTweetOptions : R.array.tweetOptions, mine ?
				new OnClickListener() {
					@Override
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
						case MY_TWEET_MENU_OPEN_LINKS:
						{
							openLinksInBrowser(tweet);
							break;
						}
						case MY_TWEET_MENU_OPEN_LINKS_IN_BAKGROUND:
						{
							openLinksInBackground(tweet);
							break;
						}
						}
					}
				} :
			new OnClickListener() {
			@Override
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
				case TWEET_MENU_OPEN_LINKS:
				{
					openLinksInBrowser(tweet);
					break;
				}
				case TWEET_MENU_OPEN_LINKS_IN_BAKGROUND:
				{
					openLinksInBackground(tweet);
					break;
				}
				}
			}
		}).show();
	}

	protected void openLinksInBackground(Tweet tweet) {
		final String[] urls = parseUrls(tweet.getMessage());
		if (urls.length == 0) {
			Toast.makeText(this, getString(R.string.noURLFound), Toast.LENGTH_SHORT).show();
		} else if (urls.length == 1) {
			loadURLInBackground(urls[0]);
		} else { // we have 2+ urls
			new AlertDialog.Builder(this).
			setTitle(getString(R.string.selectURL)).
			setItems(urls,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							loadURLInBackground(urls[which]);
						}
			}).show();
		}
	}

	protected void loadURLInBackground(final String url) {
		final WebView webview = new WebView(this);
		webs.put(url, webview);
		webview.loadUrl(url);
		webview.getSettings().setJavaScriptEnabled(true);

		String ns = Context.NOTIFICATION_SERVICE;
		final NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon_web;
		CharSequence tickerText = "Loading web...";
		long when = System.currentTimeMillis();

		final Notification notification = new Notification(icon, tickerText, when);
		final Intent notificationIntent = new Intent(this, this.getClass());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.contentIntent = contentIntent;

		final RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.background_url);
		contentView.setImageViewResource(R.id.image, R.drawable.icon_web);
		contentView.setTextViewText(R.id.text, "Loading web: " + url);
		notification.contentView = contentView;

		final int _notificationID = ++notificationID;
		mNotificationManager.notify(_notificationID, notification);

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				contentView.setProgressBar(R.id.progress, 100, progress, false);
				mNotificationManager.notify(_notificationID, notification);
				if (progress >= 100) {
					mNotificationManager.cancel(_notificationID);
					notifyFinished(url);
				}
			}
		});
		webview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					webs.remove(url);
					webview.setVisibility(View.INVISIBLE);
					webview.destroy();
					return true;
				}
				return false;
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(Timeline.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
				webs.remove(url);
			}
		});
	}

	protected void notifyFinished(String url) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon_web;        // icon from resources
		CharSequence tickerText = "Hello";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();      // application Context
		CharSequence contentTitle = url;  // expanded message title
		CharSequence contentText = "Tap to open";      // expanded message text

		Intent notificationIntent = new Intent(this, this.getClass());
		notificationIntent.setAction(Intent.ACTION_VIEW);
		notificationIntent.putExtra("url", url);
		int notificationID = ++Timeline.notificationID;
		notificationIntent.putExtra("notificationID", notificationID);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.contentIntent = contentIntent;
	
		mNotificationManager.notify(notificationID, notification);
	}

	protected void openLinksInBrowser(Tweet tweet) {
		final String[] urls = parseUrls(tweet.getMessage());
		if (urls.length == 0) {
			Toast.makeText(this, getString(R.string.noURLFound), Toast.LENGTH_SHORT).show();
		} else if (urls.length == 1) {
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[0])));
		} else { // we have 2+ urls
			new AlertDialog.Builder(this).
			setTitle(getString(R.string.selectURL)).
			setItems(urls,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[which])));
						}
			}).show();
		}
	}

	static private String[] parseUrls(String message) {
        String [] parts = message.split("\\s");

        ArrayList<String> foundURLs = new ArrayList<String>();
        for( String item : parts ) try {
            foundURLs.add((new URL(item)).toString());
        } catch (MalformedURLException e) {
        }

        return (String[])foundURLs.toArray(new String[foundURLs.size()]);
	}

	protected void shareTweet(Tweet tweet) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); 
		// FIXME: no sprintf... this will do it, for now
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareSubject).replace("%s", tweet.getUsername()));
		intent.putExtra(Intent.EXTRA_TEXT, tweet.getMessage());
		startActivity(Intent.createChooser(intent, getString(R.string.shareChooserTitle)));
	}

	protected void refresh() {
		getTimeline().refresh();
	}

	public void timelineRequestStarted(com.tuit.ar.models.Timeline timeline) {
		this.setProgressBarIndeterminateVisibility(true);
	}
	public void timelineRequestFinished(com.tuit.ar.models.Timeline timeline) {
		this.setProgressBarIndeterminateVisibility(false);
	}

	public void timelineHasChanged(com.tuit.ar.models.Timeline timeline) {
		if (tweets.size() == 0)
			tweets.addAll(getTimeline().getTweets());
		else
			tweets.addAll(0, getTimeline().getTweetsNewerThan(tweets.get(0)));
		if (isVisible)
			timelineAdapter.notifyDataSetChanged();
	}

	public void timelineUpdateHasFailed(com.tuit.ar.models.Timeline timeline) {
		if (isVisible) {
			Toast.makeText(this, getString(R.string.unableToFetchTimeline), Toast.LENGTH_SHORT).show();
		}
	}

	protected class TimelineAdapter extends ArrayAdapter<Tweet> 
	{
		Activity context;
		HashMap<View, TimelineElement> elements = new HashMap<View, TimelineElement>();

		public TimelineAdapter(Activity context)
		{
			super(context, R.layout.timeline_element, tweets);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			TimelineElement element = getTimelineElement(convertView);

			Tweet tweet = tweets.get(position);
			if (element.currentTweet == tweet) return element.getView();

			element.getUsername().setText("@" + tweet.getUsername());
			element.getMessage().setText(tweet.getMessage());
			element.getDate().setText(Tweet.calculateElapsed(tweet.getDateMillis()));
			element.currentTweet = tweet;

			return element.getView();
		}

		private TimelineElement getTimelineElement(View convertView) {
			if (convertView == null)
			{
				convertView = View.inflate(this.context, R.layout.timeline_element, null);
			}

			if (!elements.containsKey(convertView)) {
				elements.put(convertView, new TimelineElement(convertView));
			}
			return elements.get(convertView);
		}
	}

	protected class TimelineElement {
		private View view;
		private TextView username;
		private TextView message;
		private TextView date;
		public Tweet currentTweet; 

		public TimelineElement(View view) {
			super();
			this.view = view;
		}

		public TextView getUsername() {
			if (username != null) return username;
			else return username = (TextView)view.findViewById(R.id.username);
		}

		public TextView getMessage() {
			if (message != null) return message;
			else return message = (TextView)view.findViewById(R.id.message);
		}

		public TextView getDate() {
			if (date != null) return date;
			else return date = (TextView)view.findViewById(R.id.date);
		}

		public View getView() { return view; }
	}
}
